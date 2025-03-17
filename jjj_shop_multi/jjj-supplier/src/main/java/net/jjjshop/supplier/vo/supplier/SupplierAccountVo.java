package net.jjjshop.supplier.vo.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.store.Store;
import net.jjjshop.common.entity.supplier.SupplierAccount;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户账户VO")
public class SupplierAccountVo extends SupplierAccount {
    private static final long serialVersionUID = 1L;
}
