package io.github.baiyibs.jwxt.service;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.github.baiyibs.jwxt.exception.LoginException;
import io.github.baiyibs.jwxt.helper.CaptchaHelper;
import io.github.baiyibs.jwxt.model.Student;
import io.github.baiyibs.jwxt.util.ConsoleTerminal;
import io.github.baiyibs.jwxt.util.StudentParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Slf4j
public class AuthService {
    private final Page page;
    @Getter
    private boolean isLogin;
    @Getter
    private Student student;

    public AuthService(Page page) {
        this.page = page;
    }

    /**
     * 进行登录操作
     * @param username  账号
     * @param password  密码
     * @throws LoginException   登录失败时
     */
    public void login(String username, String password) throws LoginException {
        String baseUrl = "https://jw.educationgroup.cn/ytkjxy_jsxsd/";
        page.navigate(baseUrl);

        page.locator("#userAccount").fill(username);
        page.locator("#userPassword").fill(password);

        fillCaptchaCode(page);
        page.locator(".login_btn").click();

        if (Objects.equals(page.title(), "教学一体化服务平台")) {
            this.isLogin = true;
            String homeUrl = "https://jw.educationgroup.cn/ytkjxy_jsxsd/framework/xsMain_new.jsp";
            page.navigate(homeUrl);
            String innerText =  page.locator(".middletopttxlr").innerText();
            this.student = StudentParser.parseFromText(innerText);
            log.info("学生 {} 登录成功", student.getName());
        } else {
            this.isLogin = false;
            this.student = null;
            throw new LoginException(username + " 登录失败!");
        }
    }

    private void fillCaptchaCode(Page page) {
        Path imagePath;
        try {
            imagePath = Files.createTempFile("captcha", ".png");
        } catch (IOException e) {
            throw new RuntimeException("创建临时文件失败: ", e);
        }
        imagePath.toFile().deleteOnExit();

        Locator locator = page.locator("#SafeCodeImg");
        CaptchaHelper helper = new CaptchaHelper();

        String code = helper.recognizeWithRetry(() -> {
            // 刷新验证码
            locator.click();
            // 等待验证码出现
            locator.waitFor();
            // 对验证码进行截图
            File file = screenshotElement(locator, imagePath);
            return (file.exists() && file.length() > 0) ? file : null;
        }, 4);

        if (code == null) {
            File file = screenshotElement(locator, imagePath);
            ConsoleTerminal.displayImage(file);
            try {
                code = ConsoleTerminal.readLine("请手动输入验证码: ");
            } catch (IOException e) {
                log.error("读取输入失败: {}", e.getMessage());
                throw new RuntimeException("手动输入验证码失败: ", e);
            }
        }

        page.locator("#RANDOMCODE").fill(code);
    }

    private static File screenshotElement(Locator locator, Path path) {
        locator.screenshot(new Locator.ScreenshotOptions()
                .setPath(path));
        return path.toFile();
    }
}
