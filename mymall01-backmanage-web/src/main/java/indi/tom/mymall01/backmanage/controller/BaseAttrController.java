package indi.tom.mymall01.backmanage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.org.apache.xml.internal.resolver.Catalog;
import indi.tom.mymall01.bean.BaseAttrInfo;
import indi.tom.mymall01.bean.Msg;
import indi.tom.mymall01.interfaces.BaseAttrService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/14 14:02
 * @Version 1.0
 * @Description 平台属性控制器
 */
@RestController
@CrossOrigin
public class BaseAttrController {
    @Reference
    BaseAttrService baseAttrService;

    //根据三级分类查询平台属性以及属性值
    @GetMapping("attrInfoList")
    public List<BaseAttrInfo> getAttrInfoListByCatalog3Id(String catalog3Id){
        List<BaseAttrInfo> list = baseAttrService.getBaseAttrByCatalog3Id(catalog3Id);
        return list;
    }
    @PostMapping("addAttrInfo")
    public Msg addAttrInfoWithAttrInfoValues(@RequestBody BaseAttrInfo baseAttrInfo){
        System.out.println(baseAttrInfo);
        baseAttrService.addBaseAttr(baseAttrInfo);

        return Msg.success();
    }
}
