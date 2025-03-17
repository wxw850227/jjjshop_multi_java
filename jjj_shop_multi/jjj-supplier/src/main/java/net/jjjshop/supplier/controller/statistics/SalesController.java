package net.jjjshop.supplier.controller.statistics;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.util.OrderDataUtils;
import net.jjjshop.common.util.ProductDataUtils;
import net.jjjshop.common.vo.product.ProductVo;
import net.jjjshop.common.vo.statistics.ProductRefundRankingVo;
import net.jjjshop.common.vo.statistics.ProductSaleRankingVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.statistics.RankingParam;
import net.jjjshop.supplier.service.order.OrderService;
import net.jjjshop.supplier.service.supplier.SupplierUserService;
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
@RequestMapping("/supplier/statistics/sales")
public class SalesController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private SupplierUserService supplierUserService;
    @Autowired
    private ProductDataUtils productDataUtils;
    @Autowired
    private OrderDataUtils orderDataUtils;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index() throws Exception{
        Map<String,Object> result = new HashMap<>();
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        JSONObject order = orderDataUtils.getData(user.getShopSupplierId());
        JSONObject product = productDataUtils.getProductData(user.getShopSupplierId());
        List<ProductSaleRankingVo> productSaleRanking = productDataUtils.getSaleRanking(user.getShopSupplierId());
        List<ProductVo> productViewsRanking = productDataUtils.getViewsRanking(user.getShopSupplierId());
        List<ProductRefundRankingVo> productRefundRanking = productDataUtils.getRefundRanking(user.getShopSupplierId());
        result.put("order", order);
        result.put("product", product);
        result.put("productSaleRanking", productSaleRanking);
        result.put("productViewsRanking", productViewsRanking);
        result.put("productRefundRanking", productRefundRanking);
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @OperationLog(name = "order")
    @ApiOperation(value = "order", response = String.class)
    public ApiResult<Map<String,Object>> order(@Validated @RequestBody RankingParam rankingParam) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        Map<String, Object> map = null;
        if("order".equals(rankingParam.getType())){
            map = orderService.getOrderDataByDate(rankingParam.getStartDate(), rankingParam.getEndDate(), user.getShopSupplierId());
        }else if("refund".equals(rankingParam.getType())) {
            map = orderService.getRefundDataByDate(rankingParam.getStartDate(), rankingParam.getEndDate(), user.getShopSupplierId());
        }
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    @OperationLog(name = "product")
    @ApiOperation(value = "product", response = String.class)
    public ApiResult<Map<String,Object>> product(@Validated @RequestBody RankingParam rankingParam) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        return  ApiResult.ok(productDataUtils.getProductDataByDate(rankingParam.getStartDate(), rankingParam.getEndDate(), user.getShopSupplierId()));
    }
}
