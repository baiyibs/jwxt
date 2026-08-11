package io.github.baiyibs.jwxt.helper;

import io.github.baiyibs.jwxt.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.util.function.Supplier;

@Slf4j
public class CaptchaHelper {
    private final CaptchaService service;
    private final int maxRetries;

    public CaptchaHelper() {
        this(new CaptchaService(), 3);
    }

    public CaptchaHelper(CaptchaService service, int maxRetries) {
        this.service = service;
        this.maxRetries = maxRetries;
    }

    /**
     * 带重试的验证码识别方法
     * @param imageSupplier 验证码图片提供者
     * @return 识别结果，若所有重试都失败则返回 null
     */
    public String recognizeWithRetry(Supplier<File> imageSupplier, int expectedLength) {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                attempt++;

                File imageFile = imageSupplier.get();
                if (imageFile == null || !imageFile.exists()) {
                    log.warn("第 {} 次尝试获取验证码图片失败（文件不存在或为空）", attempt);
                    continue;
                }

                log.info("开始识别验证码, 第 {} 次尝试", attempt);
                String result = service.recognize(imageFile);
                if (result.length() != expectedLength) {
                    log.warn("第 {} 次尝试失败: 验证码长度错误({})", attempt, result.length());
                    continue;
                }
                log.info("识别成功: {}", result);
                return result;
            } catch (Exception e) {
                log.warn("第 {} 次尝试失败: {}", attempt, e.getMessage());
            }
            if (attempt < maxRetries) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        log.error("识别验证码失败，已重试 {} 次", maxRetries);
        return null;
    }
}
