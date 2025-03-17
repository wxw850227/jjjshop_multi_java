package net.jjjshop.shop.controller.supplier;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierServiceSecurity;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.supplier.SupplierCategoryParam;
import net.jjjshop.shop.param.supplier.SupplierSecurityParam;
import net.jjjshop.shop.service.supplier.SupplierCategoryService;
import net.jjjshop.shop.service.supplier.SupplierServiceSecurityService;
import net.jjjshop.shop.vo.supplier.SupplierCategoryVo;
import net.jjjshop.shop.vo.supplier.SupplierSecurityVo;
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
@RequestMapping("/shop/supplier/security")
public class SupplierSecurityController {
    @Autowired
    private SupplierServiceSecurityService supplierServiceSecurityService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<SupplierSecurityVo>> index() throws Exception {
        return ApiResult.ok(supplierServiceSecurityService.getList());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated SupplierSecurityParam supplierSecurityParam) {
        if (supplierServiceSecurityService.add(supplierSecurityParam)) {
            return ApiResult.ok(null, "添加成功");
        } else {
            return ApiResult.fail("添加失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated SupplierSecurityParam supplierSecurityParam) {
        if (supplierServiceSecurityService.edit(supplierSecurityParam)) {
            return ApiResult.ok(null, "修改成功");
        } else {
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String>  delById(Integer serviceSecurityId) {
        if (supplierServiceSecurityService.delById(serviceSecurityId)) {
            return ApiResult.ok("删除成功");
        } else {
            return ApiResult.fail("删除失败");
        }
    }
}
