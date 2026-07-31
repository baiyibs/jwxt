package io.github.baiyibs.jwxt.service;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.github.baiyibs.jwxt.helper.CaptchaHelper;
import io.github.baiyibs.jwxt.util.ConsoleTerminal;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
public class AuthService {
    private final Page page;

    public AuthService(Page page) {
        this.page = page;
    }

    public void Login(String username, String password) {
        log.info("进入教务系统登录页面");
        String baseUrl = "https://jw.educationgroup.cn/ytkjxy_jsxsd/";
        page.navigate(baseUrl);

        log.info("进行登录操作...");
        page.locator("#userAccount").fill(username);
        page.locator("#userPassword").fill(password);
        log.info("开始识别验证码...");
        fillCaptchaCode(page);
        page.locator(".login_btn").click();

        if (Objects.equals(page.title(), "教学一体化服务平台")) {
            log.info("登录成功!");
        } else {
            log.info("登录失败!");
            System.exit(1);
        }
    }

    private static void fillCaptchaCode(Page page) {
        Path imagePath = Paths.get(System.getProperty("user.dir"),"captcha.png");
        Locator locator = page.locator("#SafeCodeImg");
        CaptchaHelper helper = new CaptchaHelper();
        // 退出时删除文件
        imagePath.toFile().deleteOnExit();

        String code = helper.recognizeWithRetry(() -> {
            // 刷新验证码
            locator.click();
            // 等待验证码出现
            locator.waitFor();
            // 对验证码进行截图
            File file = screenshotElement(locator, imagePath);
            return (file.exists() && file.length() > 0) ? file : null;
        }, 4);

        if (code == null) {
            File file = screenshotElement(locator, imagePath);
            ConsoleTerminal.displayImage(file);
            try {
                code = ConsoleTerminal.readLine("请手动输入验证码: ");
            } catch (IOException e) {
                log.error("读取输入失败: {}", e.getMessage());
            }
        }

        page.locator("#RANDOMCODE").fill(code);
    }

    private static File screenshotElement(Locator locator, Path path) {
        locator.screenshot(new Locator.ScreenshotOptions()
                .setPath(path));
        return path.toFile();
    }
}
