package net.jjjshop.supplier.service.statistics.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierService;
import net.jjjshop.common.util.OrderDataUtils;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.service.order.OrderRefundService;
import net.jjjshop.supplier.service.order.OrderService;
import net.jjjshop.supplier.service.product.ProductCommentService;
import net.jjjshop.supplier.service.product.ProductService;
import net.jjjshop.supplier.service.statistics.HomeDataService;
import net.jjjshop.supplier.service.supplier.SupplierUserService;
import net.jjjshop.supplier.service.user.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 * 首页统计数据 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */

@Slf4j
@Service
public class HomeDataServiceImpl implements HomeDataService {
    @Autowired
    private ProductCommentService productCommentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDataUtils orderDataUtils;
    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierUserService supplierUserService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private UserFavoriteService userFavoriteService;

    /**
     * 获取首页显示数据
     * @param
     * @return
     */
    public JSONObject getHomeData() throws ParseException {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        Integer shopSupplierId = user.getShopSupplierId();
        Supplier supplier = supplierService.getById(shopSupplierId);
        //获取今天时间
        String today = DateUtil.format(DateUtil.offsetDay(new Date(), 0), "yyyy-MM-dd");
        //获取昨天时间
        String yesterday = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");
        //获取七天前的时间
        String lately7days = DateUtil.format(DateUtil.offsetDay(new Date(), -7), "yyyy-MM-dd");

        // 商品总量
        Integer productTotal = productService.getProductTotal(shopSupplierId);
        // 订单总量
        Integer orderTotal = Integer.parseInt(orderDataUtils.getOrderData(null,null,"order_total",shopSupplierId).toString());
        // 订单销售额
        BigDecimal totalMoney = orderDataUtils.getOrderData(today, null, "order_total_price",shopSupplierId);
        // 评价总量
        Integer productCommentTotal = productCommentService.getProductCommentTotal(shopSupplierId);

        // 销售额(元)
        BigDecimal orderTotalPriceT = orderDataUtils.getOrderData(today, null, "order_total_price",shopSupplierId);
        BigDecimal orderTotalPriceY = orderDataUtils.getOrderData(yesterday, null, "order_total_price",shopSupplierId);

        // 支付订单数
        Integer orderTotalT = Integer.parseInt(orderDataUtils.getOrderData(today, null, "order_total",shopSupplierId).toString());
        Integer orderTotalY = Integer.parseInt(orderDataUtils.getOrderData(yesterday, null, "order_total",shopSupplierId).toString());

        //下单用户数
        Integer payOrderUserTotalT = orderService.getPayOrderUserTotal(today,shopSupplierId);
        Integer payOrderUserTotalY = orderService.getPayOrderUserTotal(yesterday,shopSupplierId);

        //店铺关注数
        Integer favUserTotalT = userFavoriteService.getUserTotal(today, shopSupplierId);
        Integer favUserTotalY = userFavoriteService.getUserTotal(yesterday, shopSupplierId);

        // 最近七天日期订单交易数
        Integer orderTotal7 = Integer.parseInt(orderDataUtils.getOrderData(lately7days, today, "order_total",shopSupplierId).toString());
        //最近七天订单交易金额
        BigDecimal orderTotalPrice7 = orderDataUtils.getOrderData(lately7days, today, "order_total_price",shopSupplierId);
        //待处理订单
        Integer reviewOrderTotal = orderService.getReviewOrderTotal(shopSupplierId);
        //获取售后订单总量
        Integer refundTotal = orderRefundService.getRefundTotal(shopSupplierId);
        //获取提现订单总量
        Integer cardOrderTotal = orderService.getCardOrderTotal(shopSupplierId);
        //未审核评论
        Integer reviewCommentTotal = productCommentService.getReviewCommentTotal(shopSupplierId);
        //库存告急商品
        Integer productStockTotal = productService.getProductStockTotal(shopSupplierId);

        JSONObject result = new JSONObject();
        result.put("productTotal", productTotal);
        result.put("orderTotal", orderTotal);
        result.put("productCommentTotal", productCommentTotal);
        //店铺关注数量
        result.put("favCount", supplier.getFavCount());
        result.put("expressScore", supplier.getExpressScore());
        result.put("serverScore", supplier.getServerScore());
        result.put("describeScore", supplier.getDescribeScore());
        result.put("totalMoney", totalMoney);

        result.put("orderTotalPriceT", orderTotalPriceT);
        result.put("orderTotalPriceY", orderTotalPriceY);

        result.put("orderTotalT", orderTotalT);
        result.put("orderTotalY", orderTotalY);
        result.put("payOrderUserTotalT", payOrderUserTotalT);
        result.put("payOrderUserTotalY", payOrderUserTotalY);
        result.put("favUserTotalT", favUserTotalT);
        result.put("favUserTotalY", favUserTotalY);

        result.put("orderTotal7", orderTotal7);
        result.put("orderTotalPrice7", orderTotalPrice7);
        result.put("reviewOrderTotal", reviewOrderTotal);
        result.put("refundTotal", refundTotal);
        result.put("cardOrderTotal", cardOrderTotal);
        result.put("reviewCommentTotal", reviewCommentTotal);
        result.put("productStockTotal", productStockTotal);

        return result;

    }
}
