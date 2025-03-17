package net.jjjshop.shop.controller.cash;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.statistics.RankingParam;
import net.jjjshop.shop.service.order.OrderSettledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Api(value = "链接数据", tags = {"链接数据"})
@RestController
@RequestMapping("/shop/cash/settled")
public class SettledController {

    @Autowired
    private OrderSettledService orderSettledService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<JSONObject> index() throws Exception{
        return ApiResult.ok(orderSettledService.getData());
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @OperationLog(name = "data")
    @ApiOperation(value = "data", response = String.class)
    public ApiResult<Map<String, Object>> data(RankingParam rankingParam) throws Exception{
        return ApiResult.ok(orderSettledService.getSettledDataByDate(rankingParam.getStartDate(), rankingParam.getEndDate()));
    }
}
