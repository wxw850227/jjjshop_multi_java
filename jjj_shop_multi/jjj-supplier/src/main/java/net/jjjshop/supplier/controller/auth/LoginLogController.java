package net.jjjshop.supplier.controller.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.auth.LoginLogPageParam;
import net.jjjshop.supplier.service.supplier.SupplierLoginLogService;
import net.jjjshop.supplier.service.supplier.SupplierUserService;
import net.jjjshop.supplier.vo.auth.SupplierLoginLogVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "setting", tags = {"setting"})
@RestController
@RequestMapping("/supplier/auth/loginlog")
public class LoginLogController {

    @Autowired
    private SupplierLoginLogService supplierLoginLogService;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<SupplierLoginLogVo>> index(@RequestBody LoginLogPageParam loginLogPageParam) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        return ApiResult.ok(supplierLoginLogService.getList(user.getShopSupplierId(), loginLogPageParam));
    }
}
