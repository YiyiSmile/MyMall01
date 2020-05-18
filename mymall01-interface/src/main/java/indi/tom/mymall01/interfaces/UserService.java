package indi.tom.mymall01.interfaces;


import indi.tom.mymall01.bean.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserInfo> getUserInfoListAll();
    UserInfo getUserById(String id);
    void addUser(UserInfo userInfo);
    void updateUser(UserInfo userInfo);
    void updateUserByName(String name, UserInfo userInfo);
    void delUser(UserInfo userInfo);
    UserInfo login(UserInfo userInfo);
    Boolean verify(Map<String, Object> map);
}
