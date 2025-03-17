package net.jjjshop.supplier.controller.cash;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderRefund;
import net.jjjshop.common.entity.order.OrderSettled;
import net.jjjshop.common.entity.supplier.SupplierAccount;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierService;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.product.CommentPageParam;
import net.jjjshop.supplier.param.supplier.SupplierAccountParam;
import net.jjjshop.supplier.param.supplier.SupplierCapitalPageParam;
import net.jjjshop.supplier.param.supplier.SupplierCashPageParam;
import net.jjjshop.supplier.param.supplier.SupplierCashParam;
import net.jjjshop.supplier.service.order.OrderRefundService;
import net.jjjshop.supplier.service.order.OrderService;
import net.jjjshop.supplier.service.order.OrderSettledService;
import net.jjjshop.supplier.service.product.ProductSpecService;
import net.jjjshop.supplier.service.supplier.SupplierAccountService;
import net.jjjshop.supplier.service.supplier.SupplierCapitalService;
import net.jjjshop.supplier.service.supplier.SupplierCashService;
import net.jjjshop.supplier.vo.supplier.SupplierAccountVo;
import net.jjjshop.supplier.vo.supplier.SupplierCashVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"商品列表"})
@RestController
@RequestMapping("/supplier/cash/cash")
public class CashController {
    @Autowired
    private SupplierAccountService supplierAccountService;
    @Autowired
    private SupplierCashService supplierCashService;
    @Autowired
    private SupplierUserService supplierUserService;
    @Autowired
    private OrderSettledService orderSettledService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private SupplierCapitalService supplierCapitalService;
    @Autowired
    private SupplierService supplierService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@Validated @RequestBody SupplierCapitalPageParam supplierCapitalPageParam) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        Map<String, Object> map = new HashMap<>();
        JSONObject tjData= new JSONObject();
        tjData.put("nosettledMoney",orderService.getNoSettledMoney(user.getShopSupplierId()));
        tjData.put("refundMoney",orderRefundService.getRefundMoney(user.getShopSupplierId()));
        map.put("tjData",tjData);
        map.put("supplier",supplierService.getById(user.getShopSupplierId()));
        map.put("cashList",supplierCapitalService.getList(user.getShopSupplierId(), supplierCapitalPageParam));
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    @OperationLog(name = "account")
    @ApiOperation(value = "account", response = String.class)
    public ApiResult<SupplierAccountVo> account() throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        return ApiResult.ok(supplierAccountService.detail(user.getShopSupplierId()));
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    @OperationLog(name = "account")
    @ApiOperation(value = "account", response = String.class)
    public ApiResult<String> account(@Validated @RequestBody SupplierAccountParam supplierAccountParam) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        if(supplierAccountService.add(user.getShopSupplierId(), supplierAccountParam)) {
            return ApiResult.ok("操作成功");
        }else {
            return ApiResult.fail("操作失败");
        }
    }

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<Paging<SupplierCashVo>> lists(@Validated @RequestBody SupplierCashPageParam supplierCashPageParam) {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        return ApiResult.ok(supplierCashService.getList(user.getShopSupplierId(), supplierCashPageParam));
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @OperationLog(name = "apply")
    @ApiOperation(value = "apply", response = String.class)
    public ApiResult<String> apply(@Validated @RequestBody SupplierCashParam supplierCashParam) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        if(supplierCashService.submit(user.getShopSupplierId(), supplierCashParam)) {
            return ApiResult.ok("提现成功");
        }else {
            return ApiResult.fail("提现失败");
        }
    }
}
