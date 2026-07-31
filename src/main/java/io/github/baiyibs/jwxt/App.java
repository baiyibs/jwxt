package io.github.baiyibs.jwxt;

import com.microsoft.playwright.*;
import io.github.baiyibs.jwxt.config.BrowserConfigLoader;
import io.github.baiyibs.jwxt.config.ConfigManager;
import io.github.baiyibs.jwxt.config.AppConfig;
import io.github.baiyibs.jwxt.model.Course;
import io.github.baiyibs.jwxt.model.Student;
import io.github.baiyibs.jwxt.service.AuthService;
import io.github.baiyibs.jwxt.util.CourseParser;
import io.github.baiyibs.jwxt.util.StudentParser;
import io.github.kihdev.playwright.stealth4j.Stealth4j;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class App {

    public static void main(String[] args) {
        // 加载配置文件
        ConfigManager configManager = ConfigManager.getInstance();
        AppConfig config = configManager.getConfig();

        try (Playwright playwright = Playwright.create()) {
            // 初始化浏览器
            BrowserType.LaunchOptions launchOptions = BrowserConfigLoader.loadLaunchOptions(config);
            Browser browser = playwright.chromium().launch(launchOptions);
            BrowserContext browserContext = Stealth4j.newStealthContext(browser);
            Page page = browserContext.newPage();

            browserContext.onFrameNavigated(frame -> log.info("访问 -> {}", frame.url()));
            // 登录
            AuthService authService = new AuthService(page);
            authService.Login(config.getAccount().getUsername(), config.getAccount().getPassword());

            Student student = getStudentInfo(page);
            if (student != null) {
                log.info(student.toString());
            }

            page.navigate("https://jw.educationgroup.cn/ytkjxy_jsxsd/kscj/cjcx_list");
            String dataList =  page.locator("#dataList").innerText();
            List<Course> courseList = CourseParser.parseFromText(dataList);
            courseList.forEach(course -> log.info("{}", course));

        } catch (Exception e) {
            log.error("初始化浏览器失败: {}", e.getMessage());
            System.exit(1);
        }
    }

    public static Student getStudentInfo(Page page) {
        String baseUrl = "https://jw.educationgroup.cn/ytkjxy_jsxsd/framework/xsMain_new.jsp";
        page.navigate(baseUrl);
        try {
            String studentInfo =  page.locator(".middletopttxlr").innerText();
            return StudentParser.parseFromText(studentInfo);
        } catch (Exception e) {
            log.error("获取元素失败: {}", e.getMessage());
            return null;
        }
    }
}