package com.example.bookmanager.service.impl;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SimpleService {

    @PreAuthorize("hasAnyRole('admin')")      //@PreAuthorize可以用在任何方法上
    public void test() {
        System.out.println("我是测试");
    }

    //我们还可以使用@PreFilter和@PostFilter对集合类型的参数或返回值进行过滤
    @PreFilter("filterObject.equals('abc')")    //list里满足条件的元素留下
    public void test1(List<String> list) {
        System.out.println("我是业务" + list);
    }

    @PostFilter("filterObject.equals('abc')")     //list里满足条件的元素才会被返回
    public List<String> test2() {
        List<String> list = new LinkedList<>();
        list.add("abc");
        list.add("aaa");
        return list;
    }
    /*当有多个集合时，需要使用filterTarget进行指定
    @PreFilter(value = "filterObject.equals('lbwnb')", filterTarget = "list2")
    public void test(List<String> list, List<String> list2){
        System.out.println("成功执行"+list);
    }*/
}
