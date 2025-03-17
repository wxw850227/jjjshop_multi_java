package net.jjjshop.shop.controller.supplier;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.supplier.SupplierCashPageParam;
import net.jjjshop.shop.param.supplier.SupplierCashParam;
import net.jjjshop.shop.param.supplier.SupplierPageParam;
import net.jjjshop.shop.param.supplier.SupplierParam;
import net.jjjshop.shop.service.supplier.SupplierCashService;
import net.jjjshop.shop.service.supplier.SupplierCategoryService;
import net.jjjshop.shop.service.supplier.SupplierService;
import net.jjjshop.shop.vo.supplier.SupplierCashVo;
import net.jjjshop.shop.vo.supplier.SupplierCategoryVo;
import net.jjjshop.shop.vo.supplier.SupplierVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/shop/supplier/cash")
public class SupplierCashController {
    @Autowired
    private SupplierCashService supplierCashService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<SupplierCashVo>> index(@Validated @RequestBody SupplierCashPageParam supplierCashPageParam) throws Exception {
        return ApiResult.ok(supplierCashService.getList(supplierCashPageParam));
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @OperationLog(name = "submit")
    @ApiOperation(value = "submit", response = String.class)
    public ApiResult<String> submit(@Validated @RequestBody SupplierCashParam supplierCashParam) throws Exception {
        if(supplierCashService.submit(supplierCashParam)){
            return ApiResult.ok(null,"审核成功");
        }else {
            return ApiResult.fail("审核失败");
        }
    }

    @RequestMapping(value = "/money", method = RequestMethod.POST)
    @OperationLog(name = "money")
    @ApiOperation(value = "money", response = String.class)
    public ApiResult<String> money(Integer id) throws Exception {
        if(supplierCashService.money(id)){
            return ApiResult.ok(null,"审核成功");
        }else {
            return ApiResult.fail("审核失败");
        }
    }

}
