package indi.tom.mymall01.interfaces;

import indi.tom.mymall01.bean.CartInfo;

import java.util.List;

public interface CartService {
    CartInfo addToCart(String userId, String skuId, Integer num) throws Exception;
    List<CartInfo>  cartList(String userId);

    List<CartInfo> cartListByUserIdAndTempUserId(String userId, String tempUserId);
}
