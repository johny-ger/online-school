package com.example.school.controllers;

import com.example.school.actions.CourseAction;
import com.example.school.actions.LessonAction;
import com.example.school.actions.ModuleAction;
import com.example.school.model.Course;
import com.example.school.model.Lesson;
import com.example.school.model.Module;
import com.example.school.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CourseController {
    final
    CourseAction courseAction;
    final
    ModuleAction moduleAction;
    final
    LessonAction lessonAction;

    public CourseController(CourseAction courseAction, ModuleAction moduleAction, LessonAction lessonAction) {
        this.courseAction = courseAction;
        this.moduleAction = moduleAction;
        this.lessonAction = lessonAction;
    }

    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") // Переход на страницу курсов
    @GetMapping("/course")
    public String course(Model model) {
        model.addAttribute("courses", getCourses());
        model.addAttribute("course", new Course());
        return "course";
    }

    private Iterable<Course> getCourses() {
        Iterable<Course> courses = courseAction.findAll();
        return courses;
    }

    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") // Создание курса
    @PostMapping("/course")
    public String add(@Valid Course newCourse,
                      BindingResult bindingResult,
                      Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", getCourses());
            return "course";
        }
        else {
            courseAction.save(newCourse);
        }
        return "redirect:/course";
    }


    private List<Module> getCourseModules(@PathVariable Course course) {
        return moduleAction.findModulesByCourseId(course.getId());
    }

    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") // отображение данных выбранного курса
    @GetMapping("/course/{course}")
    public String showCourse(@PathVariable Course course, Model model) {
        model.addAttribute("course", course);
        model.addAttribute("modules", getCourseModules(course));
        Module module = new Module();
        model.addAttribute("module", module);
        return "module";
    }



    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") //Сохранение измененных данных курса
    @PostMapping("/saveCourse")
    public String saveCourse(@RequestParam("id") Course course,
                             @RequestParam String courseName,
                             @RequestParam String tag) {
            course.setName(courseName);
            course.setTag(tag);
            courseAction.save(course);
            return "redirect:/course/"
                    + course.getId();
  //      }
    }

    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") // удаление курса
    @PostMapping("/deleteCourse")
    public String deleteCourse(@RequestParam("id") Course course) {
        courseAction.delete(course);
        return "redirect:/course";
    }

    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") // добавление модуля
    @PostMapping("/course/{course}")
    public String addModule(@PathVariable Course course,
                            @Valid Module module,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("modules", getCourseModules(course));
            model.addAttribute("course", course);
            return "module";
        }
        else {
            course.setModule(module);
            moduleAction.save(module);
            List<Module> modules = moduleAction.findModulesByCourseId(course.getId());
            model.addAttribute("modules", modules);
            return "redirect:/course/"
                    + course.getId();
        }
    }

    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") // удаление модуля
    @PostMapping("/deleteModule")
    public String deleteModule(@RequestParam("id") Module module) {
        moduleAction.delete(module);
        return "redirect:/course/"
                + module.getCourse().getId();
    }

    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") // отображение данных модуля
    @GetMapping("/course/{course}/{module}")
    public String showModule(@PathVariable Course course,
                             @PathVariable Module module,
                             Model model) {
        model.addAttribute("module", module);
        model.addAttribute("lessons", lessonAction.findLessonsByModuleId(module.getId()));
        Lesson lesson = new Lesson();
        model.addAttribute("lesson", lesson);
        return "lesson";
    }

    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") // сохранение измененных данных модуля
    @PostMapping("saveModule")
    public String SaveModule(@RequestParam("id") Module module,
                             @RequestParam String name) {
        module.setName(name);
        moduleAction.save(module);
        return "redirect:/course/"
                + module.getCourse().getId()
                + "/"
                + module.getId();
    }


    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") // добавление урока к модулю
    @PostMapping("/course/{course}/{module}")
    public String addLesson(@PathVariable Course course,
                            @PathVariable Module module,
                            @Valid Lesson lesson,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("module", module);
            model.addAttribute("lessons", lessonAction.findLessonsByModuleId(module.getId()));
            return "lesson";
        }
        else {
            module.setLesson(lesson);
            lessonAction.save(lesson);
            return "redirect:/course/"
                    + lesson.getModule().getCourse().getId()
                    + "/"
                    + lesson.getModule().getId();
        }
    }

    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") // удаление урока
    @PostMapping("/deleteLesson")
    public String deleteLesson(@RequestParam("id") Lesson lesson){
        lessonAction.delete(lesson);
        return "redirect:/course/"
                + lesson.getModule().getCourse().getId()
                + "/"
                + lesson.getModule().getId();
    }


    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") // отображение данных урока
    @GetMapping("/course/{course}/{module}/{lesson}")
    public String showLesson(@PathVariable Course course,
                             @PathVariable Module module,
                             @PathVariable Lesson lesson,
                             Model model) {
        model.addAttribute("crs", course);
        model.addAttribute("md", module);
        model.addAttribute("lssn", lesson);
        return "aboutLesson";
    }

    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_TEACHER)") // сохранение данных урока
    @PostMapping("/saveLesson")
    public String saveLesson(@RequestParam("id") Lesson lesson,
                             @RequestParam String name,
                             @RequestParam String lesson_header,
                             @RequestParam String lesson_text,
                             @RequestParam("link") String lesson_video) {
        lesson.setName(name);
        if (lesson_header.isEmpty()) {
            lesson.setLesson_header(null);
        } else {
            lesson.setLesson_header(lesson_header);
        }
        if (lesson_text.isEmpty()) {
            lesson.setLesson_text(null);
        } else {
            lesson.setLesson_text(lesson_text);
        }
        if (lesson_video.isEmpty()) {
            lesson.setLesson_video(null);
        } else {
            lesson.setLesson_video(lesson_video);
        }
        lessonAction.save(lesson);
        return "redirect:/course/" + lesson.getModule().getCourse().getId()
                + "/"
                + lesson.getModule().getId();
    }

    @GetMapping("/course/view/{course}") // отображение всего выбранного курса
    public String viewCourse(@PathVariable Course course,
                             @AuthenticationPrincipal User user,
                             Model model) {
        model.addAttribute("cs", course);
        List<Long> CoursesIDs = user
                .getSubscriptions()
                .stream()
                .collect(Collectors.toList())
                .stream()
                .map(Course::getId)
                .collect(Collectors.toList());
        if (CoursesIDs.contains(course.getId())) {
            model.addAttribute("crs", course);
            return "courseview";
        } else {
            return "errorpage";
        }
    }
}
