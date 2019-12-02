package indi.tom.mymall01.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Tom
 * @Date 2019/12/2 19:44
 * @Version 1.0
 * @Description
 */
@Data
public class SkuSearchResult implements Serializable {

    List<SkuSearchInfo> skuSearchInfoList;

    long total;

    long totalPages;

    List<String> attrValueIdList;

}
