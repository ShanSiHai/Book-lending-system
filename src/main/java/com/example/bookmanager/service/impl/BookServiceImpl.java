package com.example.bookmanager.service.impl;

import com.example.bookmanager.entity.Book;
import com.example.bookmanager.entity.Borrow;
import com.example.bookmanager.entity.BorrowDetail;
import com.example.bookmanager.mapper.BookMapper;
import com.example.bookmanager.mapper.UserMapper;
import com.example.bookmanager.service.BookService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookMapper mapper;
    @Autowired
    UserMapper userMapper;

    int pageSize=4;
    @Override
    public List<Book> getAllBook() {
        return mapper.allBook();
    }

    public PageInfo<Book> getPage(int pageNum){
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<Book> page=new PageInfo<>(mapper.allBook());
        return page;
    }

    @Override
    public void deleteBook(int bid) {
        mapper.deleteBook(bid);
    }

    @Override
    public void addBook(String title, String desc, double price) {
        mapper.addBook(title,desc,price);
    }

    @Override
    public void borrowBook(int bid, int id) {
        Integer sid=userMapper.getSidByUserId(id);
        if(sid==null) return;    //int不可以判空，integer可以
        mapper.borrowBook(bid,sid);
    }

    @Override
    public PageInfo<Book> getAllBookWithoutBorrow(int pageNum) {       //被借过的书就不显示了
        //第一种取法，先取到所有书，再取到已被借的书，将所有书剔除已经被借的书
        //但不知道为什么，分页搞不好
        /*List<Book> bookList=mapper.allBook();
        List<Integer> borrows=mapper.getBorrowList()   //所有已经被借的书
                .stream()
                .map(Borrow->Borrow.getBid())    //只留下bid
                .collect(Collectors.toList());
        PageHelper.startPage(pageNum,pageSize);   //它和下面的一句必须挨着
        PageInfo<Book> page=new PageInfo<>(bookList
                .stream()
                .filter(Book->!borrows.contains(Book.getBid()))   //borrows里不包含的留下
                .collect(Collectors.toList()));*/

        //另一种取法，直接用sql语句查询
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<Book> page=new PageInfo<>(mapper.getAllBookWithoutBorrow());
        return page;
    }

    @Override
    public PageInfo<Book> getBorrowedBookListById(@RequestParam("pageNum")int pageNum, int id) {
        Integer sid=userMapper.getSidByUserId(id);
        if(sid==null) return null;    //int不可以判空，integer可以
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(mapper.getBorrowListBySid(sid)   //得到List<Borrow>，也就是用户的借阅列表
                .stream()
                .map(Borrow->mapper.getBookByBid(Borrow.getBid()))    //将每一个Borrow替换成对应的Book
                .collect(Collectors.toList()));       //成了List<Book>，也就是用户借的书
    }

    @Override
    public PageInfo<BorrowDetail> getBorrowDetailList(@RequestParam("pageNum")int pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<BorrowDetail> page=new PageInfo<>(mapper.getBorrowDetailList());
        return page;
    }
}
