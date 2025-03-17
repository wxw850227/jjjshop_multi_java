package net.jjjshop.shop.controller.statistics;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.util.ProductDataUtils;
import net.jjjshop.common.vo.product.ProductVo;
import net.jjjshop.common.vo.statistics.ProductRefundRankingVo;
import net.jjjshop.common.vo.statistics.ProductSaleRankingVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.statistics.RankingParam;
import net.jjjshop.shop.service.statistics.OrderRankingService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/shop/statistics/sales")
public class SalesController {

    @Autowired
    private ProductDataUtils productDataUtils;

    @Autowired
    private OrderRankingService orderRankingService;


    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/statistics/sales/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index() throws Exception{
        HashMap<String, Object> map = new HashMap<>();
        JSONObject order = orderRankingService.getData();
        JSONObject product = orderRankingService.getProductData();
        List<ProductSaleRankingVo> saleRanking = productDataUtils.getSaleRanking(null);
        List<ProductVo> viewsRanking = productDataUtils.getViewsRanking(null);
        List<ProductRefundRankingVo> refundRanking = productDataUtils.getRefundRanking(null);
        map.put("order",order);
        map.put("product",product);
        map.put("saleRanking",saleRanking);
        map.put("viewsRanking",viewsRanking);
        map.put("refundRanking",refundRanking);
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @OperationLog(name = "order")
    @ApiOperation(value = "order", response = String.class)
    public ApiResult<Map<String,Object>> order(@Validated @RequestBody RankingParam rankingParam) throws Exception{
        return ApiResult.ok(orderRankingService.getOrderDataByDate(rankingParam.getStartDate(), rankingParam.getEndDate(),rankingParam.getType()));
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    @OperationLog(name = "product")
    @ApiOperation(value = "product", response = String.class)
    public ApiResult<Map<String,Object>> product(@Validated @RequestBody RankingParam rankingParam) throws Exception{
        return  ApiResult.ok(productDataUtils.getProductDataByDate(rankingParam.getStartDate(), rankingParam.getEndDate(), null));
    }

}
