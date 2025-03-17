package net.jjjshop.supplier.controller.cash;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderSettled;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.order.OrderSettledPageParam;
import net.jjjshop.supplier.param.supplier.SupplierAccountParam;
import net.jjjshop.supplier.param.supplier.SupplierCashPageParam;
import net.jjjshop.supplier.service.order.OrderSettledService;
import net.jjjshop.supplier.service.supplier.SupplierAccountService;
import net.jjjshop.supplier.service.supplier.SupplierCashService;
import net.jjjshop.supplier.vo.order.OrderSettledVo;
import net.jjjshop.supplier.vo.supplier.SupplierAccountVo;
import net.jjjshop.supplier.vo.supplier.SupplierCashVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "index", tags = {"商品列表"})
@RestController
@RequestMapping("/supplier/cash/settled")
public class SettledController {
    @Autowired
    private OrderSettledService orderSettledService;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<OrderSettledVo>> index(@Validated @RequestBody OrderSettledPageParam orderSettledPageParam) {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        return ApiResult.ok(orderSettledService.getList(user.getShopSupplierId(), orderSettledPageParam));
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<OrderSettledVo> detail(Integer settledId) {
        return ApiResult.ok(orderSettledService.detail(settledId));
    }
}
