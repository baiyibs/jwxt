package io.github.baiyibs.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppConfig {
    @JsonProperty("OCR_API_URL")
    private String ocrApiUrl;
    private BrowserConfig browser = new BrowserConfig();
    private Account account;

    @Data
    @NoArgsConstructor
    public static class BrowserConfig {
        private boolean headless = true;
        private int slowMo = 0;
    }

    @Data
    @NoArgsConstructor
    public static class Account {
        private String username;
        private String password;
    }
}
