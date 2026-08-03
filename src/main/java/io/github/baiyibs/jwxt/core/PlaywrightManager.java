package io.github.baiyibs.jwxt.core;

import com.microsoft.playwright.*;
import io.github.baiyibs.jwxt.config.AppConfig;
import io.github.baiyibs.jwxt.config.BrowserConfigLoader;
import io.github.kihdev.playwright.stealth4j.Stealth4j;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;

@Slf4j
public class PlaywrightManager implements AutoCloseable{
    private final Playwright playwright;
    private final Browser browser;
    @Getter
    private final BrowserContext browserContext;
    @Getter
    private LinkedHashMap<String, Page> pageList = new LinkedHashMap<>();

    public PlaywrightManager(AppConfig config) {
        playwright = Playwright.create();
        BrowserType.LaunchOptions launchOptions = BrowserConfigLoader.loadLaunchOptions(config);
        browser = playwright.chromium().launch(launchOptions);
        browserContext = Stealth4j.newStealthContext(browser);

        browserContext.onFrameNavigated(frame ->
                log.info("导航 -> {}", frame.url())
        );

        log.debug("PlaywrightManager 初始化完成");
    }

    public Page newPage(String pageName) {
        Page page = browserContext.newPage();
        pageList.put(pageName, page);
        return page;
    }

    public Page getPage(String pageName) {
        return pageList.get(pageName);
    }

    @Override
    public void close() {
        try {
            if (!pageList.isEmpty()) {
                pageList.forEach((pageName, page) -> {
                    if (page != null) {
                        page.close();
                        log.debug("Page {} 已关闭", pageName);
                    }
                });
            }
            if (browserContext != null) {
                browserContext.close();
                log.debug("BrowserContext 已关闭");
            }
            if (browser != null) {
                browser.close();
                log.debug("Browser 已关闭");
            }
            if (playwright != null) {
                playwright.close();
                log.debug("Playwright 已关闭");
            }
        } catch (Exception e) {
            log.error("关闭 Playwright 资源失败: {}", e.getMessage());
        }
    }
}
