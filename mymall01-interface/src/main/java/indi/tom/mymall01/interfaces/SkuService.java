package indi.tom.mymall01.interfaces;

import indi.tom.mymall01.bean.SkuImage;
import indi.tom.mymall01.bean.SkuInfo;

import java.util.Map;

/**
 * @Author Tom
 * @Date 2019/11/20 16:07
 * @Version 1.0
 * @Description
 */
public interface SkuService {
    String saveSku(SkuInfo skuInfo);
    String addImage(SkuImage skuImage);
    SkuInfo getSkuInfoByIdDB(String skuId);
    Map getSkuValueIdsMap(String spuId);
    SkuInfo getSkuInfoByIdRedis(String skuId) throws Exception;
    SkuInfo getSkuInfoById(String skuId) throws Exception;
}
