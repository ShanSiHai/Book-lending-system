package com.example.bookmanager.mapper;

import com.example.bookmanager.entity.AuthUser;
import org.apache.ibatis.annotations.*;
import org.springframework.security.access.prepost.PreAuthorize;

@Mapper
public interface UserMapper {
    @Select("select * from users where name=#{username}")
    AuthUser getPasswordByUsername(String username);

    @Insert("insert into users(name,role,password) values(#{name},#{role},#{password})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")   //设置主键，自动将主键id的自增值给到User
    int registerUser(/*@Param("name") String name,@Param("role") String role,@Param("password") String password*/
    AuthUser user);

    @Insert("insert into student(uid,name,sex,grade) values(#{uid},#{name},#{sex},#{grade})")
    int addStudentInfo(@Param("uid") int uid,@Param("name") String name,@Param("sex") String sex,@Param("grade") String grade);

    /*users表里面没有sid，只有id，想要获取当前用户的学号需要查询*/
    @Select("select sid from student where uid=#{id}")
    Integer getSidByUserId(int id);
    @Select("select count(*) from users")
    int getUserCount();
}
