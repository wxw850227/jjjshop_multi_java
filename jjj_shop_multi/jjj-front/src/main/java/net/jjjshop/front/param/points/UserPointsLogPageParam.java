package net.jjjshop.front.param.points;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserPointsLogPageParam对象", description = "积分明细分页对象")
public class UserPointsLogPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
