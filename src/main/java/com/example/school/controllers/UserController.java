package com.example.school.controllers;

import com.example.school.model.Course;
import com.example.school.model.Role;
import com.example.school.model.User;
import com.example.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_ADMIN)")
    @GetMapping
    public String userList(Model model) {
        Iterable<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "userList";
    }
      @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_ADMIN)")
      @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("usr", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("subscriptions", userService.findAllCourses());
        return "userEdit";
    }
    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_ADMIN)")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("id") User user
    ) {
        userService.saveUser(user, username, form);
        return "redirect:/user";
    }

    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_ADMIN)")
    @GetMapping("/subs/{user}")
    public String userEditSubscriptions(@PathVariable User user, Model model){
        model.addAttribute("usr", user);
        model.addAttribute("subscriptions", userService.findAllCourses());
        return "subscriptions";
    }
    @PreAuthorize("hasRole(T(com.example.school.model.Role).ROLE_ADMIN)")
    @PostMapping("/subs")
    public String userSaveSubscriptions(
            @RequestParam Map<String, Course> form,
            @RequestParam("id") User user
    ) {
        userService.saveSubscriptions(user, form);
        return "redirect:/user";
    }


    @GetMapping("profile")
    public String getProfile(Model model,
                             @AuthenticationPrincipal User user) {
        model.addAttribute("startDate", "1900-01-01");
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("usr", user);
        return "profile";
    }
    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
       //     @RequestParam String password,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String firstName,
            @RequestParam String surname,
            @RequestParam String secondName,
            @RequestParam String birthDate
    ){
        userService.updateProfile(user,
       //         password,
                email,
                firstName,
                secondName,
                surname,
                phoneNumber,
                birthDate);
        return "redirect:/user/profile";
    }

}
