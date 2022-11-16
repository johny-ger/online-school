package com.example.school.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;
    @Column
    private String first_name;
    @Column
    private String second_name;
    @Column
    private String surname;
    @Column
    private String phone_number;
    @Column

   // @Email(message = "Почта введена некорректно.")
    private String email;
    @Column
    private String birth_date;
    @OneToOne(mappedBy = "userInfo", cascade = CascadeType.ALL)
    private User user;

    public UserInfo() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }


}