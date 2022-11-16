package com.example.school.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_STUDENT ("Student"),
    ROLE_TEACHER ("Teacher"),
    ROLE_ADMIN ("Admin");

    private final String name;

    private Role(String s) {
        name = s;
    }

    @Override
    public String getAuthority() {
        return name();
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}