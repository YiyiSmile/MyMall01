package indi.tom.mymall01.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("indi.tom.mymall01")
public class Mymall1ItemWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(Mymall1ItemWebApplication.class, args);
    }

}
