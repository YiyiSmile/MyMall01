package indi.tom.mymall01.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/20 15:35
 * @Version 1.0
 * @Description
 */
@Data
@NoArgsConstructor
public class SkuInfo implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer spuId;
    @Column
    private Double price;
    @Column
    private String skuName;
    @Column
    private String skuDesc;
    @Column
    private Double weight;
    @Column
    private Integer catalog3Id;
    @Column
    private String skuDefaultImg;

    @Transient
    List<SkuAttrValue> skuAttrValueList;
    @Transient
    List<SkuImage> skuImgList;
    @Transient
    List<SkuSaleAttrValue> skuSaleAttrValueList;
}
