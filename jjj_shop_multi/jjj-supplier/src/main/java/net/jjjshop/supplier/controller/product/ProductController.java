

package net.jjjshop.supplier.controller.product;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.product.ProductPageParam;
import net.jjjshop.supplier.param.product.ProductParam;
import net.jjjshop.supplier.service.product.ProductService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"商品列表"})
@RestController
@RequestMapping("/supplier/product/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierUserService supplierUserService;


    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/product/product/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index(@Validated @RequestBody ProductPageParam productPageParam) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        productPageParam.setShopSupplierId(user.getShopSupplierId());
        return ApiResult.ok(productService.getList(productPageParam));
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    @RequiresPermissions("/product/product/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Map<String, Object>> toAdd() {
        return ApiResult.ok(productService.getBaseData(0, "add"));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/product/product/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated ProductParam productParam) {
        if(productService.add(productParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @RequiresPermissions("/product/product/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Map<String, Object>> toEdit(Integer productId, String scene) {
        return ApiResult.ok(productService.getBaseData(productId, scene));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/product/product/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated ProductParam productParam) {
        if(productService.edit(productParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/state", method = RequestMethod.POST)
    @RequiresPermissions("/product/product/edit")
    @OperationLog(name = "state")
    @ApiOperation(value = "state", response = String.class)
    public ApiResult<String> state(Integer productId, Integer state) {
        if(productService.setState(productId, state)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/product/product/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer productId) {
        if(productService.setDelete(productId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }
}
