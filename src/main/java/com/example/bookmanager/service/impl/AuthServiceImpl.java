package com.example.bookmanager.service.impl;

import com.example.bookmanager.entity.AuthUser;
import com.example.bookmanager.mapper.UserMapper;
import com.example.bookmanager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.ByteArrayPropertyEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpSession;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserMapper mapper;

    @Transactional    //开启事务，在用户输入信息不合法之后回滚，不会产生脏数据
    @Override
    public void register(String name, String sex, String grade, String password) {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        AuthUser user=new AuthUser(0,name,encoder.encode(password),"user");
        if(mapper.registerUser(user)<=0)
            throw new RuntimeException("用户基本信息添加失败");
        if (mapper.addStudentInfo(user.getId(),name,sex,grade)<=0)
            throw new RuntimeException("学生详细信息插入失败");
    }

    public AuthUser findUser(HttpSession session){
        AuthUser user=(AuthUser) session.getAttribute("user");
        if(user==null){
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            user=mapper.getPasswordByUsername(authentication.getName());
            session.setAttribute("user",user);
        }
        return user;
    }
}
