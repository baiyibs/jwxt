package io.github.baiyibs.jwxt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OcrResponse {
    private int code;
    private String message;
    private String data;
}
