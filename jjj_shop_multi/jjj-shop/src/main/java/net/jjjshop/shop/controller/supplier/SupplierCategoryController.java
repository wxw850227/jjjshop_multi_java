package net.jjjshop.shop.controller.supplier;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.ProductCategoryCache;
import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.common.entity.supplier.SupplierCategory;
import net.jjjshop.common.vo.product.CategoryVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.product.CategoryParam;
import net.jjjshop.shop.param.setting.ExpressPageParam;
import net.jjjshop.shop.param.setting.ExpressParam;
import net.jjjshop.shop.param.supplier.SupplierCategoryParam;
import net.jjjshop.shop.service.product.ProductCategoryService;
import net.jjjshop.shop.service.supplier.SupplierCategoryService;
import net.jjjshop.shop.vo.supplier.SupplierCategoryVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/shop/supplier/category")
public class SupplierCategoryController {
    @Autowired
    private SupplierCategoryService supplierCategoryService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<SupplierCategoryVo>> index() throws Exception {
        return ApiResult.ok(supplierCategoryService.getList());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated SupplierCategoryParam supplierCategoryParam) {
        if (supplierCategoryService.add(supplierCategoryParam)) {
            return ApiResult.ok(null, "添加成功");
        } else {
            return ApiResult.fail("添加失败");
        }
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<SupplierCategoryVo> toEdit(Integer id) {
        return ApiResult.ok(supplierCategoryService.detail(id));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated SupplierCategoryParam supplierCategoryParam) {
        if (supplierCategoryService.edit(supplierCategoryParam)) {
            return ApiResult.ok(null, "修改成功");
        } else {
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String>  delById(Integer id) {
        if (supplierCategoryService.delById(id)) {
            return ApiResult.ok("删除成功");
        } else {
            return ApiResult.fail("删除失败");
        }
    }
}
