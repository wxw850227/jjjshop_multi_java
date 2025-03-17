package net.jjjshop.common.entity.supplier;

import java.math.BigDecimal;
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
 * 供应商提现明细表
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_supplier_cash")
@ApiModel(value = "SupplierCash对象")
public class SupplierCash implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("供应商用户id")
    private Integer shopSupplierId;

    @ApiModelProperty("提现金额")
    private BigDecimal money;

    @ApiModelProperty("打款方式 (10支付宝 20银行卡)")
    private Integer payType;

    @ApiModelProperty("申请状态 (10待审核 20审核通过 30驳回 40已打款)")
    private Integer applyStatus;

    @ApiModelProperty("审核时间")
    private Date auditTime;

    @ApiModelProperty("驳回原因")
    private String rejectReason;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
