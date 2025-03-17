package net.jjjshop.front.param.order;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * 订单参数
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "订单参数对象", description = "订单参数对象")
public class OrderBuyParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("发货方式")
    private Integer delivery;

    @ApiModelProperty("上门自提门店id")
    private Integer storeId;

    @ApiModelProperty("是否使用积分")
    private Boolean isUsePoints;

    @ApiModelProperty("支付渠道来源")
    private String paySource;

    @ApiModelProperty("商品id")
    private Integer productId;

    @ApiModelProperty("购买商品数量")
    private Integer productNum;

    @ApiModelProperty("商品specId")
    private String specSkuId;

    @ApiModelProperty("商品skuId")
    private Integer productSkuId;

    @ApiModelProperty("自提电话")
    private String phone;

    @ApiModelProperty("自提联系人")
    private String linkman;

    @ApiModelProperty("订单备注")
    private String remark;

    @ApiModelProperty("购物车")
    private String cartIds;

    @ApiModelProperty("门店参数")
    private Map<Integer, StoreData> supplier;

    @Data
    @Accessors(chain = true)
    @ApiModel("门店参数")
    public static class StoreData implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer delivery;
        private Integer storeId;
        private String remark;
    }
}
