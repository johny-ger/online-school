package com.example.school.actions;

import com.example.school.model.Module;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ModuleAction extends CrudRepository<Module, Long> {
    List<Module> findModulesByCourseId(Long Id);
}
