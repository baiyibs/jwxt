package io.github.baiyibs.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

@Slf4j
public class ConfigManager {
    private static final ObjectMapper MAPPER = new YAMLMapper()
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .enable(SerializationFeature.INDENT_OUTPUT);;

    private static final String DEFAULT_RESOURCE = "default-config.yaml";
    private final Path EXTERNAL_PATH = Paths.get(System.getProperty("user.dir"), "config", "config.yaml");

    private static volatile ConfigManager instance;
    @Getter
    private AppConfig config;

    private ConfigManager() {
        loadConfig();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    private void loadConfig() {
        try {
            // 从外部加载配置文件
            if (Files.exists(EXTERNAL_PATH)) {
                this.config = MAPPER.readValue(EXTERNAL_PATH.toFile(), AppConfig.class);
                log.info("加载配置文件: {}", EXTERNAL_PATH);
                return;
            }
            // 保存默认配置到本地
            Files.createDirectories(EXTERNAL_PATH.getParent());
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_RESOURCE)){
                if (inputStream == null) {
                    failLoading();
                    throw new IllegalStateException("没有找到默认配置文件: " + DEFAULT_RESOURCE);
                }

                AppConfig newConfig = MAPPER.readValue(inputStream, AppConfig.class);
                Scanner scanner = new Scanner(System.in);
                System.out.print("请输入账号: ");
                newConfig.getAccount().setUsername(scanner.nextLine());
                System.out.print("请输入密码: ");
                newConfig.getAccount().setPassword(scanner.nextLine());

                saveAndUpdate(newConfig);
                System.exit(1);
            }
        } catch (IOException e) {
            throw new RuntimeException("加载配置失败: {}", e);
        }
    }

    private static void failLoading() {
        log.error("没有找到默认配置文件: {}", DEFAULT_RESOURCE);
    }

    public void save() throws IOException {
        Files.createDirectories(EXTERNAL_PATH.getParent());
        MAPPER.writeValue(EXTERNAL_PATH.toFile(), config);
        log.info("已将配置保存到: {}", EXTERNAL_PATH);
    }

    public void saveAndUpdate(AppConfig newConfig) throws IOException {
        this.config = newConfig;
        save();
    }

    public void reload() throws IOException {
        if (Files.exists(EXTERNAL_PATH)) {
            this.config = MAPPER.readValue(EXTERNAL_PATH.toFile(), AppConfig.class);
            log.info("已重载配置文件: {}", EXTERNAL_PATH);
        } else {
            loadConfig();
        }
    }
}
