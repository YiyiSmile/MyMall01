package indi.tom.mymall01.cartweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("indi.tom.mymall01")
public class Mymall01CartWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(Mymall01CartWebApplication.class, args);
    }

}
