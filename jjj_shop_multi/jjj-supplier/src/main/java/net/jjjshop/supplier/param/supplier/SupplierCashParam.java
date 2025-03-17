package net.jjjshop.supplier.param.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel(value = "SupplierCashParam对象", description = "商户提现参数对象")
public class SupplierCashParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("提现金额")
    private BigDecimal money;

    @ApiModelProperty("提现种类")
    private Integer payType;

}
