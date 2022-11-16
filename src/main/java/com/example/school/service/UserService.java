package com.example.school.service; //Необходимые импорты

import com.example.school.actions.CourseAction;
import com.example.school.actions.UserAction;
import com.example.school.model.Course;
import com.example.school.model.Role;
import com.example.school.model.User;
import com.example.school.model.UserInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserAction userAction; //Используемые инжекты
    private final CourseAction courseAction;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserAction userAction, CourseAction courseAction, PasswordEncoder passwordEncoder) {
        this.userAction = userAction;
        this.courseAction = courseAction;
        this.passwordEncoder = passwordEncoder;
    }

    @Override //Поиск пользователя по логину
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAction.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userAction.findAll();
    } //список со всеми пользователями

    public List<Course> findAllCourses() {
        return courseAction.findAll();
    } //список со всеми курсами

    public boolean addUser(User user, UserInfo userInfo){ //создание пользователя и проверка наличия такого логина в БД
        User userFromDb = userAction.findByUsername(user.getUsername());
        if (userFromDb != null){
            return false;
        }
        else {
            user.setActive(true);
            user.setRoles(Collections.singleton(Role.ROLE_STUDENT));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUserInfo(userInfo);
            userAction.save(user);
            return true;
        }
    }

    public void saveUser(User user, String username, Map<String, String> form) { //изменение ролей пользователя
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
            }
        userAction.save(user);
        }


    public void updateProfile(User user,            //Обновление личных данных в личном кабинете
                              String email,
                              String firstName,
                              String secondName,
                              String surname,
                              String phoneNumber,
                              String birthDate) {

        String userEmail = user.getUserInfo().getEmail();
        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged){
            user.getUserInfo().setEmail(email);
        }

        user.getUserInfo().setFirst_name(firstName);
        user.getUserInfo().setSecond_name(secondName);
        user.getUserInfo().setSurname(surname);
        user.getUserInfo().setPhone_number(phoneNumber);
        user.getUserInfo().setBirth_date(birthDate);
        userAction.save(user);
    }

    public void saveSubscriptions(User user, Map<String, Course> form) { //организация доступа на курсы пользователю
        Map<String, Course> courseMap = findAllCourses().
                stream().
                collect(Collectors.toMap(Course::getTag, Function.identity()));
        Set<Course> courseSet = new HashSet<>();
        user.getSubscriptions().clear();
        for (String key : form.keySet()) {
            if (courseMap.containsKey(key)) {
                courseSet.add(courseMap.get(key));
            }
        }

        user.setSubscriptions(courseSet);
        userAction.save(user);
    }

}