package io.github.baiyibs.jwxt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OcrResult {
    private int code;
    private String message;
    private String data;
}
