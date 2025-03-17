package net.jjjshop.front.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OrderPageParam对象", description = "订单分页查询参数")
public class OrderSettledPageParam extends BasePageOrderParam {

    private final static long serialVersionUID = 1L;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("开始时间")
    private String startDay;

    @ApiModelProperty("结束时间")
    private String endDay;

    @ApiModelProperty("是否结算")
    private Integer isSettled;

    @ApiModelProperty("供应商Id")
    private Integer shopSupplierId;

}
