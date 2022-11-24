package com.example.bookmanager.service.impl;

import com.example.bookmanager.entity.AuthUser;
import com.example.bookmanager.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService implements UserDetailsService {
    @Autowired
    UserMapper mapper;

    /*@Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String password = mapper.getPasswordByUsername(s);  //从数据库根据用户名获取密码
        if(password == null)
            throw new UsernameNotFoundException("登录失败，用户名或密码错误！");
        return User   //这里需要返回UserDetails，SpringSecurity会根据给定的信息进行比对
                .withUsername(s)
                .password(password)   //直接从数据库取的密码
                .roles("user")   //用户角色
                .build();
    }*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = mapper.getPasswordByUsername(username);  //从数据库根据用户名获取密码
        if(user == null)
            throw new UsernameNotFoundException("登录失败，用户名或密码错误！");
        return User   //这里需要返回UserDetails，SpringSecurity会根据给定的信息进行比对
                .withUsername(user.getName())
                .password(user.getPassword())   //直接从数据库取的密码
                .roles(user.getRole())   //用户角色
                //.authorities("page:index","page:admin")         //也可以不用角色，直接设置权限，限制用户只能登陆这些页面
                .build();
    }

}
