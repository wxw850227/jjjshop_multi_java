package net.jjjshop.shop.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

import java.util.Date;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "积分明细分页对象", description = "积分明细分页对象")
public class UserPointsLogPageParam extends BasePageOrderParam {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("开始时间")
    private Date startDate;

    @ApiModelProperty("场景")
    private Date endDate;

}
