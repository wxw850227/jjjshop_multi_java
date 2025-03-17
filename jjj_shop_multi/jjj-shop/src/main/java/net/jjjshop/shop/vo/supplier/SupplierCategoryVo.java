package net.jjjshop.shop.vo.supplier;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.supplier.SupplierCategory;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户分类VO")
public class SupplierCategoryVo extends SupplierCategory {
    private static final long serialVersionUID = 1L;
}
