package net.jjjshop.shop.vo.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.supplier.SupplierCash;
import net.jjjshop.common.entity.supplier.SupplierServiceApply;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户服务申请VO")
public class SupplierServiceApplyVo extends SupplierServiceApply {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("服务名称")
    private String serverName;

    @ApiModelProperty("服务logo")
    private String serverLogo;
}
