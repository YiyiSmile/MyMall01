package indi.tom.mymall01.backmanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import tk.mybatis.spring.annotation.MapperScan;

@Configuration
@SpringBootApplication
@MapperScan(basePackages = "indi.tom.mymall01.backmanage.mapper")
@ComponentScan(basePackages = "indi.tom.mymall01.config")
public class Mymall01BackManageServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(Mymall01BackManageServiceApplication.class, args);
    }

}
