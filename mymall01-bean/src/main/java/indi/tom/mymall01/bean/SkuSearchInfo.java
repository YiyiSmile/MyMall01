package indi.tom.mymall01.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/30 18:48
 * @Version 1.0
 * @Description SkuInfo for search page
 */
@Data
@NoArgsConstructor
public class SkuSearchInfo implements Serializable {
    String id;

    BigDecimal price;

    String skuName;

    String catalog3Id;

    String skuDefaultImg;

    Long hotScore=0L;

    List<SkuAttrValueSearch> skuAttrValueList;
}
