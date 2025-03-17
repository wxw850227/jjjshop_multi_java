package net.jjjshop.shop.param.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

import java.util.Date;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "积分明细分页对象", description = "积分明细分页对象")
public class UserSignPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    private Integer days;
    private Integer signDate;
    private String nickName;
    private Date startDate;
    private Date endDate;
}
