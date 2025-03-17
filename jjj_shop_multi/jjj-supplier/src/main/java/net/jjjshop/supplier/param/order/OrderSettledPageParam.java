package net.jjjshop.supplier.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

import java.util.Date;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OrderRefundPageParam对象", description = "售后单分页参数")
public class OrderSettledPageParam extends BasePageOrderParam {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("搜索订单号")
    private String orderNo;

    @ApiModelProperty("查询条件：起始日期")
    private String startDay;

    @ApiModelProperty("查询条件：结束日期")
    private String endDay;

    @ApiModelProperty("售后类型")
    private Integer isSettled;

}
