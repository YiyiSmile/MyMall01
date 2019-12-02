package indi.tom.mymall01.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author Tom
 * @Date 2019/11/30 18:50
 * @Version 1.0
 * @Description
 */
@Data
@NoArgsConstructor
public class SkuAttrValueSearch implements Serializable {
    private String valueId;
}
