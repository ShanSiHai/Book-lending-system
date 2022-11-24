package com.example.bookmanager.controller.api;

import com.example.bookmanager.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/admin")
public class AdminApiController {
    @Autowired
    BookService service;
    @RequestMapping(value = "/del-book",method = RequestMethod.GET)
    public String delBook(@RequestParam("bid") int bid,@RequestParam("pageNum") int pageNum){    //加了pageNum参数，删除书记之后也能留在圆分页也页面
        service.deleteBook(bid);
        return "redirect:/page/admin/book?pageNum="+pageNum;
    }
    @RequestMapping("/add-book")
    public String addBook(@RequestParam("title") String title,
                          @RequestParam("desc") String desc,
                          @RequestParam("price") double price){
        service.addBook(title,desc,price);
        return "redirect:/page/admin/book";
    }
}
