package io.github.baiyibs;

import com.microsoft.playwright.*;
import io.github.baiyibs.config.BrowserConfigLoader;
import io.github.baiyibs.config.ConfigManager;
import io.github.baiyibs.config.AppConfig;
import io.github.kihdev.playwright.stealth4j.Stealth4j;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        // 加载配置文件
        ConfigManager configManager = ConfigManager.getInstance();
        AppConfig config = configManager.getConfig();

        // 初始化浏览器
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchOptions launchOptions = BrowserConfigLoader.loadLaunchOptions(config);
            Browser browser = playwright.chromium().launch(launchOptions);
            BrowserContext browserContext = Stealth4j.newStealthContext(browser);
            Page page = browserContext.newPage();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}