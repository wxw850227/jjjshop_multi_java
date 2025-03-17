package net.jjjshop.front.controller.supplier;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.product.ProductParam;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.front.vo.product.ProductListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "store", tags = {"订单核销"})
@RestController
@RequestMapping("/front/supplier/product")
public class SupplierProductController  extends BaseController {
    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<ProductListVo>> index(@RequestBody ProductParam productParam) throws Exception {
        User user = this.getUser(true);
        return ApiResult.ok(productService.getList(productParam, user));
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @OperationLog(name = "modify")
    @ApiOperation(value = "modify", response = String.class)
    public ApiResult<String> modify(@RequestBody ProductParam productParam) throws Exception {
        User user = this.getUser(true);
        Product product = productService.getById(productParam.getProductId());
        SupplierUser supplierUser = supplierUserService.getOne(new LambdaQueryWrapper<SupplierUser>().eq(SupplierUser::getUserId, user.getUserId()).last("LIMIT 1"));
        if(product.getShopSupplierId().intValue() != supplierUser.getShopSupplierId().intValue()) {
            throw new BusinessException("非法请求");
        }
        if(productService.editStatus(productParam.getProductId(),productParam.getProductStatus())){
            return ApiResult.ok(null,"操作成功");
        }else {
            return ApiResult.fail("操作成功");
        }
    }

}
