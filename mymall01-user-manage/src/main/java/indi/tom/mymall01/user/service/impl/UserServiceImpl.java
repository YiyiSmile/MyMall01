package indi.tom.mymall01.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import indi.tom.mymall01.bean.UserInfo;
import indi.tom.mymall01.interfaces.UserService;
import indi.tom.mymall01.user.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;

import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/12 15:09
 * @Version 1.0
 * @Description
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public List<UserInfo> getUserInfoListAll() {
        List<UserInfo> userInfos = userMapper.selectAll();
        return userInfos;
    }

    @Override
    public UserInfo getUserById(String id) {
        UserInfo userInfo;

        userInfo = userMapper.selectByPrimaryKey(id);
        return userInfo;
    }

    @Override
    public void addUser(UserInfo userInfo) {
        userMapper.insertSelective(userInfo);

    }

    @Override
    public void updateUser(UserInfo userInfo) {
        userMapper.updateByPrimaryKeySelective(userInfo);

    }

    @Override
    public void updateUserByName(String name, UserInfo userInfo) {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("name", name);
        userMapper.updateByExampleSelective(userInfo, example);
    }

    @Override
    public void delUser(UserInfo userInfo) {
        userMapper.deleteByPrimaryKey(userInfo.getId());
    }


}
