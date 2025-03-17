package net.jjjshop.shop.param.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "SupplierApplyParam对象", description = "供应商申请对象")
public class SupplierApplyParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer supplierApplyId;

    @ApiModelProperty("0待审核1审核通过2未通过")
    private Integer status;

    @ApiModelProperty("审核备注")
    private String content;
}
