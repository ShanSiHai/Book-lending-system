package com.example.bookmanager.controller.page;

import com.example.bookmanager.entity.AuthUser;
import com.example.bookmanager.entity.Book;
import com.example.bookmanager.mapper.BookMapper;
import com.example.bookmanager.mapper.UserMapper;
import com.example.bookmanager.service.impl.SimpleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/page/auth")    //这个控制器只负责登录与注册这些公共页面
public class AuthPageController {
    @Autowired
    UserMapper mapper;

    @Autowired
    SimpleService simpleService;

    /*管理员的主页与普通用户的主页分开以后就不需要在这里面设置/index请求了，另新建UserPageController与AdminPageController两个控制器分别管理管理员的页面与普通用户的页面
    @PreAuthorize("hasAnyRole('user','admin')")     //user和admin都可以访问
    //@PostAuthorize("hasAnyRole('admin')")    //先执行方法再进行权限判断
    @RequestMapping("/index")
    public String index(@SessionAttribute("user") AuthUser authUser,Model model*//*@SessionAttribute("SPRING_SECURITY_CONTEXT") SecurityContext context*//*){
        //simpleService.test();     //页面会被禁止，因为test方法只允许admin访问
        List<String> list=new LinkedList<>();
        list.add("abc");
        list.add("aaa");
        simpleService.test1(list);

        //用户登录之后，想要获取当前已经登录用户的信息要通过使用SecurityContextHolder得到SecurityContext对象，直接使用SecurityContext对象来获取当前的认证信息
        SecurityContext context= SecurityContextHolder.getContext();   //也可以不用SecurityContextHolder，直接从session获取，在方法参数中注入
        Authentication authentication=context.getAuthentication();
        //Authentication可以直接拿到授权信息，通过源码可以看到其是继承自Principal类，而Principal类是java.security包的，可见其是最高级的身份认证
        User user=(User) authentication.getPrincipal();     //这里的User实际上就是UserAuthService里的loadUserByUsername方法返回的User
        System.out.println(user.getUsername());
        System.out.println(user.getAuthorities());      //输出[ROLE_user]，表示角色是user

        model.addAttribute("user",authUser);
        return "admin/index";
    }*/

    /*@RequestMapping("/index")
    public String index(@SessionAttribute("user") AuthUser authUser,Model model){
        SecurityContext context=SecurityContextHolder.getContext();
        Authentication authentication=context.getAuthentication();
        User user=(User) authentication.getPrincipal();
        System.out.println(authentication.getDetails());
        return "index";
    }*/

    /*//一开始从session里取到用户信息展示在主页，可以这样实现，也可以在SecurityConfiguration里.rememberMe()下面添加authenticationSuccessHandler
    @RequestMapping("/index")      //tomcat里设置了服务器会先走这个页面
    public String index(HttpSession session, Model model){
        AuthUser user=(AuthUser) session.getAttribute("user");
        if(user==null){
            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            user=mapper.getPasswordByUsername(authentication.getName());
            session.setAttribute("user",user);
        }
        model.addAttribute("user",user);
        return "index";
    }*/

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/home")
    @PreAuthorize("hasAnyRole('admin','user')")
    public String home(){
        System.out.println(simpleService.test2());
        return "admin/index";
    }
}
