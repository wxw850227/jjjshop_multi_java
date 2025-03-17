package net.jjjshop.supplier.param.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SupplierCapitalPageParam列表", description = "商户资金明细分页参数对象")
public class SupplierCapitalPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("搜索栏内容")
    private Integer flowType;

    @ApiModelProperty("搜索栏开始时间")
    private String startDay;

    @ApiModelProperty("搜索栏结束时间")
    private String endDay;
}
