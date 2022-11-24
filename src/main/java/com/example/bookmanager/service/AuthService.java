package com.example.bookmanager.service;

import com.example.bookmanager.entity.AuthUser;

import javax.servlet.http.HttpSession;

public interface AuthService {
    void register(String name,String sex,String grade,String password);   //先参数太多也可以定义一个实体类
    AuthUser findUser(HttpSession session);
}
