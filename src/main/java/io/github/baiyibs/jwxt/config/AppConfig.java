package io.github.baiyibs.jwxt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@NoArgsConstructor
public class AppConfig {
    private BaseConfig base;
    private BrowserConfig browser = new BrowserConfig();
    private LinkedList<Account> account;

    @Data
    @NoArgsConstructor
    public static class BaseConfig {
        @JsonProperty("OCR_API_URL")
        private String ocrApiUrl;
        private String version;
    }

    @Data
    @NoArgsConstructor
    public static class BrowserConfig {
        private boolean headless = true;
    }

    @Data
    @NoArgsConstructor
    public static class Account {
        private String username;
        private String password;
    }
}
