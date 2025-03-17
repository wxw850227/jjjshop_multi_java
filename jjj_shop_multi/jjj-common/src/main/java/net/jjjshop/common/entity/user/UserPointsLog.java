package net.jjjshop.common.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户积分变动明细表
 *
 * @author jjjshop
 * @since 2022-07-21
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_user_points_log")
@ApiModel(value = "UserPointsLog对象")
public class UserPointsLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("变动数量")
    private Integer value;

    @ApiModelProperty("描述/说明")
    private String description;

    @ApiModelProperty("管理员备注")
    private String remark;

    @ApiModelProperty("小程序商城id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
