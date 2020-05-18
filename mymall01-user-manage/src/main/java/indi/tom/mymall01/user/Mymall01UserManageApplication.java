package indi.tom.mymall01.user;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "indi.tom.mymall01.user.mapper")
@ComponentScan("indi.tom.mymall01")
public class Mymall01UserManageApplication {

    public static void main(String[] args) {

        SpringApplication.run(Mymall01UserManageApplication.class, args);
    }

}
