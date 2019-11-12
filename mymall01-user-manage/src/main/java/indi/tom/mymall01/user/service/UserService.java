package indi.tom.mymall01.user.service;

import indi.tom.mymall01.user.bean.UserInfo;

import java.util.List;

public interface UserService {
    List<UserInfo> getUserInfoListAll();
    UserInfo getUserById(String id);
    void addUser(UserInfo userInfo);
    void updateUser(UserInfo userInfo);
    void updateUserByName(String name, UserInfo userInfo);
    void delUser(UserInfo userInfo);
}
