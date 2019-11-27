package indi.tom.mymall01.bean;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Author Tom
 * @Date 2019/11/20 15:48
 * @Version 1.0
 * @Description
 */
@Data
@NoArgsConstructor
public class SkuImage implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer skuId;
    @Column
    private String imgName;
    @Column
    private String imgUrl;
    @Column
    private Integer spuImgId;
    @Column
    private String isDefault;
}
