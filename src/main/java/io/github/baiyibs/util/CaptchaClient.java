package io.github.baiyibs.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.baiyibs.config.ConfigManager;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CaptchaClient {
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String OCR_API_URL = ConfigManager.getInstance().getConfig().getOcrApiUrl();

    private CaptchaClient() {}

    /**
     * 识别验证码，失败返回 null
     */
    public static String recognizeCaptcha(byte[] imageBytes) {
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "captcha.png",
                            RequestBody.create(imageBytes, MediaType.parse("application/octet-stream")))
                    .build();

            Request request = new Request.Builder()
                    .url(OCR_API_URL)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("错误代码: " + response);
                }
                String responseBody = response.body().string();
                JsonNode root = mapper.readTree(responseBody);
                int code = root.path("code").asInt(-1);
                if (code != 200) {
                    String msg = root.path("msg").asText("未知错误");
                    throw new IOException("OCR服务出错: " + msg);
                }
                JsonNode dataNode = root.path("data");
                return dataNode.isMissingNode() ? null : dataNode.asText();
            }
        } catch (Exception e) {
            log.error("验证码识别失败: {}", e.getMessage());
            return null;
        }
    }
}