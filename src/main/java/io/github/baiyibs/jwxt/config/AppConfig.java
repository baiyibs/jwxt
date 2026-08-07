package io.github.baiyibs.jwxt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AppConfig {
    @JsonProperty("OCR_API_URL")
    private String ocrApiUrl;
    private BrowserConfig browser = new BrowserConfig();
    private List<Account> account;

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
