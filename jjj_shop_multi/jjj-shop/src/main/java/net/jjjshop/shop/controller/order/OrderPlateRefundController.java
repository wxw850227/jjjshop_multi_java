package net.jjjshop.shop.controller.order;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.order.OrderRefundPageParam;
import net.jjjshop.shop.param.order.OrderRefundParam;
import net.jjjshop.shop.service.order.OrderRefundService;
import net.jjjshop.shop.vo.order.OrderRefundVo;
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
@RequestMapping("/shop/order/platerefund")
public class OrderPlateRefundController {

    @Autowired
    private OrderRefundService orderRefundService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@Validated @RequestBody OrderRefundPageParam orderRefundPageParam) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        Paging<OrderRefundVo> list = orderRefundService.getPlateList(orderRefundPageParam);
        map.put("list", list);
        List<Integer> status = new ArrayList<>();
        //平台售后状态，10申请平台介入20同意30拒绝
        orderRefundPageParam.setPlateStatus(10);
        status.add(orderRefundService.getCountByPlateStatus(orderRefundPageParam));
        orderRefundPageParam.setPlateStatus(30);
        status.add(orderRefundService.getCountByPlateStatus(orderRefundPageParam));
        orderRefundPageParam.setPlateStatus(20);
        status.add(orderRefundService.getCountByPlateStatus(orderRefundPageParam));
        //售后单状态(0进行中 10已拒绝 20已完成 30已取消)
        orderRefundPageParam.setStatus(20);
        status.add(orderRefundService.getCountByPlateStatus(orderRefundPageParam));
        map.put("status", status);
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<OrderRefundVo> detail(Integer orderRefundId) throws Exception{
        return ApiResult.ok(orderRefundService.detail(orderRefundId));
    }

    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @OperationLog(name = "audit")
    @ApiOperation(value = "audit", response = String.class)
    public ApiResult<String> audit(@Validated @RequestBody OrderRefundParam orderRefundParam) throws Exception{
        if (orderRefundService.plateAudit(orderRefundParam)) {
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
