package indi.tom.mymall01.backmanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import indi.tom.mymall01.backmanage.mapper.BaseCatalog1Mapper;
import indi.tom.mymall01.backmanage.mapper.BaseCatalog2Mapper;
import indi.tom.mymall01.backmanage.mapper.BaseCatalog3Mapper;
import indi.tom.mymall01.bean.BaseCatalog1;
import indi.tom.mymall01.bean.BaseCatalog2;
import indi.tom.mymall01.bean.BaseCatalog3;
import indi.tom.mymall01.interfaces.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/13 20:43
 * @Version 1.0
 * @Description
 */
//注意这个service是com.alibaba.dubbo.config.annotation.Service
@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    BaseCatalog1Mapper baseCatalog1Mapper;
    @Autowired
    BaseCatalog2Mapper baseCatalog2Mapper;
    @Autowired
    BaseCatalog3Mapper baseCatalog3Mapper;
    @Override
    public List<BaseCatalog1> getAllCatalog1() {
        List<BaseCatalog1> baseCatalog1s = baseCatalog1Mapper.selectAll();
        return baseCatalog1s;
    }

    @Override
    public List<BaseCatalog2> getCatalog2ByCatalog1Id(int catalog1Id) {
        Example example = new Example(BaseCatalog2.class);
        example.createCriteria().andEqualTo("catalog1Id",catalog1Id);
        List<BaseCatalog2> baseCatalog2s = baseCatalog2Mapper.selectByExample(example);
        return baseCatalog2s;
    }

    @Override
    public List<BaseCatalog3> getCatalog3ByCatalog2Id(int catalog2Id) {
        Example example = new Example(BaseCatalog3.class);
        example.createCriteria().andEqualTo("catalog2Id", catalog2Id);
        List<BaseCatalog3> baseCatalog3s = baseCatalog3Mapper.selectByExample(example);
        return baseCatalog3s;
    }
}
