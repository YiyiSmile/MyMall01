package indi.tom.mymall01.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import indi.tom.mymall01.bean.UserInfo;
import indi.tom.mymall01.interfaces.UserService;
import indi.tom.mymall01.user.mapper.UserMapper;

import indi.tom.mymall01.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.DigestUtils;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

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

    @Autowired
    RedisUtil redisUtil;

    private static final int userInfokeyTimeout = 60*60*24;
    private static final String userInfoKeyPrefix = "user:";
    private static final String userInfoKeySuffix = ":info";

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
        String passwdMd5 = DigestUtils.md5DigestAsHex(userInfo.getPasswd().getBytes());
        userInfo.setPasswd(passwdMd5);
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

    @Override
    public UserInfo login(UserInfo userInfo) {
        //将密码使用md5转成密文
        String passwd = userInfo.getPasswd();
        String passwdMd5 = DigestUtils.md5DigestAsHex(passwd.getBytes());
        userInfo.setPasswd(passwdMd5);

        //根据登录名和密码查询
        UserInfo userInfoReturn = userMapper.selectOne(userInfo);
        //如果查询到，加入到redis中
        if(userInfoReturn!=null){
            Jedis jedis = redisUtil.getJedis();
            //1. 类型 String 2. key user:id:info 3. value
            String key = userInfoKeyPrefix + userInfoReturn.getId() + userInfoKeySuffix;
            jedis.setex(key,userInfokeyTimeout, JSON.toJSONString(userInfoReturn));
            jedis.close();
            return userInfoReturn;
        }else{

            return null;
        }
    }

    @Override
    public Boolean verify(Map<String,Object> map) {
        Jedis jedis = redisUtil.getJedis();
        String userId = (String) map.get("userId");
        String userinfo = jedis.get(userInfoKeyPrefix + userId + userInfoKeySuffix);
        if(userinfo!=null){
            jedis.expire(userInfoKeyPrefix + userId + userInfoKeySuffix, userInfokeyTimeout);
            return true;
        }else{
            //如果缓存中没有，查询数据库，并放入缓存。
            return false;
        }

    }

    //jedis test
    public static void main(String[] args) {
//        RedisUtil redisUtil=new RedisUtil();
//        redisUtil.initJedisPool("redis.mymall01.com",6379,0);
//        Jedis jedis = redisUtil.getJedis();
//        jedis.setex("test", 100*100, "test");
//        System.out.println(jedis.get("test"));
//        System.out.println(jedis.get("test1"));
        Boolean temp = testBoolean();
        if(temp){
            System.out.println("This is True");
        }else{
            System.out.println("This is false");
        }
        System.out.println();

    }

    public static Boolean testBoolean(){
        return true;
    }


}
