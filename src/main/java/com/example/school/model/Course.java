package com.example.school.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    @NotEmpty(message = "Пожалуйста, заполните поле")
    @Size(max = 255, message = "Название курса должно быть до 255 символов")
    private String name;
    @Column
    @NotEmpty(message = "Пожалуйста, заполните поле")
    @Size(max = 5, message = "Тег должен быть уникальным, до 5 символов")
    private String tag;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private List<Module> modules = new ArrayList<Module>();

    @ManyToMany
    @JoinTable(
            name = "user_courses",
            joinColumns = { @JoinColumn(name = "course_id") },
            inverseJoinColumns = { @JoinColumn(name = "student_id") }
    )
    private Set<User> students = new HashSet<>();

    public Course() {
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag.toUpperCase();
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModule(Module module) {
        this.modules.add(module);
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public Set<User> getStudents() {
        return students;
    }

    public void setStudents(Set<User> students) {
        this.students = students;
    }

}
