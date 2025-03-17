package net.jjjshop.supplier.controller.product;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.product.CommentPageParam;
import net.jjjshop.supplier.param.product.CommentParam;
import net.jjjshop.supplier.service.product.ProductCommentService;
import net.jjjshop.supplier.vo.product.CommentVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"商品评价"})
@RestController
@RequestMapping("/supplier/product/comment")
public class CommentController {

    @Autowired
    private ProductCommentService productCommentService;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/product/comment/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@Validated @RequestBody CommentPageParam commentPageParam) {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        commentPageParam.setShopSupplierId(user.getShopSupplierId());
        return ApiResult.ok(productCommentService.getList(commentPageParam));
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @RequiresPermissions("/product/comment/detail")
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<CommentVo> detail(Integer commentId) {
        return ApiResult.ok(productCommentService.getDetail(commentId));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/product/comment/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@Validated @RequestBody CommentParam commentParam) {
        if (productCommentService.edit(commentParam)) {
            return ApiResult.ok(null, "修改成功");
        } else {
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/product/comment/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer commentId) {
        if (productCommentService.setDelete(commentId)) {
            return ApiResult.ok(null, "修改成功");
        } else {
            return ApiResult.fail("修改失败");
        }
    }
}
