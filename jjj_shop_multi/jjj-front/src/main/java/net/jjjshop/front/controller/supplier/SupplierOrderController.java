package net.jjjshop.front.controller.supplier;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.user.BaseSupplierController;
import net.jjjshop.front.param.order.OrderPageParam;
import net.jjjshop.front.param.order.OrderRefundPageParam;
import net.jjjshop.front.service.order.OrderRefundService;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.vo.order.OrderListVo;
import net.jjjshop.front.vo.order.OrderRefundListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "store", tags = {"订单核销"})
@RestController
@RequestMapping("/front/supplier/order")
public class SupplierOrderController extends BaseSupplierController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRefundService orderRefundService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<OrderListVo>> index(@RequestBody OrderPageParam orderPageParam) throws Exception {
        super.checkSupplier(orderPageParam.getShopSupplierId());
        User user = this.getUser(true);
        orderPageParam.setUserId(user.getUserId());
        return ApiResult.ok(orderService.getList(orderPageParam));
    }

    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    @OperationLog(name = "refund")
    @ApiOperation(value = "refund", response = String.class)
    public ApiResult<Paging<OrderRefundListVo>> refund(@RequestBody OrderRefundPageParam orderRefundPageParam) {
        super.checkSupplier(orderRefundPageParam.getShopSupplierId());
        User user = this.getUser(true);
        orderRefundPageParam.setUserId(user.getUserId());
        return ApiResult.ok(orderRefundService.getList(orderRefundPageParam));
    }
}
