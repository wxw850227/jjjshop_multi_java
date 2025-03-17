package net.jjjshop.shop.param.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SupplierCashPageParam对象", description = "SupplierCashPageParam商户提现分页参数对象")
public class SupplierCashPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("搜索栏内容")
    private String search;

    @ApiModelProperty("搜索栏审核状态")
    private Integer applyStatus;

    @ApiModelProperty("搜索栏支付类型")
    private Integer payType;
}
