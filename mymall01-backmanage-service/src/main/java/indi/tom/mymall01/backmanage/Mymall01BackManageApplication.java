package indi.tom.mymall01.backmanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "indi.tom.mymall01.backmanage.mapper")
public class Mymall01BackManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(Mymall01BackManageApplication.class, args);
    }

}
