package io.github.baiyibs.jwxt.service;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.baiyibs.jwxt.config.ConfigManager;
import io.github.baiyibs.jwxt.exception.OcrException;
import io.github.baiyibs.jwxt.model.OcrResponse;

import java.io.File;
import java.io.IOException;

public class CaptchaService {
    private final ObjectMapper mapper;
    private final String ocrApiUrl;

    public CaptchaService() {
        this.mapper = new ObjectMapper();
        this.ocrApiUrl = ConfigManager.getInstance().getConfig().getOcrApiUrl();
    }

    /**
     * 识别验证码
     * @param imageFile 验证码图片
     * @return 识别出的验证码字符串
     */
    public String recognize(File imageFile) throws IOException, OcrException {
        try (HttpResponse httpResponse = HttpRequest.post(ocrApiUrl)
                .form("file", imageFile)
                .timeout(30000)
                .execute()) {

            String responseBody = httpResponse.body();
            if (responseBody == null || responseBody.isEmpty()) {
                throw new IOException("OCR 服务没有返回任何数据");
            }

            OcrResponse result = mapper.readValue(responseBody, OcrResponse.class);

            if (result.getCode() != 200) {
                throw new OcrException(String.format("请求错误 %d: %s", result.getCode(), result.getMessage()));
            }
            return result.getData();
        } catch (HttpException e) {
            throw new IOException("HTTP 请求失败: " + e.getMessage(), e);
        }
    }

}