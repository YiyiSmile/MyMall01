package indi.tom.mymall01.bean;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Author Tom
 * @Date 2019/11/20 15:59
 * @Version 1.0
 * @Description
 */
@Data
@NoArgsConstructor
public class SkuSaleAttrValue implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer skuId;
    @Column
    private Integer saleAttrId;
    @Column
    private Integer saleAttrValueId;
    @Column
    private String saleAttrName;
    @Column
    private String saleAttrValueName;
}
