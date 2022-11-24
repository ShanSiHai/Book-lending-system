package com.example.bookmanager.mapper;

import com.example.bookmanager.entity.Book;
import com.example.bookmanager.entity.Borrow;
import com.example.bookmanager.entity.BorrowDetail;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookMapper {
    @Select("select * from book")
    List<Book>  allBook();

    @Select("select * from book where bid=#{bid}")
    Book getBookByBid(int bid);

    @Delete("delete from book where bid=#{bid}")
    void deleteBook(int bid);

    @Insert("insert into book(title,`desc`,price) values(#{title},#{desc},#{price})")
    void addBook(@Param("title") String title, @Param("desc") String desc, @Param("price") double price);

    @Insert("insert into borrow(bid,sid,time) values(#{bid},#{sid},NOW())")
    void borrowBook(@Param("bid") int bid,@Param("sid") int sid);

    @Select("select * from book where book.bid not in (select borrow.bid from borrow)")
    List<Book> getAllBookWithoutBorrow();

    @Select("select * from borrow where sid=#{sid}")
    List<Borrow> getBorrowListBySid(int sid);

    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "title",property = "book_title"),
            @Result(column = "name",property = "user_name"),
            @Result(column = "time",property = "time")
    })
    @Select("select * from book INNER JOIN borrow on book.bid=borrow.bid " +
            "INNER JOIN student on student.sid=borrow.sid")
    List<BorrowDetail> getBorrowDetailList();

    @Select("select count(*) from book")
    int getBookCount();

    @Select("select count(*) from borrow")
    int getBorrowCount();
}