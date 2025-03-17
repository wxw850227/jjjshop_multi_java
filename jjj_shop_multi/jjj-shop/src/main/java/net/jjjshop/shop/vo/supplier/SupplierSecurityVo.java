package net.jjjshop.shop.vo.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.supplier.SupplierCash;
import net.jjjshop.common.entity.supplier.SupplierServiceSecurity;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户服务VO")
public class SupplierSecurityVo extends SupplierServiceSecurity {
    private static final long serialVersionUID = 1L;
}
