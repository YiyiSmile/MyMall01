package indi.tom.mymall01.cartweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import indi.tom.mymall01.annotation.LoginRequired;
import indi.tom.mymall01.bean.CartInfo;
import indi.tom.mymall01.interfaces.CartService;
import indi.tom.mymall01.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

/**
 * @Author Tom
 * @Date 2020/5/19 14:01
 * @Version 1.0
 * @Description
 */
@Controller
public class CartController {

    @Reference
    CartService cartService;

    @PostMapping("addToCart")
    @LoginRequired(autoRedirect = false)
    public String addToCart(int num, String skuId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userId = (String) request.getAttribute("userId");
        if (null == userId) {
            userId = CookieUtil.getCookieValue(request, "temp_user_id", false);
            if (null == userId) {
                userId = UUID.randomUUID().toString();
                CookieUtil.setCookie(request, response, "temp_user_id", userId, 60 * 60 * 24, false);
            }

        }
        CartInfo cartInfo = cartService.addToCart(userId, skuId, num);
        request.setAttribute("cartInfo", cartInfo);
        request.setAttribute("num", num);
        String originUrl = URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");
        request.setAttribute("originUrl", originUrl);
        return "success";

    }

    @GetMapping("cartList")
    @LoginRequired(false)
    public String cartList(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        //获取userId
        String userId = (String) request.getAttribute("userId");
        //从cookie中获取临时用户id
        String tempUserId = CookieUtil.getCookieValue(request, "temp_user_id", false);
        List<CartInfo> cartInfos = null;
        //1.用户没登陆
        if (null == userId) {
            if (null == tempUserId) {
                //返回空的购物车
                return "cartList";
            } else {
                //返回临时用户的购物车
                //查询临时用户购物车
                cartInfos = cartService.cartList(tempUserId);
                return handleCartListByUserId(userId, request,cartInfos);
            }
        } else {
            //2.用户已经登陆
            /*2.1 用户没有临时id，显示当前登录用户的购物车

             */
            if (null == tempUserId) {
                cartInfos = cartService.cartList(userId);
                return handleCartListByUserId(userId, request,cartInfos);
            } else {
                /*
               2.2 当前用户请求中有临时用户id
                 2.2.3 从cookie中删除temp userId
                 2.2.4 根据 userId 和temp userId同时去后台查询购物车并传给返回页面
*/
                CookieUtil.deleteCookie(request, response, "temp_user_id");
                cartInfos = cartService.cartListByUserIdAndTempUserId(userId,tempUserId);
                return handleCartListByUserId(userId, request,cartInfos);
            }
        }

    }

    private String handleCartListByUserId(String userId, HttpServletRequest request, List<CartInfo> cartInfos) throws UnsupportedEncodingException {

        //计算总价
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartInfo cartInfo : cartInfos) {

            totalPrice = totalPrice.add(cartInfo.getSkuPrice().multiply(new BigDecimal(cartInfo.getSkuNum())));

        }
        request.setAttribute("cartInfoList", cartInfos);
        request.setAttribute("userId", userId);
        request.setAttribute("totalPrice", totalPrice);
        String originUrl= URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");
        request.setAttribute("originUrl", originUrl);
        return "cartList";
    }
}
