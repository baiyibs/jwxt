package io.github.baiyibs;

import com.microsoft.playwright.*;
import io.github.baiyibs.config.BrowserConfigLoader;
import io.github.baiyibs.config.ConfigManager;
import io.github.baiyibs.config.AppConfig;
import io.github.baiyibs.helper.CaptchaHelper;
import io.github.kihdev.playwright.stealth4j.Stealth4j;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Main {
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

            Login(page, config);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void Login(Page page, AppConfig config) {
        String baseUrl = "https://jw.educationgroup.cn/ytkjxy_jsxsd/";
        log.info("进入教务系统登录页面 -> {}", baseUrl);
        page.navigate(baseUrl);

        log.info("进行登录操作...");
        page.locator("#userAccount").fill(config.getAccount().getUsername());
        page.locator("#userPassword").fill(config.getAccount().getPassword());
        log.info("开始识别验证码...");
        fillCaptchaCode(page);
        page.locator(".login_btn").click();
    }

    private static void fillCaptchaCode(Page page) {
        Path imagePath = Paths.get(System.getProperty("user.dir"),"captcha.png");
        CaptchaHelper helper = new CaptchaHelper();

        String code = helper.recognizeWithRetry(() -> {
            // 刷新验证码
            page.click("#SafeCodeImg");
            // 等待验证码出现
            page.locator("#SafeCodeImg").waitFor();
            // 对验证码进行截图
            page.locator("#SafeCodeImg")
                    .screenshot(new Locator.ScreenshotOptions()
                            .setPath(imagePath));
            File file = new File(imagePath.toUri());
            return (file.exists() && file.length() > 0) ? file : null;
        }, 4);

        if (code == null) {
            System.exit(1);
        }

        page.locator("#RANDOMCODE").fill(code);
    }
}