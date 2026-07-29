package io.github.baiyibs.jwxt.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.impl.TerminalGraphicsManager;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class ConsoleTerminal {
    @Getter
    private static Terminal terminal;
    private static BufferedReader reader;

    static {
        try {
            terminal = TerminalBuilder.terminal();
            reader = new BufferedReader(new InputStreamReader(terminal.input()));
            // 注册钩子
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (terminal != null) {
                        terminal.close();
                    }
                } catch (IOException e) {
                    log.error("关闭 Terminal 失败: {}", e.getMessage());
                }
            }));
        } catch (IOException e) {
            log.error("初始化 Terminal 失败!");
            terminal = null;
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
    }

    // 禁止实例化
    private ConsoleTerminal() {}

    /**
     * 判断 Terminal 是否可用
     * @return 可用状态
     */
    public static boolean isAvailable() {
        return terminal != null;
    }

    /**
     * 显示图片
     * @param imageFile 图片文件
     */
    public static void displayImage(File imageFile) {
        if (isAvailable() && TerminalGraphicsManager.isGraphicsSupported(terminal)) {
            try {
                TerminalGraphicsManager.displayImage(terminal, imageFile);
                return;
            } catch (IOException e) {
                log.error("使用终端显示图片失败: {}", e.getMessage());
            }
        }

        try {
            Desktop.getDesktop().open(imageFile);
        } catch (IOException e) {
            log.error("打开图片失败: {}", e.getMessage());
        }
    }

    /**
     * 读取一行输入
     * @param prompt 提示词
     * @return 输入的字符串
     * @throws IOException 如果发生 I/O 错误
     */
    public static String readLine(String prompt) throws IOException {
        System.out.print(prompt);
        System.out.flush();

        if (isAvailable()) {
            return reader.readLine();
        } else {
            return new BufferedReader(new InputStreamReader(System.in)).readLine();
        }
    }

    public static void close() {
        if (terminal != null) {
            try {
                terminal.close();
            } catch (IOException e) {
                log.error("关闭 Terminal 失败", e);
            }
        }
    }

}
