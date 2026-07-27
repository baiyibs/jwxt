package io.github.baiyibs.utli;

import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.impl.TerminalGraphicsManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Slf4j
public class ImageViewer {

    /**
     * 根据终端支持情况显示图片
     * @param imageFile 图片文件
     */
    public static void show(File imageFile) {
        try (Terminal terminal  = TerminalBuilder.terminal()) {
            if (TerminalGraphicsManager.isGraphicsSupported(terminal)) {
                TerminalGraphicsManager.displayImage(terminal, imageFile);
            } else {
                Desktop.getDesktop().open(imageFile);
            }
        } catch (IOException e) {
            log.error("打开图片失败: {}", e.getMessage());
        }
    }
}
