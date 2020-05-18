package indi.tom.mymall01.user.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import indi.tom.mymall01.bean.UserInfo;
import indi.tom.mymall01.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/12 15:16
 * @Version 1.0
 * @Description
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("allUser")
    public List<UserInfo> getAllUser(){
        List<UserInfo> userInfoListAll = userService.getUserInfoListAll();
        return userInfoListAll;
    }

    @PostMapping("addUser")
    public String addUser(UserInfo userInfo){
        userService.addUser(userInfo);
        return "Add user success!!";
    }

    @PostMapping("updateUser")
    public String updateUser(UserInfo userInfo){
        userService.updateUser(userInfo);
        return "Update user success!!";
    }

    @PostMapping("updateUserByName")
    public String updateUserByName(UserInfo userInfo){
        userService.updateUserByName(userInfo.getName(), userInfo);
        return "Update user by name success!!";
    }

    @PostMapping("deleteUser")
    public String deleteUser(UserInfo userInfo){
        userService.delUser(userInfo);
        return "Delete user success!!";
    }

    @GetMapping("getUser")
    public UserInfo getUser(String id){
        UserInfo userInfo = userService.getUserById(id);
        return userInfo;
    }
}
