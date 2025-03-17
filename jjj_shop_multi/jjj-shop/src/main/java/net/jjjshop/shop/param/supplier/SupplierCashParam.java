package net.jjjshop.shop.param.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel(value = "SupplierCashParam对象", description = "商户提现参数对象")
public class SupplierCashParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("金额")
    private BigDecimal money;

    @ApiModelProperty("审核状态")
    private Integer applyStatus;

    @ApiModelProperty("拒绝理由")
    private String rejectReason;

    @ApiModelProperty("供应商Id")
    private String shopSupplierId;
}
