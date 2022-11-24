package com.example.bookmanager.config;

import com.github.pagehelper.PageInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScans({
        @ComponentScan("com.example.bookmanager.service")
})
@MapperScan("com.example.bookmanager.mapper")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement      //配置事务要记得加注解
public class RootConfiguration {
    @Bean
    public DataSource dataSource(){
        HikariDataSource dataSource=new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/book_manage");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("200115");
        return dataSource;
    }

    //分页插件
    @Bean
    public PageInterceptor pageInterceptor(){
        PageInterceptor pageInterceptor=new PageInterceptor();
        Properties properties=new Properties();
        properties.setProperty("value", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Autowired DataSource dataSource){
        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //mybatis整合分页插件↓
        Interceptor[] plugins={pageInterceptor()};
        bean.setPlugins(plugins);
        return bean;
    }

    //配置事务
    @Bean
    public TransactionManager transactionManager(@Autowired DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
