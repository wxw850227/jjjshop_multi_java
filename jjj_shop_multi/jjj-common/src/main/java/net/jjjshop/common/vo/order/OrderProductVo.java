package net.jjjshop.common.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.OrderProduct;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("订单VO")
public class OrderProductVo extends OrderProduct {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品图片")
    private String imagePath;

    @ApiModelProperty("订单创建时间")
    private String orderCreateTime;

    @ApiModelProperty("订单创建时间")
    private Boolean refund;

    @ApiModelProperty("售后单类型")
    private Integer refundType;

    @ApiModelProperty("是否同意售后单")
    private Integer isAgree;
}
