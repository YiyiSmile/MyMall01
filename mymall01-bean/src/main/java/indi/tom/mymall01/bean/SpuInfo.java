package indi.tom.mymall01.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/19 12:35
 * @Version 1.0
 * @Description
 */
@Data
@NoArgsConstructor
public class SpuInfo implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String spuName;
    @Column
    private String description;
    @Column
    private Integer catalog3Id;

    @Transient
    private List<SpuImage> spuImageList;

    @Transient
    private  List<SpuSaleAttr> spuSaleAttrList;
}
