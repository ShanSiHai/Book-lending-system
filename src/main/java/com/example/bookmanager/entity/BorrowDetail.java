package com.example.bookmanager.entity;

import lombok.Data;

@Data
public class BorrowDetail {
    int id;
    String book_title;
    String user_name;
    String time;
}
