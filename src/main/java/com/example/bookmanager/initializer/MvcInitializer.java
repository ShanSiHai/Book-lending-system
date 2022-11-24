package com.example.bookmanager.initializer;

import com.example.bookmanager.config.MvcConfiguration;
import com.example.bookmanager.config.RootConfiguration;
import com.example.bookmanager.config.SecurityConfiguration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
//创建Spring上下文
public class MvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    /*@Override
    public void onStartup(ServletContext servletContext) throws ServletException {      //将CharacterEncodingFilter提前，提到springSecurity自带的过滤器之前
        servletContext.addFilter("characterEncodingFilter",new CharacterEncodingFilter("UTF-8",true))
                .addMappingForUrlPatterns(null,false,"/*");
        super.onStartup(servletContext);
    }        //这里还是不够靠前，要添加到Security自己的初始化类里*/

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfiguration.class, SecurityConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{MvcConfiguration.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /*@Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new CharacterEncodingFilter("UTF-8",true)};    //解决用户注册输入的汉字传给服务器乱码问题，但是到这里就晚了，会先走SpringSecurity的过滤器，到这里已经是乱码了
    }*/
}
