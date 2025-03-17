package net.jjjshop.shop.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "用户等级查询参数", description = "用户等级查询参数")
public class UserCashParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("申请状态 (10待审核 20审核通过 30驳回 40已打款)")
    private Integer applyStatus;

    @ApiModelProperty("驳回原因")
    private String rejectReason;
}
