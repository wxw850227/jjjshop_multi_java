package net.jjjshop.common.util.message;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderAddress;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.entity.order.OrderRefund;
import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.common.entity.settings.Message;
import net.jjjshop.common.entity.settings.MessageSettings;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.OrderPayTypeEnum;
import net.jjjshop.common.service.order.OrderAddressService;
import net.jjjshop.common.service.order.OrderProductService;
import net.jjjshop.common.service.settings.ExpressService;
import net.jjjshop.common.service.settings.MessageService;
import net.jjjshop.common.service.settings.MessageSettingsService;
import net.jjjshop.common.service.settings.RegionService;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.common.util.OrderRefundUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class MessageUtils {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageSettingsService messageSettingsService;
    @Autowired
    private WxMessageUtils wxMessageUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private SmsMessageUtils smsMessageUtils;
    @Autowired
    private OrderRefundUtils orderRefundUtils;

    /**
     * 支付成功消息
     */
    public void payment(Order order) {
        MessageSettings settings = this.getSettings("order_pay_user", order.getAppId());
        if (settings == null) {
            return;
        }
        User user = userService.getById(order.getUserId());
        List<OrderProduct> productList = orderProductService.list(new LambdaQueryWrapper<OrderProduct>()
                .eq(OrderProduct::getOrderId, order.getOrderId()));
        JSONObject data = new JSONObject();
        // 订单编号
        data.put("order_no", order.getOrderNo());
        String productName = productList.get(0).getProductName();
        if (productList.size() > 1) {
            productName += "等" + productList.size() + "个";
        }
        // 商品名称
        data.put("product_name", productName);
        // 订单金额
        data.put("pay_price", order.getPayPrice());
        // 支付方式
        data.put("pay_type", OrderPayTypeEnum.getName(order.getPayType()));
        // 支付时间
        data.put("pay_time", DateUtil.format(new Date(), "YYYY-MM-dd HH:mm:ss"));
        this.sendMessage(settings, data, user);
    }

    /**
     * 后台发货通知
     */
    @Async
    public void delivery(Order order) {
        MessageSettings settings = this.getSettings("order_delivery_user", order.getAppId());
        if (settings == null) {
            return;
        }
        User user = userService.getById(order.getUserId());
        List<OrderProduct> productList = orderProductService.list(new LambdaQueryWrapper<OrderProduct>()
                .eq(OrderProduct::getOrderId, order.getOrderId()));
        JSONObject data = new JSONObject();
        // 订单编号
        data.put("order_no", order.getOrderNo());
        String productName = productList.get(0).getProductName();
        if (productList.size() > 1) {
            productName += "等" + productList.size() + "个";
        }
        // 商品名称
        data.put("product_name", productName);
        // 收货人
        OrderAddress address = orderAddressService.getOne(new LambdaQueryWrapper<OrderAddress>()
                .eq(OrderAddress::getOrderId, order.getOrderId()));
        data.put("name", address.getName());
        // 收货地址
        data.put("address", regionService.getById(address.getProvinceId()).getName()
                + regionService.getById(address.getCityId()).getName()
                + regionService.getById(address.getRegionId()).getName()
                + address.getDetail());
        // 物流公司
        Express express = expressService.getById(order.getExpressId());
        data.put("express_name", express.getExpressName());
        // 物流单号
        data.put("express_no", order.getExpressNo());
        // 发货时间
        data.put("express_time", DateUtil.format(order.getDeliveryTime(), "YYYY-MM-dd HH:mm:ss"));
        this.sendMessage(settings, data, user);
    }

    /**
     * 后台售后单状态通知
     */
    @Async
    public void refund(OrderRefund orderRefund, String orderNo) {
        MessageSettings settings = this.getSettings("order_refund_user", orderRefund.getAppId());
        if (settings == null) {
            return;
        }
        User user = userService.getById(orderRefund.getUserId());
        List<OrderProduct> productList = orderProductService.list(new LambdaQueryWrapper<OrderProduct>()
                .eq(OrderProduct::getOrderId, orderRefund.getOrderId()));
        JSONObject data = new JSONObject();
        // 订单编号
        data.put("order_no", orderNo);
        String productName = productList.get(0).getProductName();
        if (productList.size() > 1) {
            productName += "等" + productList.size() + "个";
        }
        // 商品名称
        data.put("product_name", productName);
        // 售后类型
        data.put("type", orderRefundUtils.getTypeAttr(orderRefund.getType()));
        // 处理结果
        data.put("status", orderRefundUtils.getIsAgreeAttr(orderRefund.getIsAgree()));
        // 处理时间
        data.put("process_time", DateUtil.format(new Date(), "YYYY-MM-dd HH:mm:ss"));
        // 拒绝原因
        data.put("refuse_desc", StringUtils.isNotEmpty(orderRefund.getRefuseDesc())?orderRefund.getRefuseDesc():"无");
        this.sendMessage(settings, data, user);
    }

    /**
     * 获取设置
     *
     * @param name
     * @return
     */
    private MessageSettings getSettings(String name, Integer appId) {
        Message message = messageService.getOne(new LambdaQueryWrapper<Message>().eq(Message::getMessageEname, name));
        return messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                .eq(MessageSettings::getMessageId, message.getMessageId()).eq(MessageSettings::getAppId, appId));
    }

    private void sendMessage(MessageSettings settings, JSONObject data, User user) {
        // 发送小程序订阅消息
        if (settings.getWxStatus() == 1 && StringUtils.isNotBlank(user.getOpenId())) {
            wxMessageUtils.send(data, settings.getWxTemplate(), user.getOpenId(), Long.valueOf(user.getAppId()));
        }
        // 发送短信消息
        if (settings.getSmsStatus() == 1 && StringUtils.isNotBlank(user.getMobile())) {
            smsMessageUtils.send(data, settings.getSmsTemplate(), user.getMobile(), Long.valueOf(user.getAppId()));
        }
    }
}
