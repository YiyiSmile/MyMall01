package indi.tom.mymall01.passportweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import indi.tom.mymall01.bean.UserInfo;
import indi.tom.mymall01.interfaces.UserService;
import indi.tom.mymall01.util.JwtUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Tom
 * @Date 2020/5/17 16:41
 * @Version 1.0
 * @Description
 */
@Controller
public class PassportController {

    @Reference
    UserService userService;

    private static final String JWT_KEY = "tom098";

    @GetMapping("index")
    public String login(HttpServletRequest request, Model model) {
        String originUrl = request.getParameter("originUrl");
        model.addAttribute("originUrl", originUrl);
        return "index";
    }

    @PostMapping("login")
    @ResponseBody
    public String login(UserInfo userInfo, HttpServletRequest request) {
        //1. Call the user service to see if user exists, return the userinfo
        UserInfo userInfoReturn = userService.login(userInfo);
        //2. if user exists, generate the token
        //return token if success, else return "fail"
        if (userInfoReturn != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userInfoReturn.getId());
            map.put("nickName", userInfoReturn.getNickName());

// 如果有 反向代理的话  ，要在反向代理中进行配置  把用户真实ip传递过来

//            String ipAddr = request.getHeader("X-forwarded-for");
            String ipAddr = request.getRemoteAddr();
            String token = JwtUtil.encode(JWT_KEY, map, ipAddr);
            return token;
        } else {
            return "fail";
        }

    }

    @GetMapping("verify")
    @ResponseBody
    public String verifyToken(String token, String ipAddr){
        System.out.println("token ---" + token);
        System.out.println("ipAddr ---" + ipAddr);
        //调用jwtutil解码
        Map<String, Object> userInfoDecoded = JwtUtil.decode(token, JWT_KEY, ipAddr);
        if(null == userInfoDecoded){
            return "fail";
        }else{
            //调用后台服务校验token, 成功返回 ture,失败返回false
            Boolean isUserExistsInCache = userService.verify(userInfoDecoded);
            if(isUserExistsInCache){
                return "success";
            }else{
                return "fail";
            }
        }
    }


    }

