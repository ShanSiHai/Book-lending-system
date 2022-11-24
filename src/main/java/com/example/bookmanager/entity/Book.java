package com.example.bookmanager.entity;

import lombok.Data;

@Data
public class Book {
    int bid;
    String title;
    String desc;
    Double price;
}
