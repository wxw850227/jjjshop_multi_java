package net.jjjshop.common.entity.shop;

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
 * 满减设置表
 *
 * @author jjjshop
 * @since 2022-08-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_shop_fullreduce")
@ApiModel(value = "ShopFullreduce对象")
public class ShopFullreduce implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "fullreduce_id", type = IdType.AUTO)
    private Integer fullreduceId;

    @ApiModelProperty("活动名称")
    private String activeName;

    @ApiModelProperty("满类型1，满额2，满件数")
    private Integer fullType;

    @ApiModelProperty("满值")
    private Integer fullValue;

    @ApiModelProperty("减类型，1，减金额 2，打折")
    private Integer reduceType;

    @ApiModelProperty("减值")
    private Integer reduceValue;

    @ApiModelProperty("商品id")
    private Integer productId;

    @ApiModelProperty("0=显示1=伪删除")
    private Integer isDelete;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("供应商Id")
    private Integer shopSupplierId;

}
