package com.example.school.actions;

import com.example.school.model.Course;
import org.springframework.data.repository.CrudRepository;


public interface UserInfoAction extends CrudRepository<Course, Long> {

}
