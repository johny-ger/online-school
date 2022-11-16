package com.example.school.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    @NotEmpty(message = "Пожалуйста, заполните поле")
    private String name;
    @ManyToOne(cascade = {  CascadeType.DETACH,
                            CascadeType.MERGE,
                            CascadeType.PERSIST,
                            CascadeType.REFRESH})
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "module")
    private List<Lesson> lessons = new ArrayList<Lesson>();

    public Module() {
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLesson(Lesson lesson) {
        this.lessons.add(lesson);
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}


