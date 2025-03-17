package net.jjjshop.supplier.param.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "SupplierServiceApplyParam对象", description = "商户服务申请对象")
public class SupplierServiceApplyParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer serviceSecurityId;

    @ApiModelProperty("供应商Id")
    private Integer shopSupplierId;

    @ApiModelProperty("审核状态")
    private Integer status;
}
