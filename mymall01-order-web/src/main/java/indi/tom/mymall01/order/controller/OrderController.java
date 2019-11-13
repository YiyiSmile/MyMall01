package indi.tom.mymall01.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import indi.tom.mymall01.bean.UserInfo;
import indi.tom.mymall01.interfaces.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Tom
 * @Date 2019/11/12 21:50
 * @Version 1.0
 * @Description
 */
@RestController
public class OrderController {
    /*不要引成了@jdk.nashorn.internal.ir.annotations.Reference*/
    @Reference
    UserService userService;
    @GetMapping("trade")
    public UserInfo trade(String userid){
        UserInfo user = userService.getUserById(userid);
        return user;
    }



}
