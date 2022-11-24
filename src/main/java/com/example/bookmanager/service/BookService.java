package com.example.bookmanager.service;

import com.example.bookmanager.entity.Book;
import com.example.bookmanager.entity.Borrow;
import com.example.bookmanager.entity.BorrowDetail;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BookService {
    List<Book> getAllBook();
    PageInfo<Book> getPage(int pageNum);
    void deleteBook(int bid);
    void addBook(String title, String desc, double price);
    void borrowBook(int bid,int sid);
    PageInfo<Book> getBorrowedBookListById(int pageNum,int id);
    PageInfo<Book> getAllBookWithoutBorrow(int PageNum);
    PageInfo<BorrowDetail> getBorrowDetailList(int pageNum);
}
