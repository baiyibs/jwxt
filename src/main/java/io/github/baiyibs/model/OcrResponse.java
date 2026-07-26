package io.github.baiyibs.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OcrResponse {
    private int code;
    private String message;
    private String data;
}
