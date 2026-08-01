package io.github.baiyibs.jwxt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Transcript {
    private Student student;
    private List<Course> courseList;

    public int getCourseCount() {
        return courseList == null ? 0 : courseList.size();
    }

    public Double getTotalCredit() {
        if (courseList == null || courseList.isEmpty()) return 0.0;
        return courseList.stream().mapToDouble(Course::getCredit).sum();
    }

    public Double getAvgScore() {
        if (courseList == null || courseList.isEmpty()) return 0.0;
        return courseList.stream().mapToDouble(Course::getScore).average().orElse(0);
    }

    public Double getAvgGPA() {
        if (courseList == null || courseList.isEmpty()) return 0.0;
        return courseList.stream().mapToDouble(Course::getGpa).average().orElse(0);
    }
}
