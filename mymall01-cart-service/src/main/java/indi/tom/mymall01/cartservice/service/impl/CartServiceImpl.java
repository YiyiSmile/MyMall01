package indi.tom.mymall01.cartservice.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import indi.tom.mymall01.bean.CartInfo;
import indi.tom.mymall01.bean.SkuInfo;
import indi.tom.mymall01.cartservice.mapper.CartInfoMapper;
import indi.tom.mymall01.interfaces.CartService;
import indi.tom.mymall01.interfaces.SkuService;
import indi.tom.mymall01.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Tom
 * @Date 2020/5/19 15:07
 * @Version 1.0
 * @Description
 */
@Service
public class CartServiceImpl implements CartService {

    @Reference
    SkuService skuService;
    @Autowired
    CartInfoMapper cartInfoMapper;

    @Autowired
    RedisUtil redisUtil;

    private static final String CART_KEY_PREFIX = "cart:";
    private static final String CART_KEY_SUFFIX = ":info";

    @Override
    public CartInfo addToCart(String userId, String skuId, Integer num) throws Exception {
        //1. 将当前商品加入到数据库
        //1.1 根据用户id, skuId查询数据库cart_info
        CartInfo cartInfoQuery = new CartInfo();
        cartInfoQuery.setUserId(userId);
        cartInfoQuery.setSkuId(skuId);
        CartInfo cartInfoExist = cartInfoMapper.selectOne(cartInfoQuery);

        Example example = null;
        //1.1.1 如果没查到，根据skuId查询数据库sku_info表
        if (null == cartInfoExist) {
            SkuInfo skuInfo = skuService.getSkuInfoById(skuId);
            //1.1.1.2 写入到cart_info表
            CartInfo cartInfo = new CartInfo();
            //sku_info中procie是double，而cartInfo是bigdecimal,需要转换一下
            cartInfo.setCartPrice(BigDecimal.valueOf(skuInfo.getPrice()));
            cartInfo.setSkuPrice(BigDecimal.valueOf(skuInfo.getPrice()));
            cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setSkuNum(num);
            cartInfo.setUserId(userId);
            cartInfo.setSkuId(skuId);
            //应该用insertSelective
            cartInfoMapper.insert(cartInfo);
            cartInfoExist = cartInfo;
        } else {
            //1.1.2 如果查到，将该商品num加1，然后存入数据库中
            cartInfoExist.setSkuNum(cartInfoExist.getSkuNum() + num);
            cartInfoMapper.updateByPrimaryKey(cartInfoExist);
        }
        //2. 将该用户购物车中所有商品加入到缓存.这里之所以不是将当前sku加入到缓存中，二十将该用户下所有
        // cart_info中sku加入缓存中，是因为之前加入到缓存的sku可能已经过期，已经被删掉。如果单单加一条
        //，那用户查看购物车所有商品时，就不能全部显示。
        example = new Example(CartInfo.class);
        example.createCriteria().andEqualTo("userId", userId);
        List<CartInfo> cartInfos = cartInfoMapper.selectByExample(example);

        //缓存设计 类型: hash key: cart:userid:info
        Jedis jedis = redisUtil.getJedis();
        //将 list cartInfos转换成map
        Map<String, String> cartInfoMap = new HashMap<>();
        for (CartInfo c : cartInfos) {
            cartInfoMap.put(c.getSkuId(), JSON.toJSONString(c));
        }
        jedis.hmset(CART_KEY_PREFIX + userId + CART_KEY_SUFFIX, cartInfoMap);
        jedis.close();
        return cartInfoExist;
    }

    @Override
    public List<CartInfo> cartList(String userId) {
        List<CartInfo> cartInfos = new ArrayList<>();
        //1. 从缓存中获取
        Jedis jedis = redisUtil.getJedis();
        String key = CART_KEY_PREFIX + userId + CART_KEY_SUFFIX;
        List<String> cartInfoListString = jedis.hvals(key);

        //缓存中没有，jedis.hvals(key)返回的是一个size是0的list，不是null
        if (null != cartInfoListString && 0 != cartInfoListString.size()) {
            //1.1 将List<String> 转换成List<CartInfo>
            for (String s : cartInfoListString) {
                CartInfo cartInfo = JSON.parseObject(s, CartInfo.class);
                cartInfos.add(cartInfo);
            }

        } else {
            //2. 缓存中没有，从数据库获取
            cartInfos = cartInfoMapper.selectByUserId(userId);
            //2.1 将从数据库中取的购物车列表存入缓存
            HashMap<String, String> map = new HashMap<>();
            for (CartInfo s : cartInfos) {
                map.put(s.getSkuId(), JSON.toJSONString(s));
            }
            jedis.hmset(key, map);


        }
        jedis.close();
        return cartInfos;
    }

    @Override
    public List<CartInfo> cartListByUserIdAndTempUserId(String userId, String tempUserId) {
        /*
        总体思路：现根据temp userID查询临时用户购物车，并合并到真实用户的购物车中。跟后根据真实用户id查询数据库，
        更新缓存并返回。
        1. 现根据temp userID从缓存中查询临时用户购物车
		 1.1 如果没有，从数据库中根据temp userID查询临时用户购物车

		2. 如果有临时用户购物车数据，合并，根据真实用户id从数据库查询购物车数据，并更新缓存。
		3. 如果没有临时用户购物车数据
		  3.1 根据真实用户id从缓存查询购物车数据
		    3.1.2 如果没查到，查询数据库
			  3.1.2.1 如果有结果，跟新缓存，并返回
			  3.1.2.2 如果没有，返回
        * */

        Jedis jedis = redisUtil.getJedis();
        //临时用户购物车列表
        List<CartInfo> tempUsercartInfos = null;
        //登录(真实)用户购物车列表，这个结果最终返回给controller
        List<CartInfo> realUsercartInfos = null;
        List<String> tempCartInfoStringList = jedis.hvals(CART_KEY_PREFIX + tempUserId + CART_KEY_SUFFIX);
        if (null == tempCartInfoStringList || tempCartInfoStringList.size() == 0) {
            Example example = new Example(CartInfo.class);
            example.createCriteria().andEqualTo("userId", tempUserId);
            tempUsercartInfos = cartInfoMapper.selectByExample(example);
        } else {
            for (String s : tempCartInfoStringList) {
                tempUsercartInfos = new ArrayList<>();
                tempUsercartInfos.add(JSON.parseObject(s, CartInfo.class));
            }
        }
        if (tempUsercartInfos.size() > 0) {
            /*2. 如果有临时用户购物车数据，合并，并清除临时用户购物车(缓存加数据库)*/
            for (CartInfo cartInfo : tempUsercartInfos) {
                cartInfo.setUserId(userId);
                cartInfo.setId(null);
                String cartInfoString = jedis.hget(CART_KEY_PREFIX + userId + CART_KEY_SUFFIX, cartInfo.getSkuId());
                CartInfo cartInfoTemp = null;
                boolean isSkuExistInRealUserCart = false;
                if (null != cartInfoString) {
                    cartInfoTemp = JSON.parseObject(cartInfoString, CartInfo.class);
                    cartInfo.setSkuNum(cartInfo.getSkuNum() + cartInfoTemp.getSkuNum());
                    cartInfo.setId(cartInfoTemp.getId());
                    isSkuExistInRealUserCart = true;
                } else {
                    CartInfo cartInfo1 = new CartInfo();
                    cartInfo1.setUserId(userId);
                    cartInfo1.setSkuId(cartInfo.getSkuId());
                    cartInfoTemp = cartInfoMapper.selectOne(cartInfo1);
                    if (null != cartInfoTemp) {
                        cartInfo.setSkuNum(cartInfoTemp.getSkuNum() + cartInfo.getSkuNum());
                        cartInfo.setId(cartInfoTemp.getId());
                        isSkuExistInRealUserCart = true;
                    }
                }
                if(isSkuExistInRealUserCart){
                    cartInfoMapper.updateByPrimaryKey(cartInfo);
                }else{
                    cartInfoMapper.insertSelective(cartInfo);
                }
            }
            //清除临时用户购物车(缓存加数据库)
            String tempUserKey = CART_KEY_PREFIX + tempUserId + CART_KEY_SUFFIX;
            Boolean existsInRedis = jedis.exists(tempUserKey);
            if(existsInRedis){
                jedis.del(tempUserKey);
            }
            Example example = new Example(CartInfo.class);
            example.createCriteria().andEqualTo("userId", tempUserId);
            cartInfoMapper.deleteByExample(example);
            //获取最终返回购物车列表
            realUsercartInfos = cartInfoMapper.selectByUserId(userId);
        } else {
            /*
          3. 如果没有临时用户购物车数据
		  3.1 根据真实用户id从缓存查询购物车数据
		    3.1.2 如果没查到，查询数据库
			  3.1.2.1 如果有结果，跟新缓存，并返回
			  3.1.2.2 如果没有，返回*/
            List<String> realUserCartInfoString = jedis.hvals(CART_KEY_PREFIX + userId + CART_KEY_SUFFIX);
            if(realUserCartInfoString.size() ==0){
                realUsercartInfos = cartInfoMapper.selectByUserId(userId);
            }else{
                realUsercartInfos = new ArrayList<>();
                for (String s : realUserCartInfoString) {
                    realUsercartInfos.add(JSON.parseObject(s, CartInfo.class));
                }
            }

        }
        return realUsercartInfos;
        //        if(null == tempCartInfoStringList || tempCartInfoStringList.size() == 0){
//            //不需要查询skuInfo表的实时价格，只在cartInfo表查询，所以不用下边这个联合查询
////            cartInfoMapper.selectByUserId()
//            Example example = new Example(CartInfo.class);
//            example.createCriteria().andEqualTo("userId", tempUserId);
//            List<CartInfo> cartInfos = cartInfoMapper.selectByExample(example);
//            if(cartInfos != null && cartInfos.size()>0){
//                for (CartInfo cartInfo : cartInfos) {
//                    cartInfo.setUserId(userId);
//                    cartInfo.setId(null);
//                    String cartInfoString = jedis.hget(CART_KEY_PREFIX + userId + CART_KEY_SUFFIX, cartInfo.getSkuId());
//                    if(null!=cartInfoString){
//                        CartInfo cartInfoTemp = JSON.parseObject(cartInfoString, CartInfo.class);
//                        cartInfo.setSkuNum(cartInfoTemp.getSkuNum() + cartInfoTemp.getSkuNum());
//                    }else{
//                        CartInfo cartInfo1 = new CartInfo();
//                        cartInfo1.setUserId(userId);
//                        cartInfo1.setSkuId(cartInfo.getSkuId());
//                        CartInfo cartInfo2 = cartInfoMapper.selectOne(cartInfo1);
//                        if(null!=cartInfo2){
//                            cartInfo2.setSkuNum(cartInfo2.getSkuNum()+cartInfo.getSkuNum());
//                            cartInfoMapper.updateByPrimaryKey(cartInfo2);
//                        }else{
//                            cartInfoMapper.insertSelective(cartInfo);
//                        }
//                    }
//                }
//            }
//        }else{
//           /*1.2 如果缓存中有，
//            *    1.2.1 将结果更改成用户id，存入数据库(合并)，
//            *    1.2.2 将temp userId的cartInfo数据从缓存删除*/
//
//            //将List<string>转成List<Cartinfo>
//            ArrayList<CartInfo> cartInfos = new ArrayList<CartInfo>;
//            for (String s : tempCartInfoStringList) {
//                cartInfos.add(JSON.parseObject(s, CartInfo.class));
//            }
//
//            //遍历临时用户购物车，针对每条记录，根据真实用户id和skuId分别查询缓存和数据库，得到对应的记录，
//            // 将数量skuNum加到遍历变量cartInfo上去。如果没查到,直接将遍历变量cartInfo插入数据库，否则更新数据库。
//            for (CartInfo cartInfo : cartInfos) {
//
//                cartInfo.setUserId(userId);
//                cartInfo.setId(null);
//                //查缓存
//                String cartInfoString = jedis.hget(CART_KEY_PREFIX + userId + CART_KEY_SUFFIX, cartInfo.getSkuId());
//                if(null!=cartInfoString){
//                    CartInfo cartInfoTemp = JSON.parseObject(cartInfoString, CartInfo.class);
//                    cartInfo.setSkuNum(cartInfoTemp.getSkuNum() + cartInfoTemp.getSkuNum());
//
//                }
//                //查数据库
//                cartInfoMapper.selectOne()
//                /*{
//                    CartInfo cartInfo1 = new CartInfo();
//                    cartInfo1.setUserId(userId);
//                    cartInfo1.setSkuId(cartInfo.getSkuId());
//                    CartInfo cartInfo2 = cartInfoMapper.selectOne(cartInfo1);
//                    if(null!=cartInfo2){
//                        cartInfo2.setSkuNum(cartInfo2.getSkuNum()+cartInfo.getSkuNum());
//                        cartInfoMapper.updateByPrimaryKey(cartInfo2);
//                    }else{
//                        cartInfoMapper.insertSelective(cartInfo);
//                    }
//               */ }
//
//            }
//        }
//        jedis.close();
//        return null;
    }


}
