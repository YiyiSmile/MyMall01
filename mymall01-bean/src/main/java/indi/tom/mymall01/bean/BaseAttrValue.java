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
 * @Date 2019/11/14 14:09
 * @Version 1.0
 * @Description 平台属性值，一个平台属性对应多个值
 */

@Data
@NoArgsConstructor
public class BaseAttrValue implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String valueName;
    @Column
    private Integer attrId;
    @Column
    private boolean isEnabled;
}
