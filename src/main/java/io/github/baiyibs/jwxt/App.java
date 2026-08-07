package io.github.baiyibs.jwxt;

import com.microsoft.playwright.*;
import io.github.baiyibs.jwxt.config.ConfigManager;
import io.github.baiyibs.jwxt.config.AppConfig;
import io.github.baiyibs.jwxt.core.PlaywrightManager;
import io.github.baiyibs.jwxt.exception.LoginException;
import io.github.baiyibs.jwxt.model.Student;
import io.github.baiyibs.jwxt.model.Transcript;
import io.github.baiyibs.jwxt.service.AuthService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class App {
    private static final AppConfig CONFIG;

    static {
        CONFIG = ConfigManager.getInstance().getConfig();
    }

    public static void main(String[] args) {
        int accountCount = CONFIG.getAccount().size();
        log.info("读取到 {} 个账号", accountCount);
        for (int i = 0; i < accountCount; i++) {
            int finalI = i;
            CompletableFuture.runAsync(() -> {
                try (PlaywrightManager pm = new PlaywrightManager(CONFIG)) {
                    Page page = pm.newPage("测试");
                    // 登录
                    AuthService authService = new AuthService(page);
                    try {
                        authService.login(CONFIG.getAccount().get(finalI).getUsername(), CONFIG.getAccount().get(finalI).getPassword());
                    } catch (LoginException e) {
                        log.error("{}", e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                    Student student = authService.getStudent();
                    Transcript transcript = authService.getTranscript();
                    log.info("{}", transcript.getTotalCredit());
                    log.debug("获取Page测试: {}", pm.getPage("测试"));

                } catch (Exception e) {
                    log.error("发生异常: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }).join();
        }
    }
}