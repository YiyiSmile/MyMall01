package indi.tom.mymall01.backmanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import indi.tom.mymall01.backmanage.mapper.*;
import indi.tom.mymall01.bean.*;
import indi.tom.mymall01.interfaces.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/19 8:26
 * @Version 1.0
 * @Description
 */
@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;
    @Autowired
    SpuInfoMapper spuInfoMapper;
    @Autowired
    SpuImageMapper spuImageMapper;
    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Override
    public List<BaseSaleAttr> getAllBaseSaleAttr() {
        return baseSaleAttrMapper.selectAll();
    }

    @Override
    public void saveSpu(SpuInfo spuInfo) {

        //保存spu
        spuInfoMapper.insert(spuInfo);
        //保存image信息
        Integer spuId = spuInfo.getId();
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        for (SpuImage spuImage : spuImageList) {
            spuImage.setSpuId(spuId);
            spuImageMapper.insert(spuImage);
        }
        //保存销售属性信息
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
            spuSaleAttr.setSpuId(spuId);
            spuSaleAttrMapper.insert(spuSaleAttr);
            //保存销售属性值信息
            List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                spuSaleAttrValue.setSpuId(spuId);
                spuSaleAttrValueMapper.insert(spuSaleAttrValue);
            }
        }


    }

    @Override
    public List<SpuImage> getSpuImageList(String spuId) {
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(Integer.parseInt(spuId));
        return spuImageMapper.select(spuImage);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListBySpuId(String spuId) {
/*        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(Integer.parseInt(spuId));
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.select(spuSaleAttr);
        //这种循环语句中执行sql不好，如果循环次数多，导致频繁调用sql,要改成联合查询，用一次sql，查询获取所有结果。
        for (SpuSaleAttr saleAttr : spuSaleAttrList) {
            SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
            spuSaleAttrValue.setSaleAttrId(saleAttr.getSaleAttrId());
            spuSaleAttrValue.setSpuId(Integer.parseInt(spuId));
            List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttrValueMapper.select(spuSaleAttrValue);
            saleAttr.setSpuSaleAttrValueList(spuSaleAttrValueList);
        }
        return spuSaleAttrList;*/

        //使用联合查询，减少查询次数
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.getSpuSaleAttrListBySpuId(spuId);
        return spuSaleAttrList;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckSku(String skuId, String spuId) {
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.getSpuSaleAttrListBySpuIdCheckSku(skuId, spuId);
        return spuSaleAttrList;
    }
}
