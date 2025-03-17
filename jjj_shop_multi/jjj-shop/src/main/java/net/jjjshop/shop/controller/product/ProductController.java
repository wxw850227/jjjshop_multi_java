

package net.jjjshop.shop.controller.product;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.product.ProductPageParam;
import net.jjjshop.shop.param.product.ProductParam;
import net.jjjshop.shop.service.product.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(value = "index", tags = {"商品列表"})
@RestController
@RequestMapping("/shop/product/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/product/product/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index(@Validated @RequestBody ProductPageParam productPageParam) throws Exception{
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

    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @RequiresPermissions("/product/product/edit")
    @OperationLog(name = "audit")
    @ApiOperation(value = "audit", response = String.class)
    public ApiResult<String> audit(Integer productId, Integer state, String remark) {
        if(productService.setAudit(productId, state,remark)) {
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

    //批量删除
    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    @RequiresPermissions("/product/product/delete")
    @OperationLog(name = "deleteByIds")
    @ApiOperation(value = "deleteByIds", response = String.class)
    public ApiResult<String> deleteByIds(String productIds) {
        if(StringUtils.isEmpty(productIds)){
            throw new BusinessException("请选择商品");
        }
        List<Integer> list = Arrays.asList(productIds.split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
        if(productService.deleteByIds(list)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }
}
