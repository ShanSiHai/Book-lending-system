package com.example.bookmanager.controller.api;

import com.example.bookmanager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/auth")
public class AuthApiController {
    @Autowired
    AuthService service;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(@RequestParam("name") String name,@RequestParam("sex") String sex,@RequestParam("grade") String grade,@RequestParam("password") String password){
        service.register(name,sex,grade,password);
        return "redirect:/login";     //这里改成重定向，不然会访问/api/auth/login，而不是/login
    }

}
