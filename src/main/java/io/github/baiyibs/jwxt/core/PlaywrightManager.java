package io.github.baiyibs.jwxt.core;

import com.microsoft.playwright.*;
import io.github.baiyibs.jwxt.App;
import io.github.baiyibs.jwxt.config.BrowserConfigLoader;
import io.github.kihdev.playwright.stealth4j.Stealth4j;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;

@Slf4j
public class PlaywrightManager implements AutoCloseable{
    private static final Playwright PLAYWRIGHT;
    private static final Browser BROWSER;
    
    static {
        PLAYWRIGHT = Playwright.create();
        BrowserType.LaunchOptions launchOptions = BrowserConfigLoader.loadLaunchOptions(App.CONFIG);
        BROWSER = PLAYWRIGHT.chromium().launch(launchOptions);
        log.debug("全局 Browser 初始化完成");
    }
    
    @Getter
    private final BrowserContext browserContext;
    @Getter
    private final LinkedHashMap<String, Page> pageList = new LinkedHashMap<>();
    
    public PlaywrightManager() {
        browserContext = Stealth4j.newStealthContext(BROWSER);
        log.debug("新的 BrowserContext 创建成功");
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
                pageList.clear();
            }
            if (browserContext != null) {
                browserContext.close();
                log.debug("BrowserContext 已关闭");
            }
        } catch (Exception e) {
            log.error("关闭任务资源失败: {}", e.getMessage());
        }
    }
    
    public static void shutdown() {
        try {
            if (BROWSER != null) {
                BROWSER.close();
            }
            if (PLAYWRIGHT != null) {
                PLAYWRIGHT.close();
            }
            log.debug("关闭全局资源成功");
        } catch (Exception e) {
            log.error("关闭全局资源失败: {}", e.getMessage());
        }
    }
}
