package net.jjjshop.shop.controller.statistics;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.util.ProductDataUtils;
import net.jjjshop.common.util.SupplierDataUtils;
import net.jjjshop.common.util.UserDataUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.statistics.RankingParam;
import net.jjjshop.shop.service.statistics.OrderRankingService;
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
@RequestMapping("/shop/statistics/supplier")
public class SupplierController {

    @Autowired
    private SupplierDataUtils supplierDataUtils;


    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<JSONObject> index() throws Exception{
        return ApiResult.ok(supplierDataUtils.getData(null));
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @OperationLog(name = "data")
    @ApiOperation(value = "data", response = String.class)
    public ApiResult<Map<String,Object>> data(@Validated @RequestBody RankingParam rankingParam) throws Exception{
        return ApiResult.ok(supplierDataUtils.getSupplierDateByDate(rankingParam.getStartDate(), rankingParam.getEndDate()));
    }


}
