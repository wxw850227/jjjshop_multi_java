package net.jjjshop.front.vo.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.front.util.order.vo.OrderData;
import net.jjjshop.front.vo.product.ProductBuyVo;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("供应商下单VO")
public class SupplierBuyVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("购买商品")
    private List<ProductBuyVo> productList;

    @ApiModelProperty("供应商")
    private SupplierVo supplier;

    @ApiModelProperty("订单数据")
    private OrderData orderData;

}
