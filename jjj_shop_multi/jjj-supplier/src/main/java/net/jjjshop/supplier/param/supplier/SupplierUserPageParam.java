

package net.jjjshop.supplier.param.supplier;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SupplierUserPageParam对象", description = "商户用户分页参数对象")
public class SupplierUserPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
