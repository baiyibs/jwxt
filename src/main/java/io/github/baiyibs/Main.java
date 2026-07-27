package io.github.baiyibs;

import com.microsoft.playwright.*;
import io.github.baiyibs.config.BrowserConfigLoader;
import io.github.baiyibs.config.ConfigManager;
import io.github.baiyibs.config.AppConfig;
import io.github.baiyibs.helper.CaptchaHelper;
import io.github.kihdev.playwright.stealth4j.Stealth4j;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

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

            browserContext.onFrameNavigated(frame -> log.info("访问 -> {}", frame.url()));

            Login(page, config);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void Login(Page page, AppConfig config) {
        String baseUrl = "https://jw.educationgroup.cn/ytkjxy_jsxsd/";
        log.info("进入教务系统登录页面");
        page.navigate(baseUrl);

        log.info("进行登录操作...");
        page.locator("#userAccount").fill(config.getAccount().getUsername());
        page.locator("#userPassword").fill(config.getAccount().getPassword());
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
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                log.error("手动输入失败!");
                System.exit(1);
            }
            System.out.print("请手动输入验证码: ");
            Scanner scanner = new Scanner(System.in);
            code = scanner.nextLine();
        }

        page.locator("#RANDOMCODE").fill(code);
    }

    public static File screenshotElement(Locator locator, Path path) {
        locator.screenshot(new Locator.ScreenshotOptions()
                .setPath(path));
        return path.toFile();
    }
}