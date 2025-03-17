package net.jjjshop.supplier.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.OrderSettled;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("订单详情VO")
public class OrderSettledVo extends OrderSettled {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单详情")
    private OrderVo orderMaster;

}
