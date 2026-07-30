package io.github.baiyibs.jwxt.util;

import cn.hutool.core.text.StrSplitter;
import io.github.baiyibs.jwxt.model.Course;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CourseParser {
    public static List<Course> parseFromText(String text) {
        List<Course> courseList = new ArrayList<>();

        for (String line: text.lines().skip(1).collect(Collectors.toList())) {
            List<String> properties = StrSplitter.split(line, "\t", 0, false, false);
            if (!properties.isEmpty()) {
                properties.remove(0);
            }
            Course course = Course.fromList(properties);
            if (course != null) {
                courseList.add(course);
            }
        }
        return courseList;
    }
}
