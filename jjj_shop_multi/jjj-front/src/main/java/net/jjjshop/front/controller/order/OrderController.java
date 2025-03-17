

package net.jjjshop.front.controller.order;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.order.OrderBuyParam;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.front.service.settings.MessageService;
import net.jjjshop.front.service.user.UserCartService;
import net.jjjshop.front.util.order.MasterOrderSettledUtils;
import net.jjjshop.front.vo.product.ProductBuyVo;
import net.jjjshop.front.vo.supplier.SupplierBuyVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "order", tags = {"订单管理"})
@Scope("prototype")
@RestController
@RequestMapping("/front/order/order")
public class OrderController extends BaseController {
    @Autowired
    private ProductService productService;
    @Autowired
    private MasterOrderSettledUtils masterOrderSettledUtils;
    @Autowired
    private UserCartService userCartService;
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/toBuy", method = RequestMethod.POST)
    @OperationLog(name = "toBuy")
    @ApiOperation(value = "toBuy", response = String.class)
    public ApiResult<Map<String, Object>> toBuy(@RequestBody OrderBuyParam orderBuyParam){
        User user = this.getUser(true);
        List<SupplierBuyVo> supplierData = productService.getOrderProductListByNow(orderBuyParam);
        Map<String, Object> result = masterOrderSettledUtils.settlement(user, supplierData, orderBuyParam);
        // 如果来源是小程序, 则获取小程序订阅消息id.获取支付成功,发货通知.
        result.put("templateArr", messageService.getMessageByNameArr(orderBuyParam.getPaySource(), new String[]{"order_pay_user", "order_delivery_user"}));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    @OperationLog(name = "buy")
    @ApiOperation(value = "buy", response = String.class)
    public ApiResult<String> buy(@RequestBody OrderBuyParam orderBuyParam){
        User user = this.getUser(true);
        List<SupplierBuyVo> supplierData = productService.getOrderProductListByNow(orderBuyParam);
        List<Integer> orderIds = masterOrderSettledUtils.createOrder(user, supplierData, orderBuyParam);
        return ApiResult.ok(StringUtils.join(orderIds,","));
    }

    @RequestMapping(value = "/toCart", method = RequestMethod.POST)
    @OperationLog(name = "toCart")
    @ApiOperation(value = "toCart", response = String.class)
    public ApiResult<Map<String, Object>> toCart(@RequestBody OrderBuyParam orderBuyParam){
        User user = this.getUser(true);
        List<SupplierBuyVo> supplierData = productService.getOrderProductListByCart(user, orderBuyParam);
        Map<String, Object> result = masterOrderSettledUtils.settlement(user, supplierData, orderBuyParam);
        // 如果来源是小程序, 则获取小程序订阅消息id.获取支付成功,发货通知.
        result.put("templateArr", messageService.getMessageByNameArr(orderBuyParam.getPaySource(), new String[]{"order_pay_user", "order_delivery_user"}));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    @OperationLog(name = "cart")
    @ApiOperation(value = "cart", response = String.class)
    public ApiResult<String> cart(@RequestBody OrderBuyParam orderBuyParam){
        User user = this.getUser(true);
        List<SupplierBuyVo> supplierData = productService.getOrderProductListByCart(user, orderBuyParam);
        List<Integer> orderIds = masterOrderSettledUtils.createOrder(user, supplierData, orderBuyParam);
        // 移出购物车中已下单的商品
        userCartService.clearAll(orderBuyParam.getCartIds());
        return ApiResult.ok(StringUtils.join(orderIds,","));
    }

}
