package net.jjjshop.front.vo.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("订单列表VO")
public class OrderSettledListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "settled_id", type = IdType.AUTO)
    private Integer settledId;

    @ApiModelProperty("订单号")
    private Integer orderId;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("订单金额，不包括运费")
    private BigDecimal orderMoney;

    @ApiModelProperty("支付金额")
    private BigDecimal payMoney;

    @ApiModelProperty("运费")
    private BigDecimal expressMoney;

    @ApiModelProperty("店铺金额")
    private BigDecimal supplierMoney;

    @ApiModelProperty("供应商实际结算金额")
    private BigDecimal realSupplierMoney;

    @ApiModelProperty("平台抽成")
    private BigDecimal sysMoney;

    @ApiModelProperty("平台实际结算金额")
    private BigDecimal realSysMoney;

    @ApiModelProperty("退款金额")
    private BigDecimal refundMoney;

    @ApiModelProperty("供应商退款金额")
    private BigDecimal refundSupplierMoney;

    @ApiModelProperty("平台退款结算金额")
    private BigDecimal refundSysMoney;

    @ApiModelProperty("订单号")
    private String OrderNo;

}
