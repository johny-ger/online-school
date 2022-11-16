package com.example.school.actions;

import com.example.school.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAction extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
