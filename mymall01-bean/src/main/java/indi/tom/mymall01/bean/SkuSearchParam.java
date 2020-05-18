package indi.tom.mymall01.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author Tom
 * @Date 2019/12/2 19:39
 * @Version 1.0
 * @Description
 */
@Data
public class SkuSearchParam implements Serializable {
    String  keyword;

    String catalog3Id;

    String[] valueId;

    int pageNo=1;

    int pageSize=5;
}
