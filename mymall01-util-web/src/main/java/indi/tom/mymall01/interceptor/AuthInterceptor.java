package indi.tom.mymall01.interceptor;

import com.alibaba.fastjson.JSON;
import indi.tom.mymall01.annotation.LoginRequired;
import indi.tom.mymall01.util.CookieUtil;
import indi.tom.mymall01.util.HttpClientUtil;
import io.jsonwebtoken.impl.Base64UrlCodec;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static indi.tom.mymall01.constants.WebConstant.*;

/**
 * @Author Tom
 * @Date 2020/5/18 14:33
 * @Version 1.0
 * @Description
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {


    /*设置拦截器，所有模块的所有web请求都先发给拦截器，完成以下功能
    * 1. 跳转到认证/注册页面
    * 2. 验证客户是否登录
    * 3. 设置用户id, 昵称给页面方法使用，以在返回的页面中带有用户昵称
    *
    * 具体流程如下：
    * 1. 检查URL中是否带有toen参数(完成登录，第一次访问业务会在url中带有token)
    *    1.1 如果URL有token，验证token是否有效
    *      1.1.1 如果有效，将token写入cookie中,提取nickName, userId并放入request中返回
    *      1.1.2 如果无效，转入登录页面，返回
         1.2 如果url中没有token
         * 1.2.1 检查cookie中是否有token
         *   1.2.1.1 如果cookie中有，验证token是否有效
         *     1.2.1.1.1 如果有效，提取userID和nickName并放到request中
         *     1.2.1.1.2 如果无效，检查请求方法是否需要认证
         *       1.2.1.1.2.1 如果需要验证，跳转到登录页面
         *       1.2.1.1.2.2 如果不需要验证，提取NickName并放入请求中，返回
         *   1.2.1.2 如果cookie中没有token,检查请求方法是否需要认证
         *       1.2.1.2.1 如果需要验证，跳转到登录页面
         *       1.2.1.2.2 如果不需要验证，返回

         */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getParameter("token");
        if(null!=token){
            /*1. 检查URL中是否带有toen参数(完成登录，第一次访问业务会在url中带有token)
             *    1.1 如果URL有token，验证token是否有效
             *      1.1.1 如果有效，将token写入cookie中,提取nickName, userId并放入request中返回
             *      1.1.2 如果无效，
             *        1.1.2.1 检查请求方法是否需要登录
             *          1.1.2.1.1 如果不需要登录 提取nickName，写入request中， return ture，放行
             *          1.1.2.1.2 如果需要登录，
             *             1.1.2.1.2.1 如果autoRedirect 属性值是true, 转入登录页面，返回false，请求方法不被执行(这种情况不太可能，如果有发生，多半是有人恶意伪造token)
             *             1.1.2.1.2.2 如果autoRedirect 属性值是false, 提取nickName，写入request中， 返回true.
             * */

            //使用nginx反向代理
            //String currentIP = request.getHeader("X-forwarded-for");
            //不适用nginx 代理
            String currentIp = request.getRemoteAddr();
            String verifyResult = HttpClientUtil.doGet(TOKEN_VERIFY_URL + "?token=" + token + "&ipAddr=" + currentIp);
            if("success".equals(verifyResult)){
                CookieUtil.setCookie(request, response, "token", token, COOKIE_MAX_AGE, false);
                setNickNameAndUserIdOfRequest(token,request);
                return true;
            }else{
                return checkRequestedMethodAnnotation(token,request,response,handler);

            }

        }else{
            /* 1.2 如果url中没有token
             * 1.2.1 检查cookie中是否有token
             *   1.2.1.1 如果cookie中有，验证token是否有效
             *     1.2.1.1.1 如果有效，提取userID和nickName并放到request中
             *     1.2.1.1.2 如果无效，检查请求方法是否需要认证
             *       1.2.1.1.2.1 如果需要验证，跳转到登录页面 返回false,请求方法不被执行。
             *       1.2.1.1.2.2 如果不需要验证，提取NickName并放入请求中，返回true,请求方法被调用
             *   1.2.1.2 如果cookie中没有token, 检查请求方法是否需要登录
             *          1.2.1.2.1 如果不需要登录 提取nickName，写入request中， return ture，放行
             *          1.2.1.2.2 如果需要登录，
             *             1.2.1.2.2.1 如果autoRedirect 属性值是true, 转入登录页面，返回false，请求方法不被执行(这种情况不太可能，如果有发生，多半是有人恶意伪造token)
             *             1.2.1.2.2.2 如果autoRedirect 属性值是false, 提取nickName，写入request中， 返回true.
             */
            token = CookieUtil.getCookieValue(request, "token", false);
            if(null != token){
                String currentIp = request.getRemoteAddr();
                String verifyResult = HttpClientUtil.doGet(TOKEN_VERIFY_URL + "?token=" + token + "&ipAddr=" + currentIp);
                if("success".equals(verifyResult)){
                    setNickNameAndUserIdOfRequest(token, request);
                    return true;
                }else{
                    return checkRequestedMethodAnnotation(token,request,response,handler);
                }
            }else{
                /* 1.2.1.2 如果cookie中没有token, 检查请求方法是否需要登录
                 *          1.2.1.2.1 如果不需要登录 提取nickName，写入request中， return ture，放行
                 *          1.2.1.2.2 如果需要登录，
                 *             1.2.1.2.2.1 如果autoRedirect 属性值是true, 转入登录页面，返回false，请求方法不被执行(这种情况不太可能，如果有发生，多半是有人恶意伪造token)
                 *             1.2.1.2.2.2 如果autoRedirect 属性值是false, 提取nickName，写入request中， 返回true.*/
                return checkRequestedMethodAnnotation(token,request,response,handler);
            }
        }
    }

    private Map<String, Object> getUserMapFromToken(String token) {
        String userBase64 = StringUtils.substringBetween(token, ".");
        Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
        byte[] userBytes = base64UrlCodec.decode(userBase64);

        String userJson = new String(userBytes);

        Map userMap = JSON.parseObject(userJson, Map.class);

        return userMap;
    }
    private void setNickNameOfRequest(String token, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map = getUserMapFromToken(token);
        String nickName = (String)map.get("nickName");
        request.setAttribute("nickName", nickName);
    }
    private void setNickNameAndUserIdOfRequest(String token, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map = getUserMapFromToken(token);
        String userId = (String)map.get("userId");
        String nickName = (String)map.get("nickName");
        request.setAttribute("nickName", nickName);
        request.setAttribute("userId", userId);
    }

    //token验证失败，检查被请求方法是否有LoginRequired注解，然后做相应的处理。
    private boolean checkRequestedMethodAnnotation(String token,HttpServletRequest request,HttpServletResponse response, Object handler) throws IOException {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
        if(null==methodAnnotation){
            if(null!=token){
                setNickNameOfRequest(token,request);
            }
            return true;
        }else{
            if(methodAnnotation.autoRedirect()){
                String encodedOriginalURL = URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");
                response.sendRedirect(LOGIN_URL + "?originUrl=" + encodedOriginalURL);
                return false;
            }else{
                if(null!=token){
                    setNickNameOfRequest(token,request);
                }
                return true;
            }
        }
    }
}
