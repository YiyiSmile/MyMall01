package indi.tom.mymall01.backmanage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import indi.tom.mymall01.bean.BaseSaleAttr;
import indi.tom.mymall01.bean.SpuImage;
import indi.tom.mymall01.bean.SpuInfo;
import indi.tom.mymall01.interfaces.SpuService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/19 8:18
 * @Version 1.0
 * @Description
 */
@RestController
public class SpuController {
    @Reference
    SpuService spuService;

    @GetMapping("getAllBaseSaleAttr")
    public List<BaseSaleAttr> getBaseSaleAttr(){
        return spuService.getAllBaseSaleAttr();
    }

    @PostMapping("saveSpu")
    public String saveSpu(@RequestBody SpuInfo spuInfo){
        spuService.saveSpu(spuInfo);
        return "success";
    }

    //根据spuId查询图片列表
    @GetMapping("spuImageList")
    public List<SpuImage> getSpuImageList(String spuId){
        return spuService.getSpuImageList(spuId);
    }
}
