package indi.tom.mymall01.backmanage.mapper;

import indi.tom.mymall01.bean.BaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BaseAttrMapper extends Mapper<BaseAttrInfo> {
    List<BaseAttrInfo> getBaseAttrByCatalog3Id(String catalog3Id);
    List<BaseAttrInfo> getBaseAttrByBaseAttrValueIds(@Param("baseAttrValueIds") String baseAttrValueIds);
}
