package net.jjjshop.supplier.controller.operate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.entity.user.UserFavorite;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.ad.AdPageParam;
import net.jjjshop.supplier.param.ad.AdParam;
import net.jjjshop.supplier.param.user.UserFavoritePageParam;
import net.jjjshop.supplier.service.ad.AdService;
import net.jjjshop.supplier.service.user.UserFavoriteService;
import net.jjjshop.supplier.vo.ad.AdVo;
import net.jjjshop.supplier.vo.user.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "查询用户接口", tags = {"查询用户接口"})
@RestController
@RequestMapping("/supplier/operate/fans")
public class FansController {
    @Autowired
    private UserFavoriteService userFavoriteService;
    @Autowired
    private SupplierUserService supplierUserService;


    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<UserVo>> index(@Validated @RequestBody UserFavoritePageParam userFavoritePageParam) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        return ApiResult.ok(userFavoriteService.getUserList(user.getShopSupplierId(), userFavoritePageParam));
    }

}
