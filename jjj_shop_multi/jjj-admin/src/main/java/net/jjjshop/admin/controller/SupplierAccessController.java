

package net.jjjshop.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.param.ShopAccessParam;
import net.jjjshop.admin.param.SupplierAccessParam;
import net.jjjshop.admin.service.ShopAccessService;
import net.jjjshop.admin.service.SupplierAccessService;
import net.jjjshop.common.util.ShopAccessUtils;
import net.jjjshop.common.util.SupplierAccessUtils;
import net.jjjshop.common.vo.shop.ShopAccessVo;
import net.jjjshop.common.vo.shop.SupplierAccessVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "access", tags = {"access"})
@RestController
@RequestMapping("/admin/supplierAccess")
public class SupplierAccessController {
    @Autowired
    private SupplierAccessService supplierAccessService;
    @Autowired
    private SupplierAccessUtils supplierAccessUtils;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<SupplierAccessVo>> index() {
        List<SupplierAccessVo> list = supplierAccessUtils.getAll();
        return ApiResult.ok(list);
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    @OperationLog(name = "status")
    @ApiOperation(value = "status", response = String.class)
    public ApiResult<Boolean> status(Integer id,Integer status) {
        Boolean result = supplierAccessService.editStatusById(id, status);
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer id) {
        if(supplierAccessService.delById(id)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated SupplierAccessParam supplierAccessParam) {
        if(supplierAccessService.add(supplierAccessParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated SupplierAccessParam supplierAccessParam) {
        if(supplierAccessService.edit(supplierAccessParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }
}
