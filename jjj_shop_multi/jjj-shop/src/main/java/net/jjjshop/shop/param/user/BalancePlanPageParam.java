package net.jjjshop.shop.param.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BalancePlanPageParam对象", description = "余额充值分页对象")
public class BalancePlanPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
