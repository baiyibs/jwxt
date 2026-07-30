package io.github.baiyibs.jwxt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    String semester;                    // 开课学期
    String courseCode;                  // 课程编号
    String courseName;                  // 课程名称
    String groupName;                   // 分组名
    double score;                       // 成绩
    String gradeMark;                   // 成绩标识
    double credit;                      // 学分
    int totalHours;                     // 总学时
    double gpa;                         // 绩点
    String retakeSemester;              // 补重学期
    String examMethod;                  // 考核方式
    String examNature;                  // 考试性质
    String courseAttribute;             // 课程属性
    String courseType;                  // 课程性质
    String generalEducationCategory;    // 通选课类别

    public static Course fromList(List<String> list) {
        if (list.size() != 15) {
            return null;
        }
        return new Course(
                list.get(0),                           
                list.get(1),                           
                list.get(2),                           
                list.get(3),                           
                Double.parseDouble(list.get(4)),       
                list.get(5),                           
                Double.parseDouble(list.get(6)),       
                Integer.parseInt(list.get(7)),         
                Double.parseDouble(list.get(8)),       
                list.get(9),                           
                list.get(10),                          
                list.get(11),                          
                list.get(12),                          
                list.get(13),                          
                list.get(14)                           
        );
    }
}
