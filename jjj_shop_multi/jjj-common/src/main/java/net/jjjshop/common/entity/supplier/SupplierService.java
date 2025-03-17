package net.jjjshop.common.entity.supplier;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 供应商客服表
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_supplier_service")
@ApiModel(value = "SupplierService对象")
public class SupplierService implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "service_id", type = IdType.AUTO)
    private Integer serviceId;

    @ApiModelProperty("供应商用户id")
    private Integer shopSupplierId;

    @ApiModelProperty("资金流动类型 (10微信，qq等 20在线客户)")
    private Integer serviceType;

    @ApiModelProperty("微信号")
    private String wechat;

    @ApiModelProperty("qq")
    private String qq;

    @ApiModelProperty("客服电话")
    private String phone;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
