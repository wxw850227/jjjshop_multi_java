package net.jjjshop.supplier.controller.shop;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.supplier.SupplierServiceApplyParam;
import net.jjjshop.supplier.service.supplier.SupplierServiceApplyService;
import net.jjjshop.supplier.service.supplier.SupplierServiceSecurityService;
import net.jjjshop.supplier.vo.supplier.SupplierSecurityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "index", tags = {"商品分类列表"})
@RestController
@RequestMapping("/supplier/shop/security")
public class SupplierSecurityController {
    @Autowired
    private SupplierServiceSecurityService supplierServiceSecurityService;
    @Autowired
    private SupplierServiceApplyService supplierServiceApplyService;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<SupplierSecurityVo>> index() throws Exception {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        return ApiResult.ok(supplierServiceSecurityService.getList(user.getShopSupplierId()));
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @OperationLog(name = "apply")
    @ApiOperation(value = "apply", response = String.class)
    public ApiResult<String> apply(@RequestBody @Validated SupplierServiceApplyParam supplierServiceApplyParam) {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        supplierServiceApplyParam.setShopSupplierId(user.getShopSupplierId());
        if (supplierServiceApplyService.apply(supplierServiceApplyParam)) {
            return ApiResult.ok(null, "操作成功");
        } else {
            return ApiResult.fail("操作失败");
        }
    }

    @RequestMapping(value = "/quit", method = RequestMethod.POST)
    @OperationLog(name = "quit")
    @ApiOperation(value = "quit", response = String.class)
    public ApiResult<String> quit(@RequestBody @Validated SupplierServiceApplyParam supplierServiceApplyParam) {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        supplierServiceApplyParam.setShopSupplierId(user.getShopSupplierId());
        if (supplierServiceApplyService.quit(supplierServiceApplyParam)) {
            return ApiResult.ok(null, "操作成功");
        } else {
            return ApiResult.fail("操作失败");
        }
    }


}
