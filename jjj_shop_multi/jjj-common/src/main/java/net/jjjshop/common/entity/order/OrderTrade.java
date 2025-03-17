package net.jjjshop.common.entity.order;

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
 * 外部交易号跟内部订单对应关系表
 *
 * @author jjjshop
 * @since 2022-11-03
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_order_trade")
@ApiModel(value = "OrderTrade对象")
public class OrderTrade implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("外部交易号")
    private String outTradeNo;

    @ApiModelProperty("订单号")
    private Integer orderId;

    private Date createTime;

    private Date updateTime;

    private Integer appId;

    @ApiModelProperty("支付状态10,未支付,20已支付")
    private Integer payStatus;

    @ApiModelProperty("支付时间")
    private Date payTime;

    @ApiModelProperty("余额抵扣金额")
    private BigDecimal balance;

    @ApiModelProperty("在线支付金额")
    private BigDecimal onlineMoney;

}
