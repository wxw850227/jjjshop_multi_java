package net.jjjshop.supplier.controller.statistics;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.util.UserDataUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.statistics.RankingParam;
import net.jjjshop.supplier.service.supplier.SupplierUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/supplier/statistics/user")
public class UserController {

    @Autowired
    private UserDataUtils userDataUtils;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<JSONObject> index() throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        return ApiResult.ok(userDataUtils.getData(user.getShopSupplierId()));
    }

    @RequestMapping(value = "/visit", method = RequestMethod.POST)
    @OperationLog(name = "visit")
    @ApiOperation(value = "visit", response = String.class)
    public ApiResult<Map<String,Object>> product(@Validated @RequestBody RankingParam RankingParam) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        return  ApiResult.ok(userDataUtils.getVisitByDate(RankingParam.getStartDate(), RankingParam.getEndDate(), user.getShopSupplierId()));
    }


}
