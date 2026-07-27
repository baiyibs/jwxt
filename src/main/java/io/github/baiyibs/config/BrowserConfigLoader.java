package io.github.baiyibs.config;

import com.microsoft.playwright.BrowserType;

public class BrowserConfigLoader {
    /**
     * 从 AppConfig 中读取 browser 配置,生成 LaunchOptions
     * @param config 配置文件
     * @return 浏览器启动参数
     */
    public static BrowserType.LaunchOptions loadLaunchOptions(AppConfig config) {
        AppConfig.BrowserConfig browserConfig = config.getBrowser();
        return new BrowserType.LaunchOptions()
                .setHeadless(browserConfig.isHeadless());
    }
}
