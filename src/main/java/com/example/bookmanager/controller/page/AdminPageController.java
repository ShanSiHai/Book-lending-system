package com.example.bookmanager.controller.page;

import com.example.bookmanager.entity.AuthUser;
import com.example.bookmanager.service.AuthService;
import com.example.bookmanager.service.BookService;
import com.example.bookmanager.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@RequestMapping("/page/admin")
@Controller
public class AdminPageController {
    @Autowired
    AuthService service;
    @Autowired
    BookService bookService;
    @Autowired
    StatService statService;

    @RequestMapping("/index")
    public String index(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, HttpSession session, Model model){
        AuthUser user=service.findUser(session);
        model.addAttribute("user",user);
        model.addAttribute("pageInfo",bookService.getBorrowDetailList(pageNum));
        model.addAttribute("globalStat",statService.getGlobalStat());
        return "admin/index";
    }
    @RequestMapping("/book")
    public String book(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, HttpSession session, Model model){
        model.addAttribute("user",service.findUser(session));
        model.addAttribute("pageInfo",bookService.getPage(pageNum));
        return "admin/book";
    }
    @RequestMapping("/addBookPage")
    public String addBookPage(HttpSession session, Model model){
        model.addAttribute("user",service.findUser(session));
        return "admin/add-book";
    }
}
