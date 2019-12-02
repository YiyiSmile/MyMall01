package indi.tom.mymall01.backmanage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import indi.tom.mymall01.bean.SkuInfo;
import indi.tom.mymall01.bean.SkuSearchInfo;
import indi.tom.mymall01.interfaces.SkuSearchService;
import indi.tom.mymall01.interfaces.SkuService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Tom
 * @Date 2019/11/30 19:29
 * @Version 1.0
 * @Description
 */

@RestController
public class SkuControllerSearch {
    @Reference
    SkuSearchService skuSearchService;
    @Reference
    SkuService skuService;
    @PostMapping("/onsale")
    public String onSale(String skuId) throws Exception {
        SkuInfo skuInfo = null;
        skuInfo = skuService.getSkuInfoById(skuId);
        SkuSearchInfo skuSearchInfo = new SkuSearchInfo();

        BeanUtils.copyProperties(skuSearchInfo, skuInfo);
        skuSearchService.saveSkuInfoSearch(skuSearchInfo);

        return "success";
    }
}
