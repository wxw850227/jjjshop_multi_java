package net.jjjshop.common.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户访问记录
 *
 * @author jjjshop
 * @since 2022-10-25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_user_visit")
@ApiModel(value = "UserVisit对象")
public class UserVisit implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "visit_id", type = IdType.AUTO)
    private Integer visitId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("商品id")
    private Integer productId;

    @ApiModelProperty("访问内容")
    private String content;

    @ApiModelProperty("访客id")
    private String visitcode;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

}
