package indi.tom.mymall01.cartservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan("indi.tom.mymall01")
@MapperScan("indi.tom.mymall01.cartservice.mapper")
public class Mymall01CartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Mymall01CartServiceApplication.class, args);
    }

}
