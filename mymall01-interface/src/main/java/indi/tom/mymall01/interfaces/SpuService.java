package indi.tom.mymall01.interfaces;

import indi.tom.mymall01.bean.BaseSaleAttr;
import indi.tom.mymall01.bean.SpuImage;
import indi.tom.mymall01.bean.SpuInfo;
import indi.tom.mymall01.bean.SpuSaleAttr;

import java.util.List;

public interface SpuService {
    List<BaseSaleAttr> getAllBaseSaleAttr();

    void saveSpu(SpuInfo spuInfo);

    List<SpuImage> getSpuImageList(String spuId);

    List<SpuSaleAttr> getSpuSaleAttrListBySpuId(String spuId);

    List<SpuSaleAttr> getSpuSaleAttrListCheckSku(String skuId, String spuId);
}
