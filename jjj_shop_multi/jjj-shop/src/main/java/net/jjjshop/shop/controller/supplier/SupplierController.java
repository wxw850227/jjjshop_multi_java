package net.jjjshop.shop.controller.supplier;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.supplier.*;
import net.jjjshop.shop.service.supplier.*;
import net.jjjshop.shop.vo.supplier.*;
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
@RequestMapping("/shop/supplier/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierCategoryService supplierCategoryService;
    @Autowired
    private SupplierServiceApplyService supplierServiceApplyService;
    @Autowired
    private SupplierApplyService supplierApplyService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<SupplierVo>> index(@Validated @RequestBody SupplierPageParam supplierPageParam) throws Exception {
        return ApiResult.ok(supplierService.getList(supplierPageParam));
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.POST)
    @OperationLog(name = "toAdd")
    @ApiOperation(value = "toAdd", response = String.class)
    public ApiResult<List<SupplierCategoryVo>> toAdd() throws Exception {
        return ApiResult.ok(supplierCategoryService.getList());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@Validated @RequestBody SupplierParam supplierParam) throws Exception {
        if(supplierService.add(supplierParam)){
            return ApiResult.ok(null,"添加成功");
        }else {
            return ApiResult.fail("添加失败");
        }
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.POST)
    @OperationLog(name = "toEdit")
    @ApiOperation(value = "toEdit", response = String.class)
    public ApiResult<Map<String, Object>> toEdit(Integer shopSupplierId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        SupplierVo supplierVo = supplierService.toEdit(shopSupplierId);
        result.put("model",supplierVo);
        result.put("category", supplierCategoryService.getList());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@Validated @RequestBody SupplierParam supplierParam) throws Exception {
        if(supplierService.edit(supplierParam)){
            return ApiResult.ok(null,"编辑成功");
        }else {
            return ApiResult.fail("编辑失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer shopSupplierId) throws Exception {
        if(supplierService.setDelete(shopSupplierId)){
            return ApiResult.ok(null,"删除成功");
        }else {
            return ApiResult.fail("删除失败");
        }
    }

    @RequestMapping(value = "/recycle", method = RequestMethod.POST)
    @OperationLog(name = "recycle")
    @ApiOperation(value = "recycle", response = String.class)
    public ApiResult<String> recycle(Integer shopSupplierId, Integer isRecycle) throws Exception {
        if(supplierService.setRecycle(isRecycle,shopSupplierId)){
            return ApiResult.ok(null,"操作成功");
        }else {
            return ApiResult.fail("操作失败");
        }
    }

    @RequestMapping(value = "/security", method = RequestMethod.POST)
    @OperationLog(name = "security")
    @ApiOperation(value = "security", response = String.class)
    public ApiResult<Paging<SupplierServiceApplyVo>> security(@Validated @RequestBody SupplierServiceApplyPageParam supplierServiceApplyPageParam) throws Exception {
        return ApiResult.ok(supplierServiceApplyService.getList(supplierServiceApplyPageParam));
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @OperationLog(name = "verify")
    @ApiOperation(value = "verify", response = String.class)
    public ApiResult<String> verify(@Validated @RequestBody SupplierServiceApplyParam supplierServiceApplyParam) throws Exception {
        if(supplierServiceApplyService.verify(supplierServiceApplyParam)){
            return ApiResult.ok(null,"操作成功");
        }else {
            return ApiResult.fail("操作失败");
        }
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @OperationLog(name = "apply")
    @ApiOperation(value = "apply", response = String.class)
    public ApiResult<Paging<SupplierApplyVo>> apply(@Validated @RequestBody SupplierApplyPageParam supplierApplyPageParam) throws Exception {
        return ApiResult.ok(supplierApplyService.getList(supplierApplyPageParam));
    }

    @RequestMapping(value = "/toAudit", method = RequestMethod.GET)
    @OperationLog(name = "toAudit")
    @ApiOperation(value = "toAudit", response = String.class)
    public ApiResult<SupplierApplyVo> toAudit(Integer supplierApplyId) throws Exception {
        return ApiResult.ok(supplierApplyService.detail(supplierApplyId));
    }

    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @OperationLog(name = "audit")
    @ApiOperation(value = "audit", response = String.class)
    public ApiResult<String> audit(@Validated @RequestBody SupplierApplyParam supplierApplyParam) throws Exception {
        if(supplierApplyService.audit(supplierApplyParam)){
            return ApiResult.ok(null,"操作成功");
        }else {
            return ApiResult.fail("操作失败");
        }
    }
}
