package io.github.baiyibs.jwxt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Student {
    private String name;
    private String id;
    private String department;
    private String major;
    private String className;
}
