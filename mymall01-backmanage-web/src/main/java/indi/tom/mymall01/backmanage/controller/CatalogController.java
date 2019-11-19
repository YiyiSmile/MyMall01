package indi.tom.mymall01.backmanage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import indi.tom.mymall01.bean.BaseCatalog1;
import indi.tom.mymall01.bean.BaseCatalog2;
import indi.tom.mymall01.bean.BaseCatalog3;
import indi.tom.mymall01.interfaces.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/13 21:14
 * @Version 1.0
 * @Description 三级分类控制器
 */
@RestController
public class CatalogController {

    @Reference
    CatalogService catalogService;

    @GetMapping("allCatalog1")
    public List<BaseCatalog1> getAllCatalog1(){
        List<BaseCatalog1> allCatalog1 = catalogService.getAllCatalog1();
        return allCatalog1;
    }

    @GetMapping("catalog2")
    public List<BaseCatalog2> getCatalog2ByCatalog1Id(String catalog1Id){
        List<BaseCatalog2> catalog2ByCatalog1Id = catalogService.getCatalog2ByCatalog1Id(Integer.parseInt(catalog1Id));
        return catalog2ByCatalog1Id;
    }

    @GetMapping("catalog3")
    public List<BaseCatalog3> getCatalog3ByCatalog2Id(String catalog2Id){
        List<BaseCatalog3> catalog3ByCatalog2Id = catalogService.getCatalog3ByCatalog2Id(Integer.parseInt(catalog2Id));
        return catalog3ByCatalog2Id;
    }


}
