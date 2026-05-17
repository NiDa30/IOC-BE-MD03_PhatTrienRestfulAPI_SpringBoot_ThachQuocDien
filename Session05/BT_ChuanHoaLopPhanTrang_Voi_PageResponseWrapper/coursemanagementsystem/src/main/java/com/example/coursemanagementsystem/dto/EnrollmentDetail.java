package com.example.coursemanagementsystem.dto;

import com.example.coursemanagementsystem.model.Course;

public class EnrollmentDetail {
    private long id;
    private String studentName;
    private Course course;

    public EnrollmentDetail() {}

    public EnrollmentDetail(long id, String studentName, Course course) {
        this.id = id;
        this.studentName = studentName;
        this.course = course;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
