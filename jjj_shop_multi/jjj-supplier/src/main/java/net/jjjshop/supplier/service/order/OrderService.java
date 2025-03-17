package net.jjjshop.supplier.service.order;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.order.*;
import net.jjjshop.supplier.vo.order.ExchangeVo;
import net.jjjshop.supplier.vo.order.OrderVo;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 订单记录表 服务类
 * @author jjjshop
 * @since 2022-07-04
 */
public interface OrderService extends BaseService<Order> {
    /**
     * 订单列表
     * @param orderPageParam
     * @return
     */
    Paging<OrderVo> getList(OrderPageParam orderPageParam, Integer shopSupplierId);

    /**
     * 单个订单信息
     * @param orderId
     * @return
     */
    OrderVo detail(Integer orderId);

    /**
     * 订单发货
     * @param orderParam
     * @return
     */
    Boolean delivery(OrderParam orderParam);

    /**
     * 审核：用户取消订单
     * @param orderParam
     * @return
     */
    Boolean confirmCancel(OrderParam orderParam);

    /**
     * 通过订单状态查询订单数量
     * @return order
     */
    Integer getCount(String dataType, Integer shopSupplierId);

    /**
     * 获取待处理订单总数
     * @param
     * @return
     */
    Integer getReviewOrderTotal(Integer shopSupplierId);

    /**
     * 获取提现订单总量
     * @param
     * @return
     */
    Integer getCardOrderTotal(Integer shopSupplierId);

    /**
     * 导出订单
     * @param orderPageParam
     * @param httpServletResponse
     * @return
     */
    void exportList(OrderPageParam orderPageParam, HttpServletResponse httpServletResponse, Integer shopSupplierId) throws Exception;

    /**
     * 确认核销订单
     * @param orderExtractParam
     * @return
     */
    Boolean verificationOrder(OrderExtractParam orderExtractParam);

    /**
     * 取消订单
     * @param orderCancelParam
     * @return
     */
    Boolean orderCancel(OrderCancelParam orderCancelParam);

    /**
     * 修改订单价格
     */
    Boolean updatePrice(OrderPriceParam orderPriceParam);

    /**
     * 获取所有运送方式
     * @param
     * @return
     */
    List<JSONObject> getDeliveryList();

    /**
     * 获取兑换记录
     * @param
     * @return
     */
    Paging<ExchangeVo> getExchange(ExchangePageParam exchangePageParam);


    /**
     * 通过时间范围获取商品统计数据
     * @param
     * @return
     */
    //通过时间范围获取商品统计数据
    Map<String, Object> getOrderDataByDate(String startDate, String endDate, Integer shopSupplierId) throws ParseException;

    /**
     * 获取未结算订单金额
     */
    BigDecimal getNoSettledMoney(Integer shopSupplierId);

    /**
     * 获取用户已付款订单总数
     * @param startDate
     * @return
     */
    Integer getPayOrderUserTotal(String startDate,Integer shopSupplierId) throws ParseException;

    /**
     * 通过时间范围获取商品统计数据
     * @param
     * @return
     */
    //通过时间范围获取商品统计数据
    Map<String, Object> getRefundDataByDate(String startDate, String endDate, Integer shopSupplierId) throws ParseException;

    /**
     * 虚拟商品发货
     * @param orderVirtualParam
     * @return
     */
    boolean virtual(OrderVirtualParam orderVirtualParam);
}
