package com.alag.amall.business.core.page;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Consumer;

@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
public class ParamBean implements Serializable {
    private String messageId;
    private Integer status;
    private Integer areadlyDead;
    private String consumerQueue;
    private Date createTimeBefore;
    private String listPageSortType;

    private Integer pageNum;
    private Integer pageSize;

    public static ParamBean newParamBean() {
        return new ParamBean();
    }

    public void set(Consumer<ParamBean> paramBeanConsumer) {
        paramBeanConsumer.accept(this);
    }
}
