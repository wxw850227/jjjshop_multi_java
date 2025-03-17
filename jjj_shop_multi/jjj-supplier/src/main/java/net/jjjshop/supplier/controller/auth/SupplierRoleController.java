package net.jjjshop.supplier.controller.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierRole;
import net.jjjshop.common.util.SupplierAccessUtils;
import net.jjjshop.common.vo.shop.SupplierAccessVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.supplier.param.supplier.SupplierRoleParam;
import net.jjjshop.supplier.service.supplier.SupplierRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/supplier/auth/role")
public class SupplierRoleController {
    @Autowired
    private SupplierRoleService supplierRoleService;
    @Autowired
    private SupplierAccessUtils supplierAccessUtils;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/auth/role/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<SupplierRole>> index() throws Exception{
        return ApiResult.ok(supplierRoleService.getList());
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    @RequiresPermissions("/auth/role/add")
    @OperationLog(name = "toAdd")
    @ApiOperation(value = "toAdd", response = String.class)
    public ApiResult<List<SupplierAccessVo>> toAdd() {
        return ApiResult.ok(supplierAccessUtils.getAll());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/auth/role/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Boolean> add(@Validated @RequestBody SupplierRoleParam supplierRoleParam) {
        return ApiResult.ok(supplierRoleService.add(supplierRoleParam));
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    @RequiresPermissions("/auth/role/edit")
    @OperationLog(name = "toEdit")
    @ApiOperation(value = "toEdit", response = String.class)
    public ApiResult<Map<String,Object>> toEdit(Integer roleId) {
        Map<String,Object> result = new HashMap<>();
        result.put("menu", supplierAccessUtils.getAll());
        List<Integer> selectMenu = supplierRoleService.getSelectList(roleId);
        result.put("selectMenu", selectMenu);
        result.put("model", supplierRoleService.getById(roleId));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/auth/role/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Boolean> edit(@Validated @RequestBody SupplierRoleParam supplierRoleParam) {
        return ApiResult.ok(supplierRoleService.edit(supplierRoleParam));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/auth/role/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<Boolean> delete(Integer roleId) {
        return ApiResult.ok(supplierRoleService.delete(roleId));
    }
}
