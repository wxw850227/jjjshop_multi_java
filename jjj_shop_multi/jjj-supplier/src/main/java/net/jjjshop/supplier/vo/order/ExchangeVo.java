package net.jjjshop.supplier.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.Order;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("订单换货VO")
public class ExchangeVo extends Order {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("购买用户")
    private String nickname;

    @ApiModelProperty("微信头像")
    private String avatarurl;

    @ApiModelProperty("订单状态")
    private String orderStatusText;
}
