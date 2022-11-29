package com.example.bookmanager.controller.api;

import com.example.bookmanager.entity.AuthUser;
import com.example.bookmanager.service.AuthService;
import com.example.bookmanager.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/api/user")
public class UserApiController {
    @Autowired
    BookService bookService;

    @RequestMapping(value = "/borrow-book",method = RequestMethod.GET)
    public String borrowBook(@RequestParam("bid") int bid, @SessionAttribute("user") AuthUser user){
        bookService.borrowBook(bid, user.getId());
        return "redirect:/page/user/book";
    }

    @RequestMapping("/book-return")
    public String bookReturn(@RequestParam("bid") int bid){
        bookService.bookReturn(bid);
        return "redirect:/page/user/book";
    }
}
