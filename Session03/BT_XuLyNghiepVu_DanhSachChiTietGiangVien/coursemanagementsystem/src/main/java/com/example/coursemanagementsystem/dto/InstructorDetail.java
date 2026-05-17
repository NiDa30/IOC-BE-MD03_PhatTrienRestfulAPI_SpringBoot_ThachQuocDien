package com.example.coursemanagementsystem.dto;

import com.example.coursemanagementsystem.model.Course;
import java.util.List;

public class InstructorDetail {
    private Long id;
    private String name;
    private String email;
    private List<Course> courses;

    public InstructorDetail() {}

    public InstructorDetail(Long id, String name, String email, List<Course> courses) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.courses = courses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
