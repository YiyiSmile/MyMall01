package indi.tom.mymall01.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author Tom
 * @Date 2019/11/19 12:43
 * @Version 1.0
 * @Description
 */
@Data
@NoArgsConstructor
public class SpuSaleAttrValue implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer spuId;
    @Column
    private Integer saleAttrId;
    @Column
    private String saleAttrValueName;

    @Transient
    String isChecked;

}
