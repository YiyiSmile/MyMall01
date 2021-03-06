package indi.tom.mymall01.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/19 12:40
 * @Version 1.0
 * @Description
 */
@Data
@NoArgsConstructor
public class SpuSaleAttr implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer spuId;
    @Column
    private Integer saleAttrId;
    @Column
    private String saleAttrName;

    private List<SpuSaleAttrValue> spuSaleAttrValueList;
}
