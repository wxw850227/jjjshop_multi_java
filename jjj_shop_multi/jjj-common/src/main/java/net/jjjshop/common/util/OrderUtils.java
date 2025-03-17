package net.jjjshop.common.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.entity.order.OrderRefund;
import net.jjjshop.common.entity.order.OrderSettled;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierCapital;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserPointsLog;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.factory.product.vo.UpdateProductStockVo;
import net.jjjshop.common.service.order.OrderProductService;
import net.jjjshop.common.service.order.OrderRefundService;
import net.jjjshop.common.service.order.OrderService;
import net.jjjshop.common.service.order.OrderSettledService;
import net.jjjshop.common.service.supplier.SupplierCapitalService;
import net.jjjshop.common.service.supplier.SupplierService;
import net.jjjshop.common.service.user.UserPointsLogService;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.common.settings.vo.TradeVo;
import net.jjjshop.common.vo.order.OrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderUtils {
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPointsLogService userPointsLogService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private SupplierUtils supplierUtils;
    @Autowired
    private OrderSettledService orderSettledService;
    @Autowired
    private SupplierCapitalService supplierCapitalService;
    @Autowired
    private SupplierService supplierService;
    /**
     * 生成订单号
     * @return
     */
    public static String geneOrderNo(Integer userId){

        String date = DateUtil.format(new Date(), "yyyyMMdd");

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (9999 - 1000 + 1)) + 1000;// 获取4位随机数

        //8位用户id
        int subStrLength = 8;
        String sUserId = userId.toString();
        int length = sUserId.length();
        String str;
        if (length >= subStrLength) {
            str = sUserId.substring(length - subStrLength, length);
        } else {
            str = String.format("%0" + subStrLength + "d", userId);
        }

        return date + str + rannum;// 当前时间 + 用户id + 随机数
    }

    /**
     * 设置订单状态
     * @param order
     */
    public static String getOrderStatusText(Order order){
        if(order.getOrderStatus() == 20){
            return "已取消";
        }
        if(order.getOrderStatus() == 30){
            return "已完成";
        }
        if(order.getPayStatus() == 10){
            return "待付款";
        }
        // 发货状态
        if (order.getDeliveryStatus() == 10) {
            return "已付款，待发货";
        }
        if (order.getReceiptStatus() == 10) {
            return "已发货，待收货";
        }
        return "";
    }

    /**
     * 通过订单id，查询商品
     * @return
     */
    public List<UpdateProductStockVo> getOrderProduct(Integer orderId){
        List<OrderProduct> list = orderProductService.list(new LambdaQueryWrapper<OrderProduct>().eq(OrderProduct::getOrderId, orderId));
        // 转成vo
        return list.stream().map(e -> {
            UpdateProductStockVo vo = new UpdateProductStockVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 订单完成
     */
    public void complete(List<Order> orderList, Integer appId){
        JSONObject vo = settingUtils.getSetting(SettingEnum.TRADE.getKey(), Long.valueOf(appId));
        TradeVo tradeVo = JSONObject.toJavaObject(vo, TradeVo.class);
        if(tradeVo.getRefundDays() == 0){
            this.settled(orderList);
        }
        for (Order order:orderList) {
            //增加商家销量
            if(order.getShopSupplierId() != null && order.getShopSupplierId() >0 ){
                supplierUtils.incProductSales(order.getShopSupplierId());
            }
        }
    }

    /**
     * 执行订单结算
     */
    public void settled(List<Order> orderList)
    {
        // 累积用户实际消费金额
        this.setIncUserData(orderList);
        // 订单id集
        List<Integer> orderIds = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());
        // 将订单设置为已结算
        orderService.update(new LambdaUpdateWrapper<Order>().in(Order::getOrderId, orderIds)
                .set(Order::getIsSettled, 1));
        // 供应商结算
        this.setIncSupplierMoney(orderList);
    }

    /**
     * 累积用户实际消费金额/赠送积分
     */
    private void setIncUserData(List<Order> orderList)
    {
        // 累计消费金额
        Map<Integer,BigDecimal> expendMoneyData = new HashMap<>();
        // 计算用户所得积分
        Map<Integer,Integer> pointsData = new HashMap<>();
        List<UserPointsLog> logList = new ArrayList<>();
        HashSet<Integer> gradeUpUserIds = new HashSet<>();
        // 计算并累积实际消费金额(需减去售后退款的金额)
        for (Order order:orderList) {
            gradeUpUserIds.add(order.getUserId());
            // 订单实际支付金额
            BigDecimal expendMoney = order.getPayPrice();
            Integer pointsBonus = order.getPointsBonus();
            List<OrderProduct> orderProductList = orderProductService.list(new LambdaQueryWrapper<OrderProduct>().eq(OrderProduct::getOrderId, order.getOrderId()));
            // 减去订单退款的金额
            for (OrderProduct product:orderProductList) {
                OrderRefund refund = orderRefundService.getOne(new LambdaQueryWrapper<OrderRefund>()
                        .eq(OrderRefund::getOrderId, order.getOrderId()).eq(OrderRefund::getOrderProductId, product.getOrderProductId()));
                // 售后类型：退货退款 // 商家审核：已同意
                if (refund != null && refund.getType() == 10 && refund.getIsAgree() == 10) {
                    expendMoney = expendMoney.subtract(refund.getRefundMoney());
                    pointsBonus = pointsBonus - product.getPointsBonus();
                }
            }
            if(expendMoney.compareTo(BigDecimal.ZERO) > 0){
                if(expendMoneyData.get(order.getUserId()) == null){
                    expendMoneyData.put(order.getUserId(), expendMoney);
                }else{
                    expendMoneyData.put(order.getUserId(), expendMoneyData.get(order.getUserId()).add(expendMoney));
                }
            }
            if(pointsBonus > 0){
                // 赠送积分
                if(pointsData.get(order.getUserId()) == null){
                    pointsData.put(order.getUserId(), pointsBonus);
                }else{
                    pointsData.put(order.getUserId(), pointsData.get(order.getUserId()) + pointsBonus);
                }
                // 积分日志
                UserPointsLog log = new UserPointsLog();
                log.setUserId(order.getUserId());
                log.setValue(pointsBonus);
                log.setAppId(order.getAppId());
                log.setDescription(String.format("订单赠送：%s", order.getOrderNo()));
                logList.add(log);
            }
        }
        // 累积到会员消费金额
        expendMoneyData.forEach((key, value) -> {
            userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, key)
                    .setSql("`expend_money` = `expend_money` + " + value));
        });
        // 累积会员积分
        pointsData.forEach((key, value) -> {
            userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, key)
                    .setSql("`points` = `points` + " + value));
        });
        //用户升级
        for (Integer id : gradeUpUserIds){
            userUtils.userGradeUpgrade(id);
        }
        // 批量增加积分日志
        userPointsLogService.saveBatch(logList);
    }

    /**
     * 供应商金额=支付金额-运费
     */
    private void setIncSupplierMoney(List<Order> orderList){
        // 计算并累积实际消费金额(需减去售后退款的金额)
        Map<Integer,BigDecimal> supplierData = new HashMap<>();
        List<SupplierCapital> supplierCapitalData = new ArrayList<>();
        // 订单结算记录
        List<OrderSettled> orderSettledData = new ArrayList<>();
        for(Order order:orderList){
            if(order.getShopSupplierId() == 0){
                continue;
            }
            // 供应价格+运费
            BigDecimal supplierMoney = order.getSupplierMoney();
            BigDecimal sysMoney = order.getSysMoney();
            // B2b模式，如果有参与分销，减去分销的佣金
            BigDecimal refundSupplierMoney = BigDecimal.ZERO;
            BigDecimal refundSysMoney = BigDecimal.ZERO;
            List<OrderProduct> orderProductList = orderProductService.list(new LambdaQueryWrapper<OrderProduct>().eq(OrderProduct::getOrderId, order.getOrderId()));
            // 减去订单退款的金额
            for (OrderProduct product:orderProductList) {
                OrderRefund refund = orderRefundService.getOne(new LambdaQueryWrapper<OrderRefund>()
                        .eq(OrderRefund::getOrderId, order.getOrderId()).eq(OrderRefund::getOrderProductId, product.getOrderProductId()));
                // 售后类型：退货退款 // 商家审核：已同意
                if (refund != null && refund.getType() == 10 && refund.getIsAgree() == 10) {
                    supplierMoney = supplierMoney.subtract(product.getSupplierMoney());
                    sysMoney = sysMoney.subtract(product.getSysMoney());
                    refundSupplierMoney = refundSupplierMoney.add(product.getSupplierMoney());
                    refundSysMoney = refundSysMoney.add(product.getSysMoney());
                }
            }
            if(supplierMoney.compareTo(BigDecimal.ZERO) > 0){
                if(supplierData.get(order.getShopSupplierId()) == null){
                    supplierData.put(order.getShopSupplierId(), supplierMoney);
                }else{
                    supplierData.put(order.getShopSupplierId(), supplierData.get(order.getShopSupplierId()).add(supplierMoney));
                }
            }
            // 订单结算表
            OrderSettled orderSettled = new OrderSettled();
            orderSettled.setOrderId(order.getOrderId());
            orderSettled.setShopSupplierId(order.getShopSupplierId());
            orderSettled.setOrderMoney(order.getOrderPrice());
            orderSettled.setPayMoney(order.getPayPrice());
            orderSettled.setExpressMoney(order.getExpressPrice());
            orderSettled.setSupplierMoney(order.getSupplierMoney());
            orderSettled.setRealSupplierMoney(supplierMoney);
            orderSettled.setSysMoney(order.getSysMoney());
            orderSettled.setRealSysMoney(sysMoney);
            orderSettled.setRefundMoney(refundSupplierMoney.add(refundSysMoney));
            orderSettled.setRefundSupplierMoney(refundSupplierMoney);
            orderSettled.setRealSysMoney(refundSysMoney);
            orderSettled.setAppId(order.getAppId());
            orderSettledData.add(orderSettled);

            // 商家结算记录
            SupplierCapital supplierCapital = new SupplierCapital();
            supplierCapital.setShopSupplierId(order.getShopSupplierId());
            supplierCapital.setMoney(supplierMoney);
            supplierCapital.setDescription("订单结算，订单号：" + order.getOrderNo());
            supplierCapital.setAppId(order.getAppId());
            supplierCapitalData.add(supplierCapital);
        }
        // 累积到供应商表记录
        supplierData.forEach((key, value) -> {
            if(value.compareTo(BigDecimal.ZERO) >= 0){
                supplierService.update(new LambdaUpdateWrapper<Supplier>().eq(Supplier::getShopSupplierId, key)
                        .setSql("`total_money` = `total_money` + " + value+",`money` = `money` + " + value));
            }
        });
        // 修改平台结算金额
        orderSettledService.saveBatch(orderSettledData);
        // 供应商结算明细金额
        supplierCapitalService.saveBatch(supplierCapitalData);
    }

    /**
     * 批量获取订单列表
     */
    public List<OrderVo> getListByIds(List<Integer> orderIds){
        List<Order> list = orderService.list(new LambdaQueryWrapper<Order>().in(Order::getOrderId, orderIds).eq(Order::getIsDelete,0));
        List<OrderVo> voList = list.stream().map(e -> {
            OrderVo vo = new OrderVo();
            BeanUtils.copyProperties(e, vo);
            vo.setProduct(orderProductService.list(new LambdaQueryWrapper<OrderProduct>()
                    .eq(OrderProduct::getOrderId, e.getOrderId())));
            vo.setRefund(orderRefundService.list(new LambdaQueryWrapper<OrderRefund>().eq(OrderRefund::getOrderId, e.getOrderId())));
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }


}
