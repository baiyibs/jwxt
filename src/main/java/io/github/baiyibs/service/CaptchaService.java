package io.github.baiyibs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.baiyibs.config.ConfigManager;
import io.github.baiyibs.exception.OcrException;
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
    public String recognize(File imageFile) throws IOException, OcrException {
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
                throw new IOException(String.format("HTTP %d: %s", response.code(), responseBody));
            }

            OcrResponse result = mapper.readValue(responseBody, OcrResponse.class);

            if (result.getCode() != 200) {
                throw new OcrException(String.format("请求错误 %d: %s", result.getCode(), result.getMessage()));
            }
            return result.getData();
        }
    }

}