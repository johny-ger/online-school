package com.example.school.actions;

import com.example.school.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseAction extends JpaRepository<Course, Long> {

    List<Course> findByTag(String Tag);

}
