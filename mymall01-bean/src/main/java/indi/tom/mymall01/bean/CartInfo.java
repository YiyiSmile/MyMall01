package indi.tom.mymall01.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @Author Tom
 * @Date 2020/5/19 14:30
 * @Version 1.0
 * @Description
 */
@Data
@NoArgsConstructor
public class CartInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;
    @Column
    private String userId;
    @Column
    private String skuId;
    @Column
    private BigDecimal cartPrice;
//    private Integer quantity;
    @Column
    private String imgUrl;
    @Column
    private String skuName;
    @Column
    private Integer skuNum;
    //下单的时候判断是否选中
    @Transient
    private Boolean isChecked;
    //实时价格
    @Transient
    private BigDecimal skuPrice;
}
