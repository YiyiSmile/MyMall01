package indi.tom.mymall01.interfaces;

import indi.tom.mymall01.bean.SkuSearchInfo;
import indi.tom.mymall01.bean.SkuSearchParam;
import indi.tom.mymall01.bean.SkuSearchResult;

public interface SkuSearchService {
    void saveSkuInfoSearch(SkuSearchInfo skuSearchInfo);
    SkuSearchResult getSkuSearchResult(SkuSearchParam skuSearchParam);
}
