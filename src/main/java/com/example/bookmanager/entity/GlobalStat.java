package com.example.bookmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor   //生成构造函数
public class GlobalStat {
    int userCount;
    int borrowCount;
    int bookCount;
}
