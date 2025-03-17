package net.jjjshop.supplier.vo.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.supplier.SupplierCapital;
import net.jjjshop.common.entity.supplier.SupplierCash;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户资金明细Vo")
public class SupplierCapitalVo extends SupplierCapital {
    private static final long serialVersionUID = 1L;

}
