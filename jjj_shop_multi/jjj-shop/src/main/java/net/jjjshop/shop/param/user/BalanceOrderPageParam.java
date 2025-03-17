package net.jjjshop.shop.param.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "账单明细分页对象", description = "账单明细分页对象")
public class BalanceOrderPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
