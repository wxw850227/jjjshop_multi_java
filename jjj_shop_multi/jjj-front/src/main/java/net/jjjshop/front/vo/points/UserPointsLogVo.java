package net.jjjshop.front.vo.points;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("用户积分明细VO")
public class UserPointsLogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("日志Id")
    private Integer logId;

    @ApiModelProperty("用户Id")
    private Integer userId;

    @ApiModelProperty("用户积分")
    private Integer value;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("管理员备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
