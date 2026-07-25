package io.github.baiyibs.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class AppConfig {
    @JsonProperty("OCR_API_URL")
    private String ocrApiUrl;
    private Account account;
    private BrowserConfig browser = new BrowserConfig();

    @Data
    @NoArgsConstructor
    public static class Account {
        private String username;
        private String password;
    }

    @Data
    @NoArgsConstructor
    public static class BrowserConfig {
        private boolean headless = true;
        private int slowMo = 0;
    }
}
