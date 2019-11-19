package indi.tom.mymall01.interfaces;

import indi.tom.mymall01.bean.BaseAttrInfo;

import java.util.List;

public interface BaseAttrService {
    List<BaseAttrInfo> getBaseAttrByCatalog3Id(String catalog3Id);
    void addBaseAttr(BaseAttrInfo baseAttrInfo);
    void modifyBaseAttr(BaseAttrInfo baseAttrInfo);

}
