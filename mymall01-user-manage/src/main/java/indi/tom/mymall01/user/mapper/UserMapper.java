package indi.tom.mymall01.user.mapper;

import indi.tom.mymall01.user.bean.UserInfo;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author Tom
 * @Date 2019/11/12 15:01
 * @Version 1.0
 * @Description 使用了通用mapper之后，在usermapper里不需要定义额外的方法，都继承自Mapper里的方法。
 */
public interface UserMapper extends Mapper<UserInfo> {
}
