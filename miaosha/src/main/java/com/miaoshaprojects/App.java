package com.miaoshaprojects;

import com.miaoshaprojects.dao.UserDOMapper;
import com.miaoshaprojects.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
//把App变成SpringBoot的bean，并且能启动自动化配置
//@EnableAutoConfiguration
//让SpringBoot扫描Mybatis的配置文件
@SpringBootApplication(scanBasePackages = {"com.miaoshaprojects"})
//使用SpringMVC Controller的功能 -- 1.
@RestController
//Dao层位置
@MapperScan("com.miaoshaprojects.dao")
public class App 
{
    @Autowired
    private UserDOMapper userDOMapper;

    //使用SpringMVC Controller的功能 -- 2.
    @RequestMapping("/")
    public String home(){
        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        if (userDO == null){
            return "对象不存在";
        }else {
            return userDO.getName();
        }
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        //启动SpringBoot项目
        SpringApplication.run(App.class,args);
    }
}
