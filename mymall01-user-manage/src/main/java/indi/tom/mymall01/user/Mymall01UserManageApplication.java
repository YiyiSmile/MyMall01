package indi.tom.mymall01.user;

import indi.tom.mymall01.user.bean.UserInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "indi.tom.mymall01.user.mapper")
public class Mymall01UserManageApplication {

    public static void main(String[] args) {

        SpringApplication.run(Mymall01UserManageApplication.class, args);
    }

}
