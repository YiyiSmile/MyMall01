package indi.tom.mymall01.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import indi.tom.mymall01.annotation.LoginRequired;
import indi.tom.mymall01.bean.SkuInfo;
import indi.tom.mymall01.bean.SpuInfo;
import indi.tom.mymall01.bean.SpuSaleAttr;
import indi.tom.mymall01.interfaces.SkuService;
import indi.tom.mymall01.interfaces.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.beans.DesignMode;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @Author Tom
 * @Date 2019/11/26 12:31
 * @Version 1.0
 * @Description
 */
@Controller
public class SkuController {
    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;
    @GetMapping("/{skuId}.html")
//    @LoginRequired
    public String getSkuInfoById(@PathVariable("skuId") String skuId, HttpServletRequest request) throws UnsupportedEncodingException {
        SkuInfo skuInfo = null;
        try {
            skuInfo = skuService.getSkuInfoById(skuId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("skuInfo", skuInfo);

        //获取spu销售属性以及属性值
        Integer spuId = skuInfo.getSpuId();
        List<SpuSaleAttr> spuSaleAttrList = spuService.getSpuSaleAttrListCheckSku(skuId, spuId+"");
        System.out.println("----------");
        System.out.println(spuSaleAttrList);
        request.setAttribute("spuSaleAttrList", spuSaleAttrList);

        //得到属性组合与skuid的映射关系 ，用于页面根据属性组合进行跳转
        Map skuValueIdsMap = skuService.getSkuValueIdsMap(skuInfo.getSpuId()+"");
        String valuesSkuJson = JSON.toJSONString(skuValueIdsMap);
        System.out.println(valuesSkuJson);
        request.setAttribute("valuesSkuJson",valuesSkuJson);


        String originUrl= URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");
        request.setAttribute("originUrl", originUrl);


        return "item";
    }
}
