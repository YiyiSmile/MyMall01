package indi.tom.mymall01.interfaces;

import indi.tom.mymall01.bean.BaseCatalog1;
import indi.tom.mymall01.bean.BaseCatalog2;
import indi.tom.mymall01.bean.BaseCatalog3;

import java.util.List;

public interface CatalogService {
    List<BaseCatalog1> getAllCatalog1();

    List<BaseCatalog2> getCatalog2ByCatalog1Id(int catalog1Id);

    List<BaseCatalog3> getCatalog3ByCatalog2Id(int catalog2Id);

}
