package com.example.bookmanager.config;

import com.example.bookmanager.entity.AuthUser;
import com.example.bookmanager.mapper.UserMapper;
import com.example.bookmanager.service.impl.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    UserMapper mapper;

    @Autowired
    UserAuthService service;

    /*如果服务器重新启动（因为Token信息全部存在HashMap中，也就是存在内存中），那么所有记录的Token信息将全部丢失，这时即使浏览器携带了之前的Token也无法恢复之前登陆的身份。
    我们可以将Token信息记录全部存放到数据库中（学习了Redis之后还可以放到Redis服务器中）利用数据库的持久化存储机制，即使服务器重新启动，所有的Token信息也不会丢失，配置数据库存储也很简单*/
    @Autowired
    PersistentTokenRepository repository;
    @Bean
    public PersistentTokenRepository jdbcRepository(@Autowired DataSource dataSource){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();  //使用基于JDBC的实现
        repository.setDataSource(dataSource);   //配置数据源
        //repository.setCreateTableOnStartup(true);   //启动时自动在数据库创建用于存储Token的表（建议第一次启动之后删除该行）
        return repository;
    }

    //不使用SpringSecurity默认的登陆页面，使用自己的登陆页面
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()   //首先需要配置哪些请求会被拦截，哪些请求必须具有什么角色才能访问
                .antMatchers("/static/**","/page/auth/**","/api/auth/**").permitAll()    //静态资源，使用permitAll来运行任何人访问（注意一定要放在前面）
                .antMatchers("/page/user/**","/api/user/**").hasRole("user")
                .antMatchers("/page/admin/**","/api/admin/**").hasRole("admin")
                .anyRequest().hasAnyRole("user","admin")

                /*.antMatchers("/index").hasAnyRole("user","admin")    //所有请求必须登陆并且是user或admin角色才可以访问（不包含上面的静态资源），也可以用.anyRequest().hasRole("user")
                .anyRequest().hasRole("admin")     //管理员可以访问任何页面*/

                /*也可以不用角色，直接设置权限，UserAuthService那里也要改
                .antMatchers("/index").hasAnyAuthority("page:index")      //有page:index权限只能访问/index
                .anyRequest().hasAnyAuthority("page:admin")        //有page:admin权限可以访问任何页面*/

                //使用注解实现时可以只配置静态资源，其余的交给注解实现

                .and()
                .formLogin()               //配置Form表单登陆，也可以用其他登陆方式
                .loginPage("/page/auth/login")       //登陆页面地址（GET）
                .loginProcessingUrl("/api/auth/login")
                //这路是form表单提交地址（POST）,处理登录是SpringSecurity自己实现的，只需要编个地址写一下就行了，前端表单的action要写同样的地址

                //.defaultSuccessUrl("/index",true)       //登陆成功后跳转的页面，也可以通过Handler实现高度自定义
                .successHandler(this::onAuthenticationSuccess)
                /*.successHandler(new AuthenticationSuccessHandler() { public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {}})
                样子看着乱，改为lamda表达式
                这样可以让用户名一开始就传给session，前端页面可以随时获取*/
                //.permitAll()                      //登陆页面也需要允许所有人访问，也可以写在上面静态资源那里

                .and()
                .logout()
                .logoutUrl("/api/auth/logout")           //前端退出登陆的请求地址
                .clearAuthentication(true)
                .logoutSuccessUrl("/login")     //退出后重定向的地址
                .and()
                //.csrf().disable()     //关闭csrf，之前因为没有关闭csrf，又没有在前端页面带上token导致403禁止访问
                //实现记住我
                .rememberMe()   //开启记住我功能
                .rememberMeParameter("remember")  //登陆请求表单中需要携带的参数，如果携带，那么本次登陆会被记住，前端也需要修改一下记住我勾选框的名称，名称一致才可以
                //.tokenRepository(new InMemoryTokenRepositoryImpl())  //这里使用的是直接在内存中保存token的TokenRepository实现
                //TokenRepository有很多种实现，InMemoryTokenRepositoryImpl直接基于Map实现的，缺点就是占内存、服务器重启后记住我功能将失效
                //后面我们还会讲解如何使用数据库来持久化保存Token信息
                .tokenRepository(repository)      //使用数据库存token
                //.alwaysRemember(true)
                .authenticationSuccessHandler(this::onAuthenticationSuccess)     //给记住我也添上SuccessHandler里的身份认证，不然会报错
                .tokenValiditySeconds(60*60*24*7);    //设置token过期时间
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();  //这里使用SpringSecurity提供的BCryptPasswordEncoder
        auth
                .inMemoryAuthentication() //直接验证方式，之后会讲解使用数据库验证
                .passwordEncoder(encoder) //密码加密器
                .withUser("test")   //用户名
                .password(encoder.encode("123456"))   //这里需要填写加密后的密码
                .roles("user");   //用户的角色（之后讲解）*/
        auth
                .userDetailsService(service)   //使用自定义的Service实现类进行验证
                .passwordEncoder(new BCryptPasswordEncoder());   //依然使用BCryptPasswordEncoder
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        AuthUser user=mapper.getPasswordByUsername(authentication.getName());     //authentication是Security在用户点击登录后自己生成的
        HttpSession session=request.getSession();       //在登陆一完成就将用户传给session，也可以在controller里用SecurityContext获取，见PageController里的index
        session.setAttribute("user",user);
        if(user.getRole().equals("user")){
            response.sendRedirect("/BookManager_war_exploded/page/user/index");
        }
        if(user.getRole().equals("admin")){
            response.sendRedirect("/BookManager_war_exploded/page/admin/index");
        }
    }
}
