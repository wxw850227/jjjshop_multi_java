package net.jjjshop.supplier.vo.supplier;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.supplier.SupplierServiceSecurity;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户保障服务Vo")
public class SupplierSecurityVo extends SupplierServiceSecurity {
    private static final long serialVersionUID = 1L;
}
