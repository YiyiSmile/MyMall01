package indi.tom.mymall01.backmanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import indi.tom.mymall01.backmanage.mapper.SkuAttrValueMapper;
import indi.tom.mymall01.backmanage.mapper.SkuImageMapper;
import indi.tom.mymall01.backmanage.mapper.SkuInfoMapper;
import indi.tom.mymall01.backmanage.mapper.SkuSaleAttrValueMapper;
import indi.tom.mymall01.bean.SkuAttrValue;
import indi.tom.mymall01.bean.SkuImage;
import indi.tom.mymall01.bean.SkuInfo;
import indi.tom.mymall01.bean.SkuSaleAttrValue;
import indi.tom.mymall01.interfaces.SkuService;
import indi.tom.mymall01.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Tom
 * @Date 2019/11/20 16:08
 * @Version 1.0
 * @Description
 */
@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    SkuImageMapper skuImageMapper;
    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Override
    @Transactional
    public String saveSku(SkuInfo skuInfo) {
        //保存sku基本信息
        skuInfoMapper.insertSelective(skuInfo);
        Integer skuId = skuInfo.getId();
        //保存sku图片表
        List<SkuImage> skuImgList = skuInfo.getSkuImgList();
        for (SkuImage skuImage : skuImgList) {
            skuImage.setSkuId(skuId);
            skuImageMapper.insert(skuImage);
        }
        //保存sku 平台属性值表
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuId);
            skuAttrValueMapper.insert(skuAttrValue);
        }

        //保存sku销售属性值表
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
            skuSaleAttrValue.setSkuId(skuId);
            skuSaleAttrValueMapper.insert(skuSaleAttrValue);
        }

        return "success";
    }

    @Override
    public String addImage(SkuImage skuImage) {
        skuImageMapper.insertSelective(skuImage);
        return "success";
    }

    @Override
    public SkuInfo getSkuInfoById(String skuId) {

        //test jedis
        Jedis jedis = redisUtil.getJedis();
        jedis.set("tom", "learning!!!");

        Integer skuId1 = Integer.parseInt(skuId);

        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId1);
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuId1);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.select(skuAttrValue);

        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId1);
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);

        SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
        skuSaleAttrValue.setSkuId(skuId1);
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuSaleAttrValueMapper.select(skuSaleAttrValue);

        skuInfo.setSkuAttrValueList(skuAttrValueList);
        skuInfo.setSkuImgList(skuImageList);
        skuInfo.setSkuSaleAttrValueList(skuSaleAttrValueList);
        return skuInfo;
    }

    @Override
    public Map getSkuValueIdsMap(String spuId) {
        List<Map> mapList = skuSaleAttrValueMapper.getSaleAttrValuesBySpu(spuId);
        Map skuValueIdsMap =new HashMap();

        for (Map  map : mapList) {
            String skuId =(Long ) map.get("sku_id") +"";
            String valueIds =(String ) map.get("value_ids");
            skuValueIdsMap.put(valueIds,skuId);


        }
        return skuValueIdsMap;
    }
}
