package indi.tom.mymall01.searchweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import indi.tom.mymall01.bean.BaseAttrInfo;
import indi.tom.mymall01.bean.BaseAttrValue;
import indi.tom.mymall01.bean.SkuSearchParam;
import indi.tom.mymall01.bean.SkuSearchResult;
import indi.tom.mymall01.interfaces.BaseAttrService;
import indi.tom.mymall01.interfaces.SkuSearchService;
import jdk.nashorn.internal.runtime.arrays.IteratorAction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author Tom
 * @Date 2019/12/2 20:57
 * @Version 1.0
 * @Description
 */
@Controller
public class SkuSearchController {

    @Reference
    SkuSearchService skuSearchService;

    @Reference
    BaseAttrService baseAttrService;
    @GetMapping("list.html")
    public String searchSkus(SkuSearchParam skuSearchParam, Model model){
        //获取当前请求URL;
        System.out.println("hello");
        System.out.println(skuSearchParam);
        String currentParamUrl = getParamUrl(skuSearchParam);
        SkuSearchResult skuSearchResult = skuSearchService.getSkuSearchResult(skuSearchParam);
        List<String> attrValueIdList = skuSearchResult.getAttrValueIdList();
        if(attrValueIdList.size() == 0) {
            return "list";
        }
        List<BaseAttrInfo> baseAttrList = baseAttrService.getBaseAttrListByBaseAttrValueIds(attrValueIdList);
        //从要返回的平台属性列表里边删除客户端传过来的平台属性值所对应的平台属性（客户已选中的平台属性值）。
        String[] selectedAttrValueIds = skuSearchParam.getValueId();
        //面包屑(已经选中的平台属性值)
        List<BaseAttrValue> selectedAttrValueList = new ArrayList<>();
        if(selectedAttrValueIds != null && selectedAttrValueIds.length > 0) {
            for (int i = 0; i < selectedAttrValueIds.length; i++) {
                String selectedAttrValueId = selectedAttrValueIds[i];
                for (Iterator<BaseAttrInfo> iterator = baseAttrList.iterator(); iterator.hasNext(); ) {
                    BaseAttrInfo baseAttrInfo = iterator.next();
                    List<BaseAttrValue> valueList = baseAttrInfo.getList();
                    for (BaseAttrValue baseAttrValue : valueList) {
                        if(baseAttrValue.getId().equals(Integer.parseInt(selectedAttrValueId))){
                            //生成已选中的平台属性值
                            BaseAttrValue selectedAttrValue = new BaseAttrValue();
                            selectedAttrValue.setId(Integer.parseInt(selectedAttrValueId));
                            selectedAttrValue.setValueName(baseAttrValue.getValueName());
                            selectedAttrValue.setUrlParam(getParamUrl(skuSearchParam, selectedAttrValueId));
                            selectedAttrValueList.add(selectedAttrValue);
                            //从要返回的平台属性列表里边删除客户端传过来的平台属性值所对应的平台属性（客户已选中的平台属性值）。
                            iterator.remove();
                            break;
                        }
                    }

                }

            }
        }

        model.addAttribute("totalPages", skuSearchResult.getTotalPages());
        model.addAttribute("pageNo", skuSearchParam.getPageNo());

        model.addAttribute("currentParamUrl",currentParamUrl);
        model.addAttribute("selectedAttrValueList",selectedAttrValueList);
        model.addAttribute("baseAttrList",baseAttrList);
        model.addAttribute("skuSearchResult", skuSearchResult);
        model.addAttribute("keyword", skuSearchParam.getKeyword());

        return "list";
    }

    /**
     * 根据SkuSearchParam还原当前 请求url
     * @param skuSearchParam
     * @param selectedValueId
     * @return
     */
    public String getParamUrl(SkuSearchParam skuSearchParam, String ... selectedValueId){
        String keyword = skuSearchParam.getKeyword();
        String catalog3Id = skuSearchParam.getCatalog3Id();
        String[] valueId = skuSearchParam.getValueId();
        int pageNo = skuSearchParam.getPageNo();
        int pageSize = skuSearchParam.getPageSize();
        //请求url
        String paramUrl = "";
        if(keyword != null && ! keyword.equals("")){
            paramUrl += "keyword=" + keyword;
        }
        //另外一种判断空串的方式
        if(StringUtils.isNotBlank(catalog3Id)){
            if(paramUrl.equals("")){
                paramUrl += "catalog3Id=" + catalog3Id;
            }else{
                paramUrl += "&catalog3Id=" + catalog3Id;
            }
        }

        if(valueId != null && valueId.length > 0){
            for (int i = 0; i < valueId.length; i++) {
                String valueId_String = valueId[i];
                if(selectedValueId != null && selectedValueId.length > 0) {
                    if (valueId_String.equals(selectedValueId[0])) {
                        continue;
                    }
                }
                if(paramUrl.equals("")){
                    paramUrl += "valueId=" + valueId_String;
                }else{
                    paramUrl += "&valueId=" + valueId_String;
                }
            }
        }

        //paramUrl += pageNo + pageSize;
        return paramUrl;
    }
}
