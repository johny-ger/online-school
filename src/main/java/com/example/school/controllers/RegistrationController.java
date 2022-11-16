package com.example.school.controllers;


import com.example.school.model.User;
import com.example.school.model.UserInfo;
import com.example.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;


    @GetMapping(path="/registration")
    public String registration(Model model){
        model.addAttribute("startDate", "1900-01-01");
        model.addAttribute("localDate", LocalDate.now());
        return "registration";
    }

    @PostMapping(path = "/registration")
    public String newUser(User user,
                          UserInfo userInfo,
                          Model model) {
        if (userService.addUser(user, userInfo)) {
            return "redirect:/admin";
        }
        else {
            model.addAttribute("message", "User exists!");
            return "/registration";
        }

    }
}
