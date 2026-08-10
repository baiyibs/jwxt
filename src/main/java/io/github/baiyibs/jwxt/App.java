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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class App {
    private static final AppConfig CONFIG;

    static {
        CONFIG = ConfigManager.getInstance().getConfig();
    }

    public static void main(String[] args) {
        LinkedList<AppConfig.Account> accountList = CONFIG.getAccount();
        log.info("读取到 {} 个账号", accountList.size());

        AtomicInteger threadNumber = new AtomicInteger(1);
        ExecutorService executor = Executors.newFixedThreadPool(5, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("Playwright-Worker-" + threadNumber.getAndIncrement());
            return thread;
        });

        try {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (AppConfig.Account account : accountList) {
                futures.add(CompletableFuture.runAsync(() -> {
                    try (PlaywrightManager pm = new PlaywrightManager()) {
                        Page page = pm.newPage(account.getUsername());
                        // 登录
                        AuthService authService = new AuthService(page);
                        try {
                            authService.login(account.getUsername(), account.getPassword());
                        } catch (LoginException e) {
                            log.error("{}", e.getMessage());
                        }
                        Student student = authService.getStudent();
                        Transcript transcript = authService.getTranscript();
                        log.info("{}", transcript.getTotalCredit());
                    } catch (Exception e) {
                        log.error("发生异常: {}", e.getMessage());
                    }
                }, executor));
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } finally {
            executor.shutdown();
            PlaywrightManager.shutdown();
        }
    }
}