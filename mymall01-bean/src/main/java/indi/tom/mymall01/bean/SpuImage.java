package indi.tom.mymall01.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Author Tom
 * @Date 2019/11/19 12:38
 * @Version 1.0
 * @Description
 */
@Data
@NoArgsConstructor
public class SpuImage implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer spuId;
    @Column
    private String imgName;
    @Column
    private String imgUrl;

}
