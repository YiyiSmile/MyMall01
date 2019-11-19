package indi.tom.mymall01.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author Tom
 * @Date 2019/11/13 20:24
 * @Version 1.0
 * @Description
 */
@Data
@NoArgsConstructor
public class BaseCatalog1 implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
}
