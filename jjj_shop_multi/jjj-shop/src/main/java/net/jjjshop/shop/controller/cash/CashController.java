package net.jjjshop.shop.controller.cash;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.supplier.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "链接数据", tags = {"链接数据"})
@RestController
@RequestMapping("/shop/cash/cash")
public class CashController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private SupplierService supplierService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index() throws Exception{
        //订单数据
        JSONObject tj_data = new JSONObject();
        tj_data.put("totalMoney", orderService.getTotalMoney("all", -1));
        tj_data.put("supplierMoney", orderService.getTotalMoney("supplier", -1));
        tj_data.put("sysMoney", orderService.getTotalMoney("sys", -1));
        //供应商数据
        JSONObject supplier_data = new JSONObject();
        supplier_data.put("totalMoney",supplierService.getTotalMoney("total"));
        supplier_data.put("money",supplierService.getTotalMoney("money"));
        supplier_data.put("nosettledMoney",orderService.getTotalMoney("supplier", 0));
        supplier_data.put("freezeMoney",supplierService.getTotalMoney("freezeMoney"));
        supplier_data.put("cashMoney",supplierService.getTotalMoney("cashMoney"));
        supplier_data.put("depositMoney",supplierService.getTotalMoney("depositMoney"));

        Map<String, Object> result = new HashMap<>();
        result.put("tj_data",tj_data);
        result.put("supplier_data",supplier_data);
        return ApiResult.ok(result);
    }
}
