package indi.tom.mymall01.backmanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import indi.tom.mymall01.backmanage.mapper.BaseAttrMapper;
import indi.tom.mymall01.backmanage.mapper.BaseAttrValueMapper;
import indi.tom.mymall01.bean.BaseAttrInfo;
import indi.tom.mymall01.bean.BaseAttrValue;
import indi.tom.mymall01.interfaces.BaseAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/14 14:22
 * @Version 1.0
 * @Description 平台属性查询
 */
@Service
public class BaseAttrServiceImpl implements BaseAttrService {
    @Autowired
    BaseAttrMapper baseAttrMapper;
    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;
    @Override
    public List<BaseAttrInfo> getBaseAttrByCatalog3Id(String catalog3Id) {
/*        Example example = new Example(BaseAttrInfo.class);
        example.createCriteria().andEqualTo("catalog3Id", Integer.parseInt(catalog3Id));
        List<BaseAttrInfo> baseAttrInfos = baseAttrMapper.selectByExample(example);
        //获取平台属性值
        //循环查询，开发中尽量不要使用。开发中尽量使用大sql，这样不用重复简历链接
        //除非大sql查询的数据非常多，比如10000条，我们可以先查出1000条，再循环查，再组合
        //通用mapper解决不了多表关联查询，也解决不了groupby的问题
        //如果使用多表关联查询，执行一条sql语句就得到所有结果。比如有1000个平台属性，就需要查一千次平台属性表。
        for (BaseAttrInfo baseAttrInfo : baseAttrInfos) {
            BaseAttrValue baseAttrValue = new BaseAttrValue();
            baseAttrValue.setAttrId(baseAttrInfo.getId());

            List<BaseAttrValue> list = baseAttrValueMapper.select(baseAttrValue);
            baseAttrInfo.setList(list);
        }
        return baseAttrInfos;*/

        //自定义sql语句来查询
        List<BaseAttrInfo> baseAttrInfoList = baseAttrMapper.getBaseAttrByCatalog3Id(catalog3Id);
        return baseAttrInfoList;

    }

    @Override
    public void addBaseAttr(BaseAttrInfo baseAttrInfo) {
        baseAttrMapper.insert(baseAttrInfo);
        int id = baseAttrInfo.getId();
        List<BaseAttrValue> list = baseAttrInfo.getList();
        for (BaseAttrValue baseAttrValue : list) {
            baseAttrValue.setAttrId(id);
            baseAttrValueMapper.insert(baseAttrValue);
        }
    }

    @Override
    public void modifyBaseAttr(BaseAttrInfo baseAttrInfo) {

    }
}
