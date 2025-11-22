package com.example.wordia;

public class User {

    public String email;
    public String password;
    public String nickname;

    // Firebase가 필요로 하는 기본 생성자
    public User() { }

    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
