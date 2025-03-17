package net.jjjshop.supplier.controller.order;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.order.OrderRefundPageParam;
import net.jjjshop.supplier.param.order.OrderRefundParam;
import net.jjjshop.supplier.service.order.OrderRefundService;
import net.jjjshop.supplier.vo.order.OrderRefundVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "订单售后管理", tags = {"订单售后管理"})
@RestController
@RequestMapping("/supplier/order/refund")
public class OrderRefundController {
    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/order/refund/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@Validated @RequestBody OrderRefundPageParam orderRefundPageParam) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        Map<String, Object> map = new HashMap<>();
        Paging<OrderRefundVo> list = orderRefundService.getList(orderRefundPageParam, user.getShopSupplierId());
        map.put("list", list);
        List<Integer> status = new ArrayList<>();
        orderRefundPageParam.setStatus(0);
        status.add(orderRefundService.getCountByStatus(orderRefundPageParam, user.getShopSupplierId()));
        orderRefundPageParam.setStatus(10);
        status.add(orderRefundService.getCountByStatus(orderRefundPageParam, user.getShopSupplierId()));
        orderRefundPageParam.setStatus(20);
        status.add(orderRefundService.getCountByStatus(orderRefundPageParam, user.getShopSupplierId()));
        orderRefundPageParam.setStatus(30);
        status.add(orderRefundService.getCountByStatus(orderRefundPageParam, user.getShopSupplierId()));
        map.put("status", status);
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @RequiresPermissions("/order/refund/detail")
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<OrderRefundVo> detail(Integer orderRefundId) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        return ApiResult.ok(orderRefundService.detail(orderRefundId, user.getShopSupplierId()));
    }

    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @RequiresPermissions("/order/refund/audit")
    @OperationLog(name = "audit")
    @ApiOperation(value = "audit", response = String.class)
    public ApiResult<String> audit(@Validated @RequestBody OrderRefundParam orderRefundParam) throws Exception{
        if (orderRefundService.audit(orderRefundParam)) {
            return ApiResult.ok(null, "审核提交成功");
        } else {
            return ApiResult.fail("审核提交失败");
        }
    }

    @RequestMapping(value = "/receipt", method = RequestMethod.POST)
    @OperationLog(name = "receipt")
    @ApiOperation(value = "receipt", response = String.class)
    public ApiResult<String> receipt(@Validated @RequestBody OrderRefundParam orderRefundParam) throws Exception{
        if (orderRefundService.receipt(orderRefundParam)) {
            return ApiResult.ok(null, "订单退款成功");
        } else {
            return ApiResult.fail("订单退款失败");
        }
    }
}
