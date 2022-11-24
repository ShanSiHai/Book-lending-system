package com.example.bookmanager.service.impl;

import com.example.bookmanager.entity.GlobalStat;
import com.example.bookmanager.mapper.BookMapper;
import com.example.bookmanager.mapper.UserMapper;
import com.example.bookmanager.service.BookService;
import com.example.bookmanager.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatServiceImpl implements StatService {
    @Autowired
    BookMapper bookMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public GlobalStat getGlobalStat() {
        return new GlobalStat(userMapper.getUserCount(),bookMapper.getBorrowCount(),bookMapper.getBookCount());
    }
}
