package indi.tom.mymall01.backmanage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import indi.tom.mymall01.bean.SkuImage;
import indi.tom.mymall01.bean.SkuInfo;
import indi.tom.mymall01.interfaces.SkuService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Tom
 * @Date 2019/11/20 15:33
 * @Version 1.0
 * @Description
 */
@RestController
public class skuController {
    @Reference
    SkuService skuService;
    @PostMapping("saveSku")
    public String addSku(@RequestBody SkuInfo skuInfo){
        skuService.saveSku(skuInfo);
        return "success";

    }

    @PostMapping("insertImage")
    public String addImage(@RequestBody SkuImage skuImage){
        skuService.addImage(skuImage);
        return "success";
    }
}
