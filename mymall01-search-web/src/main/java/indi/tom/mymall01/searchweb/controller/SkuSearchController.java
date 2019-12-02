package indi.tom.mymall01.searchweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import indi.tom.mymall01.bean.SkuSearchParam;
import indi.tom.mymall01.bean.SkuSearchResult;
import indi.tom.mymall01.interfaces.SkuSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Tom
 * @Date 2019/12/2 20:57
 * @Version 1.0
 * @Description
 */
@RestController
public class SkuSearchController {

    @Reference
    SkuSearchService skuSearchService;
    @GetMapping("searchSkus")
    public SkuSearchResult searchSkus(SkuSearchParam skuSearchParam){
        SkuSearchResult skuSearchResult = skuSearchService.getSkuSearchResult(skuSearchParam);
        return skuSearchResult;
    }
}
