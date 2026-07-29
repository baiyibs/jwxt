package io.github.baiyibs.jwxt.util;

import io.github.baiyibs.jwxt.model.Student;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class StudentParser {
    public static Student parseFromText(String text) {
        Student student = new Student();
        Map<String, String> map = text.lines()
                .map(line -> line.split("\\s*[：:]\\s*"))
                .filter(arr -> arr.length == 2)
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
        map.forEach((key, value) -> {
            switch (key) {
                case "学生姓名":
                    student.setName(value);
                    break;
                case "学生编号":
                    student.setId(value);
                    break;
                case "所属院系":
                    student.setDepartment(value);
                    break;
                case "专业名称":
                    student.setMajor(value);
                    break;
                case "班级名称":
                    student.setClassName(value);
                    break;
            }
        });
        return student;
    }
}
