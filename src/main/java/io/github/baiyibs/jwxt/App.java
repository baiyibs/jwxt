package io.github.baiyibs.jwxt;

import com.microsoft.playwright.*;
import io.github.baiyibs.jwxt.config.ConfigManager;
import io.github.baiyibs.jwxt.config.AppConfig;
import io.github.baiyibs.jwxt.core.PlaywrightManager;
import io.github.baiyibs.jwxt.model.Student;
import io.github.baiyibs.jwxt.model.Transcript;
import io.github.baiyibs.jwxt.service.AuthService;
import lombok.extern.slf4j.Slf4j;

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
            Transcript transcript = authService.getTranscript();
            log.info("{}", transcript.getTotalCredit());

        } catch (Exception e) {
            log.error("发生异常: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}