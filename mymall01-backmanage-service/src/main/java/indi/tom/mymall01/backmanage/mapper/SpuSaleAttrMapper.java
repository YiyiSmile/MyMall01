package indi.tom.mymall01.backmanage.mapper;

import indi.tom.mymall01.bean.SpuSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {
    List<SpuSaleAttr> getSpuSaleAttrListBySpuId(String spuId);
    List<SpuSaleAttr> getSpuSaleAttrListBySpuIdCheckSku(@Param("skuId") String skuId, @Param("spuId")  String spuId);

}
