package indi.tom.mymall01.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/14 14:04
 * @Version 1.0
 * @Description
 */
@Data
@NoArgsConstructor
public class BaseAttrInfo implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String attrName;
    @Column
    private Integer catalog3Id;
    @Column
    private boolean isEnabled;

    @Transient
    private List<BaseAttrValue> list;

}
