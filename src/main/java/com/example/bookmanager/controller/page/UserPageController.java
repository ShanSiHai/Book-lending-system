package com.example.bookmanager.controller.page;

import com.example.bookmanager.entity.AuthUser;
import com.example.bookmanager.service.AuthService;
import com.example.bookmanager.service.BookService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@RequestMapping("/page/user")
@Controller
public class UserPageController {
    @Autowired
    AuthService service;
    @Autowired
    BookService bookService;

    @RequestMapping("/index")
    public String index(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, HttpSession session, Model model){
        model.addAttribute("user",service.findUser(session));
        if(pageNum<1) pageNum=1;
        model.addAttribute("pageInfo",bookService.getAllBookWithoutBorrow(pageNum));
        return "user/index";
    }
    @RequestMapping("/book")
    public String book(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, HttpSession session, Model model){
        AuthUser user=service.findUser(session);
        model.addAttribute("user",user);
        model.addAttribute("pageInfo",bookService.getBorrowedBookListById(pageNum,user.getId()));
        return "user/book";
    }
}
