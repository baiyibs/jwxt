package io.github.baiyibs.jwxt;

import com.microsoft.playwright.*;
import io.github.baiyibs.jwxt.config.ConfigManager;
import io.github.baiyibs.jwxt.config.AppConfig;
import io.github.baiyibs.jwxt.core.PlaywrightManager;
import io.github.baiyibs.jwxt.model.Course;
import io.github.baiyibs.jwxt.model.Student;
import io.github.baiyibs.jwxt.service.AuthService;
import io.github.baiyibs.jwxt.util.CourseParser;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class App {
    private static final AppConfig CONFIG;

    static {
        CONFIG = ConfigManager.getInstance().getConfig();
    }

    public static void main(String[] args) {
        try (PlaywrightManager pm = new PlaywrightManager(CONFIG)) {
            Page page = pm.getPage();
            // 登录
            AuthService authService = new AuthService(page);
            authService.login(CONFIG.getAccount().getUsername(), CONFIG.getAccount().getPassword());

            Student student = authService.getStudent();
            if (student != null) {
                log.info(student.toString());
            }

            page.navigate("https://jw.educationgroup.cn/ytkjxy_jsxsd/kscj/cjcx_list");
            String dataList =  page.locator("#dataList").innerText();
            List<Course> courseList = CourseParser.parseFromText(dataList);
            courseList.forEach(course -> log.info("{}", course));

        } catch (Exception e) {
            log.error("发生异常: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}