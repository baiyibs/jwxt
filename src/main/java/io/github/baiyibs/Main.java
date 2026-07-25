package io.github.baiyibs;

import com.microsoft.playwright.*;
import io.github.baiyibs.config.BrowserConfigLoader;
import io.github.baiyibs.config.ConfigManager;
import io.github.baiyibs.config.AppConfig;
import io.github.baiyibs.util.CaptchaClient;
import io.github.kihdev.playwright.stealth4j.Stealth4j;
import lombok.extern.slf4j.Slf4j;

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
        int maxRetriesCount = 3;
        for (int retries = 1; retries <= maxRetriesCount; retries++) {
            byte[] captchaBytes = page.locator("#SafeCodeImg").screenshot();
            String code = CaptchaClient.recognizeCaptcha(captchaBytes);
            if (code != null) {
                if (code.length() == 4) {
                    log.info("识别到验证码: {}", code);
                    page.locator("#RANDOMCODE").fill(code);
                    return;
                } else {
                    log.warn("验证码长度错误({}), 准备重试...", code.length());
                }
            } else {
                log.warn("识别验证码失败, 准备重试...");
            }
            // 刷新验证码
            page.locator("#SafeCodeImg").click();
        }
        log.error("识别验证码失败（超过最大重试次数），程序终止。");
        System.exit(1);
    }
}