package indi.tom.mymall01.cartservice.mapper;

import indi.tom.mymall01.bean.CartInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author Tom
 * @Date 2020/5/19 15:38
 * @Version 1.0
 * @Description
 */
public interface CartInfoMapper extends Mapper<CartInfo> {
    List<CartInfo> selectByUserId(String userId);
}
