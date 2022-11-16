package com.example.school.actions;

import com.example.school.model.Lesson;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface LessonAction extends CrudRepository<Lesson, Long> {
    List<Lesson> findLessonsByModuleId(Long id);
}
