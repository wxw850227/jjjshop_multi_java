package net.jjjshop.front.util.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.vo.user.UserAddressVo;
import net.jjjshop.front.vo.store.StoreVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@ApiModel(value = "订单信息", description = "订单信息")
public class OrderData implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("配送类型")
    private Integer delivery;

    @ApiModelProperty("配送地址")
    private UserAddressVo address;

    @ApiModelProperty("是否存在地址")
    private Boolean existAddress;

    @ApiModelProperty("配送费用")
    private BigDecimal expressPrice;

    @ApiModelProperty("当前用户收货城市是否存在配送规则中")
    private Boolean intraRegion;

    @ApiModelProperty("配送地址")
    private StoreVo extractStore;

    @ApiModelProperty("是否允许使用积分抵扣")
    private Boolean isAllowPoints;

    @ApiModelProperty("是否使用积分抵扣")
    private Boolean isUsePoints;

    @ApiModelProperty("记忆的自提联系方式")
    private Map<String, String> lastExtract;

    @ApiModelProperty("系统支持的配送方式")
    private List<Integer> deliverySetting;

    @ApiModelProperty("订单金额(含优惠折扣)")
    private BigDecimal orderPrice;

    @ApiModelProperty("订单实付款金额(订单金额 + 运费)")
    private BigDecimal orderPayPrice;

    @ApiModelProperty("订单的商品总金额(不含优惠折扣)")
    private BigDecimal orderTotalPrice;

    @ApiModelProperty("订单赠送积分")
    private Integer pointsBonus;

    @ApiModelProperty("积分抵扣")
    private BigDecimal pointsMoney;

    @ApiModelProperty("订单赠送积分")
    private Integer pointsNum;

    @ApiModelProperty("门店id")
    private Integer storeId;

    @ApiModelProperty("供应商金额")
    private BigDecimal supplierMoney;

    @ApiModelProperty("平台金额")
    private BigDecimal sysMoney;
}
