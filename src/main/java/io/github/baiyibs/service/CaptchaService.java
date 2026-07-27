package io.github.baiyibs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.baiyibs.config.ConfigManager;
import io.github.baiyibs.model.OcrResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CaptchaService {
    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private final String ocrApiUrl;

    public CaptchaService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.mapper = new ObjectMapper();
        this.ocrApiUrl = ConfigManager.getInstance().getConfig().getOcrApiUrl();
    }

    /**
     * 识别验证码
     * @param imageFile 验证码图片
     * @return 识别出的验证码字符串
     */
    public String recognize(File imageFile) {
        RequestBody fileBody = RequestBody.create(
                imageFile,
                MediaType.get("image/png")
        );

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imageFile.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(ocrApiUrl)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                log.error("HTTP 请求失败，状态码: {}, 响应: {}", response.code(), responseBody);
                return null;
            }

            OcrResponse result = mapper.readValue(responseBody, OcrResponse.class);

            if (result.getCode() == 200) {
                return result.getData();
            } else {
                log.error("识别失败，状态码: {}, 错误信息: {}", result.getCode(), result.getMessage());
                return null;
            }

        } catch (IOException e) {
            log.error("发生错误: {}", e.getMessage());
            return null;
        }
    }
}