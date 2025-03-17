package net.jjjshop.front.service.order.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.*;
import net.jjjshop.common.entity.product.ProductImage;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.*;
import net.jjjshop.common.factory.order.checkpay.CheckPayFactory;
import net.jjjshop.common.factory.paysuccess.type.PaySuccessTypeFactory;
import net.jjjshop.common.factory.product.ProductFactory;
import net.jjjshop.common.factory.product.vo.UpdateProductStockVo;
import net.jjjshop.common.service.product.ProductImageService;
import net.jjjshop.common.settings.vo.TradeVo;
import net.jjjshop.common.util.OrderUtils;
import net.jjjshop.common.util.PayUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.util.UserUtils;
import net.jjjshop.common.vo.order.PayDataVo;
import net.jjjshop.common.vo.user.UserAddressVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.mapper.order.OrderMapper;
import net.jjjshop.front.param.order.OrderBuyParam;
import net.jjjshop.front.param.order.OrderCreateParam;
import net.jjjshop.front.param.order.OrderPageParam;
import net.jjjshop.front.param.order.OrderPayParam;
import net.jjjshop.front.service.app.AppService;
import net.jjjshop.front.service.order.*;
import net.jjjshop.front.service.store.StoreOrderService;
import net.jjjshop.front.service.supplier.SupplierService;
import net.jjjshop.front.service.user.UserAddressService;
import net.jjjshop.front.service.user.UserOrderService;
import net.jjjshop.front.vo.order.OrderDetailVo;
import net.jjjshop.front.vo.order.OrderListVo;
import net.jjjshop.front.vo.product.ProductBuyVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private PaySuccessTypeFactory paySuccessTypeFactory;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
    private OrderExtractService orderExtractService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductFactory productFactory;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private CheckPayFactory checkPayFactory;
    @Autowired
    private OrderUtils orderUtils;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private UserOrderService userOrderService;
    @Autowired
    private StoreOrderService storeOrderService;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private PayUtils payUtils;
    @Autowired
    private OrderTradeService orderTradeService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private AppService appService;

    /**
     * 创建订单
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer createOrder(OrderCreateParam params) {
        // 保存订单主表
        Order order = this.addOrder(params);
        // 保存地址
        if (params.getSupplier().getOrderData().getDelivery().intValue() == DeliveryTypeEnum.EXPRESS.getValue()) {
            // 记录收货地址
            this.saveOrderAddress(params.getCommonOrderData().getAddress(), order.getOrderId());
        } else if (params.getSupplier().getOrderData().getDelivery().intValue() == DeliveryTypeEnum.EXTRACT.getValue()) {
            // 记录自提信息
            this.saveOrderExtract(params, order.getOrderId());
        }
        // 保存商品
        this.saveOrderProduct(params, order.getOrderId());
        // 更新商品库存 (针对下单减库存的商品)
        productFactory.getFactory(params.getOrderSource().getSource()).updateProductStock(this.transStockVo(params.getSupplier().getProductList()));
        // 积分抵扣情况下扣除用户积分
        if (params.getCommonOrderData().getIsAllowPoints()
                && params.getCommonOrderData().getIsUsePoints()
                && params.getSupplier().getOrderData().getPointsNum() > 0) {
            String description = String.format("用户消费：%s", order.getOrderNo());
            userUtils.setIncPoints(params.getUser().getUserId(), -params.getSupplier().getOrderData().getPointsNum(), description, true);
        }
        return order.getOrderId();
    }

    private List<UpdateProductStockVo> transStockVo(List<ProductBuyVo> productList) {
        // 转成vo
        return productList.stream().map(e -> {
            UpdateProductStockVo vo = new UpdateProductStockVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    private Map<String,String> getLastExtract(Integer userId){
        Map<String,String> result = new HashMap<>();
        result.put("linkman", "");
        result.put("phone", "");
        return result;
    }

    /**
     * 保存订单主表
     * @param params
     * @return
     */
    private Order addOrder(OrderCreateParam params) {
        Order order = new Order();
        BeanUtils.copyProperties(params.getSupplier().getOrderData(), order);
        order.setUserId(params.getUser().getUserId());
        order.setOrderNo(OrderUtils.geneOrderNo(params.getUser().getUserId()));
        order.setTotalPrice(params.getSupplier().getOrderData().getOrderTotalPrice());
        order.setPayPrice(params.getSupplier().getOrderData().getOrderPayPrice());
        order.setDeliveryType(params.getSupplier().getOrderData().getDelivery());
        order.setBuyerRemark(params.getOrderBuyParam().getRemark());
        order.setOrderSource(params.getOrderSource().getSource());
        order.setPointsBonus(params.getSupplier().getOrderData().getPointsBonus());
        order.setVirtualAuto(params.getSupplier().getProductList().get(0).getVirtualAuto());
        order.setExpressPrice(params.getSupplier().getOrderData().getExpressPrice());
        order.setShopSupplierId(params.getSupplier().getShopSupplierId());

        //订单备注
        OrderBuyParam.StoreData  storeData = params.getOrderBuyParam().getSupplier().get(params.getSupplier().getShopSupplierId());
        if(storeData != null && StringUtils.isNotEmpty(storeData.getRemark())){
            order.setBuyerRemark(storeData.getRemark());
        }

        // 自提
        if (order.getDeliveryType().intValue() == DeliveryTypeEnum.EXTRACT.getValue().intValue()) {
            if(params.getSupplier().getOrderData().getExtractStore() == null){
                throw new BusinessException("配送地址不能为空");
            }
            order.setExtractStoreId(params.getSupplier().getOrderData().getExtractStore().getStoreId());
        }
        // 积分处理
        if(!params.getOrderBuyParam().getIsUsePoints()){
            order.setPointsNum(0);
            order.setPointsMoney(new BigDecimal(0));
        }
        // 结束时间随主订单配置
        JSONObject vo = settingUtils.getSetting(SettingEnum.TRADE.getKey(), null);
        TradeVo tradeVo = JSONObject.toJavaObject(vo, TradeVo.class);
        int closeDays = tradeVo.getCloseDays();
        //自动关闭时间类型,1=天,2=小时,3=分钟
        int closeType = tradeVo.getCloseType();
        if (closeDays != 0) {
            if(closeType == 1){
                order.setPayEndTime(DateUtil.offsetDay(new Date(), closeDays));
            }else if(closeType == 2){
                order.setPayEndTime(DateUtil.offsetHour(new Date(), closeDays));
            }else if(closeType == 3){
                order.setPayEndTime(DateUtil.offsetMinute(new Date(), closeDays));
            }
        }
        this.save(order);
        return order;
    }

    /**
     * 保存地址
     * @param addressVo
     * @param orderId
     * @return
     */
    private void saveOrderAddress(UserAddressVo addressVo, Integer orderId) {
        OrderAddress address = new OrderAddress();
        BeanUtils.copyProperties(addressVo, address);
        address.setOrderId(orderId);
        orderAddressService.save(address);
    }

    /**
     * 保存商品
     * @param params
     * @param orderId
     * @return
     */
    private void saveOrderProduct(OrderCreateParam params, Integer orderId) {
        List<OrderProduct> list = new ArrayList<>();
        for (ProductBuyVo vo : params.getSupplier().getProductList()) {
            OrderProduct product = new OrderProduct();
            BeanUtils.copyProperties(vo, product);
            BeanUtils.copyProperties(vo.getProductSku(), product);
            product.setUserId(params.getUser().getUserId());
            List<ProductImage> imageList = productImageService.list(new LambdaQueryWrapper<ProductImage>()
                    .eq(ProductImage::getProductId, vo.getProductId())
                    .eq(ProductImage::getImageType, 0).orderByAsc(ProductImage::getId));
            product.setImageId(imageList.get(0).getImageId());
            product.setOrderId(orderId);
            list.add(product);
        }
        orderProductService.saveBatch(list);
    }

    /**
     * 保存上门自提联系人
     * @param params
     * @param orderId
     */
    private void saveOrderExtract(OrderCreateParam params, Integer orderId) {
        OrderExtract extract = new OrderExtract();
        extract.setOrderId(orderId);
        extract.setUserId(params.getUser().getUserId());
        UserAddressVo address = params.getCommonOrderData().getAddress();
        extract.setLinkman(address.getName());
        extract.setPhone(address.getPhone());
        orderExtractService.save(extract);
    }

    /**
     * 用户订单信息
     * @param orderId
     * @param userId
     * @return
     */
    public Order getUserOrderDetail(Integer orderId, Integer userId) {
        Order order = this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderId, orderId)
                .eq(Order::getUserId, userId));
        if (order == null) {
            throw new BusinessException("用户订单信息不存在");
        }
        return order;
    }

    /**
     * 总支付金额
     * @param orderId
     * @param userId
     * @return
     */
    public BigDecimal getTotalPayMoney(String orderId, Integer userId){
        String[] orderIds = orderId.split(",");
        BigDecimal payMoney = BigDecimal.ZERO;
        for(String id:orderIds){
            Order order = this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderId, Integer.parseInt(id))
                    .eq(Order::getUserId, userId));
            if (order == null) {
                throw new BusinessException("用户订单信息不存在");
            }
            payMoney = payMoney.add(order.getPayPrice());
        }
        return payMoney;
    }

    /**
     * 获取支付参数
     * @param orderPayParam
     * @param user
     * @return
     */
    public Map<String, Object> orderPay(OrderPayParam orderPayParam, User user) {
        Map<String, Object> result = new HashMap<>();
        // 是否多订单
        List<Integer> orderIds = Arrays.stream(orderPayParam.getOrderId().split(",")).map(Integer::parseInt).collect(Collectors.toList());
        // 在线支付金额
        BigDecimal onlineMoney = this.getTotalPayPrice(orderIds, orderPayParam, user);
        String tradeNo = OrderUtils.geneOrderNo(user.getUserId());
        // 初始化交易
        this.initOrderPay(orderIds, tradeNo);
        // 如果支付金额为0，则强制使用余额
        if (onlineMoney.compareTo(BigDecimal.ZERO) == 0) {
            orderPayParam.setUseBalance(true);
        }
        // 余额支付标记订单已支付
        if (orderPayParam.getUseBalance()) {
            onlineMoney = this.paymentByBalance(orderIds, user, orderPayParam, onlineMoney, tradeNo);
        } else {
            if(orderIds.size() == 1){
                this.update(new LambdaUpdateWrapper<Order>().eq(Order::getOrderId, orderIds.get(0))
                        .set(Order::getOnlineMoney, onlineMoney));
            }else{
                orderTradeService.update(new LambdaUpdateWrapper<OrderTrade>()
                        .in(OrderTrade::getOrderId, orderIds)
                        .eq(OrderTrade::getPayStatus, 10)
                        .set(OrderTrade::getOnlineMoney, onlineMoney));
            }
        }
        Object payment = null;
        // 如果需要在线支付
        if (onlineMoney.compareTo(BigDecimal.ZERO) > 0) {
            try {
                int multiple = orderIds.size() > 1?1:0;
                // 微信支付
                if(OrderPayTypeEnum.WECHAT.getValue().intValue() == orderPayParam.getPayType().intValue()){
                    payment = payUtils.onPaymentByWechat(user, tradeNo, onlineMoney,
                            orderPayParam.getPaySource(), result, OrderTypeEnum.MASTER.getValue(), multiple);
                    // 微信支付版本号
                    result.put("wxPayVersion", appService.getById(user.getAppId()).getWxPayKind());
                }else if(OrderPayTypeEnum.ALIPAY.getValue().intValue() == orderPayParam.getPayType().intValue()){
                    // 支付宝支付
                    payment = payUtils.onPaymentByAlipay(user, tradeNo, onlineMoney,
                            orderPayParam.getPaySource(),
                            OrderTypeEnum.MASTER.getValue(), multiple);
                }
            } catch (Exception e) {
                log.info("支付异常：", e.getMessage());
                throw new BusinessException("支付失败:"+e.getMessage());
            }
        }
        result.put("orderId", orderPayParam.getOrderId());
        result.put("payType", orderPayParam.getPayType());
        result.put("payment", payment);
        return result;
    }

    private BigDecimal getTotalPayPrice(List<Integer> orderIds, OrderPayParam orderPayParam, User user){
        BigDecimal payPrice = BigDecimal.ZERO;
        for(Integer orderId:orderIds){
            Order order = this.getUserOrderDetail(orderId, user.getUserId());
            // 判断订单状态
            if (order.getOrderStatus().intValue() != OrderStatusEnum.NORMAL.getValue().intValue()
                    || order.getPayStatus().intValue() != OrderPayStatusEnum.PENDING.getValue().intValue()) {
                throw new BusinessException("很抱歉，当前订单不合法，无法支付");
            }
            // 检查是否可以支付
            List<OrderProduct> orderProductList = orderProductService.getProductList(orderId);
            checkPayFactory.getFactory(order.getOrderSource()).checkOrderStatus(orderProductList);
            payPrice = payPrice.add(order.getPayPrice());
        }
        return payPrice;
    }

    /**
     * 初始化交易
     * @return
     */
    private void initOrderPay(List<Integer> orderIds, String tradeNo) {
        if(orderIds.size() == 1){
            Order initOrder = new Order();
            initOrder.setOrderId(orderIds.get(0));
            initOrder.setBalance(BigDecimal.ZERO);
            initOrder.setOnlineMoney(BigDecimal.ZERO);
            // 重新生成交易号
            initOrder.setTradeNo(tradeNo);
            this.updateById(initOrder);
            // 交易表
            orderTradeService.update(new LambdaUpdateWrapper<OrderTrade>()
                    .in(OrderTrade::getOrderId, orderIds)
                    .eq(OrderTrade::getPayStatus, 10)
                    .set(OrderTrade::getBalance, 0)
                    .set(OrderTrade::getOnlineMoney, 0)
                    .set(OrderTrade::getOutTradeNo, tradeNo));
        }else{
            orderTradeService.update(new LambdaUpdateWrapper<OrderTrade>()
                    .in(OrderTrade::getOrderId, orderIds)
                    .eq(OrderTrade::getPayStatus, 10)
                    .set(OrderTrade::getBalance, 0)
                    .set(OrderTrade::getOnlineMoney, 0)
                    .set(OrderTrade::getOutTradeNo, tradeNo));
        }
    }

    /**
     * 余额支付
     * @param orderIds
     * @param user
     * @param orderPayParam
     * @return
     */
    private BigDecimal paymentByBalance(List<Integer> orderIds, User user, OrderPayParam orderPayParam,
                                        BigDecimal payPrice, String tradeNo) {
        if (user.getBalance().compareTo(payPrice) >= 0) {
            orderPayParam.setPayType(OrderPayTypeEnum.BALANCE.getValue());
            JSONObject attach = new JSONObject();
            if(orderIds.size() == 1){
                this.update(new LambdaUpdateWrapper<Order>().eq(Order::getOrderId, orderIds.get(0))
                        .set(Order::getBalance, payPrice));
                attach.put("multiple", 0);
            }else{
                orderTradeService.update(new LambdaUpdateWrapper<OrderTrade>()
                        .in(OrderTrade::getOrderId, orderIds)
                        .eq(OrderTrade::getPayStatus, 10)
                        .set(OrderTrade::getBalance, payPrice));
                attach.put("multiple", 1);
            }
            PayDataVo data = new PayDataVo();
            attach.put("paySource", orderPayParam.getPaySource());
            data.setAttach(attach);
            this.onPaymentByBalance(tradeNo, data);
            return BigDecimal.ZERO;
        } else {
            BigDecimal onlineMoney = payPrice.subtract(user.getBalance());
            if(orderIds.size() == 1) {
                this.update(new LambdaUpdateWrapper<Order>().eq(Order::getOrderId, orderIds.get(0))
                        .set(Order::getBalance, user.getBalance())
                        .set(Order::getOnlineMoney, onlineMoney));
            }else{
                orderTradeService.update(new LambdaUpdateWrapper<OrderTrade>()
                        .in(OrderTrade::getOrderId, orderIds)
                        .eq(OrderTrade::getPayStatus, 10)
                        .set(OrderTrade::getBalance, user.getBalance())
                        .set(OrderTrade::getOnlineMoney, onlineMoney));
            }
            return onlineMoney;
        }
    }

    /**
     * 余额支付标记订单已支付
     * @param tradeNo
     * @param data
     * @return
     */
    public void onPaymentByBalance(String tradeNo, PayDataVo data) {
        // 发起余额支付
        paySuccessTypeFactory.getFactory(OrderTypeEnum.MASTER.getValue()).onPaySuccess(tradeNo, 10, OrderPayTypeEnum.BALANCE.getValue(), data);
    }

    /**
     * 订单列表
     * @param orderPageParam
     * @return
     */
    public Paging<OrderListVo> getList(OrderPageParam orderPageParam) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        Integer userId = orderPageParam.getUserId();
        switch (orderPageParam.getType()) {
            case "all":
                break;
            case "payment":
                wrapper.eq(Order::getPayStatus, OrderPayStatusEnum.PENDING.getValue());
                wrapper.eq(Order::getOrderStatus, 10);
                break;
            case "delivery":
                wrapper.eq(Order::getPayStatus, OrderPayStatusEnum.SUCCESS.getValue());
                wrapper.eq(Order::getOrderStatus, 10);
                wrapper.eq(Order::getDeliveryStatus, 10);
                break;
            case "received":
                wrapper.eq(Order::getPayStatus, OrderPayStatusEnum.SUCCESS.getValue());
                wrapper.eq(Order::getOrderStatus, 10);
                wrapper.eq(Order::getDeliveryStatus, 20);
                wrapper.eq(Order::getReceiptStatus, 10);
                break;
            case "comment":
                wrapper.eq(Order::getIsComment, 0);
                wrapper.eq(Order::getOrderStatus, 30);
                break;
            case "cancel":
                wrapper.and(i->i.or().eq(Order::getOrderStatus, 20)
                        .or().eq(Order::getOrderStatus, 21));
                break;
        }
        if(orderPageParam.getShopSupplierId() != null && orderPageParam.getShopSupplierId() > 0) {
            wrapper.eq(Order::getShopSupplierId, orderPageParam.getShopSupplierId());
        }
        wrapper.eq(Order::getIsDelete, 0);
        wrapper.eq(Order::getUserId, userId);
        wrapper.orderByDesc(Order::getCreateTime);
        Page page = new PageInfo(orderPageParam);
        IPage<Order> ipage = this.page(page, wrapper);
        IPage result = ipage.convert(item -> {
            OrderListVo vo = new OrderListVo();
            BeanUtils.copyProperties(item, vo);
            //设置各种Text
            vo.setPayTypeText(OrderPayTypeEnum.getName(vo.getPayType()));
            vo.setPayStatusText(OrderPayStatusEnum.getName(vo.getPayStatus()));
            vo.setDeliveryTypeText(DeliveryTypeEnum.getName(vo.getDeliveryType()));
            //10未收货 20已收货
            vo.setReceiptStatusText(vo.getReceiptStatus() == 10 ? "未收货" : "已收货");
            //设置发货状态
            vo.setDeliveryStatusText(vo.getDeliveryStatus() == 10 ? "未发货" : "已发货");
            //设置后台修改价格
            if (vo.getUpdatePrice().compareTo(BigDecimal.ZERO) > 0) {
                vo.setUpdatePriceSymbol("+");
            } else if (vo.getUpdatePrice().compareTo(BigDecimal.ZERO) < 0) {
                vo.setUpdatePriceSymbol("-");
            } else {
                vo.setUpdatePriceSymbol("");
            }
            //设置订单详情中商品信息
            vo.setProduct(orderProductService.getProductVoList(vo.getOrderId()));
            //设置订单状态文本
            vo.setOrderStatusText(OrderUtils.getOrderStatusText(item));
            //设置订单来源文本
            vo.setOrderSourceText(OrderSourceEnum.getName(vo.getOrderSource()));
            //设置订单的商品总数
            vo.getProduct().stream().forEach(e -> {
                if (vo.getTotalNum() != null) {
                    vo.setTotalNum(vo.getTotalNum() + e.getTotalNum());
                } else {
                    vo.setTotalNum(e.getTotalNum());
                }
            });
            if(vo.getShopSupplierId() != null && vo.getShopSupplierId()>0){
                vo.setSupplierName(supplierService.getById(vo.getShopSupplierId()).getName());
            }
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 用户取消订单
     * @param order
     * @return
     */
    public Boolean cancelOrder(Order order) {
        if (order.getDeliveryStatus() == 20) {
            throw new BusinessException("已发货订单不可取消");
        }
        if (order.getOrderStatus() != 10) {
            throw new BusinessException("订单状态不允许取消");
        }
        //订单是否已经付款
        boolean isPay = order.getPayStatus().intValue() == OrderPayStatusEnum.SUCCESS.getValue().intValue();
        if (!isPay) {
            //商品回退库存
            List<OrderProduct> productList = orderProductService.getProductList(order.getOrderId());
            productFactory.getFactory(order.getOrderSource()).backProductStock(this.transStockOrderProductVo(productList), false);
        }
        if(order.getPointsMoney().compareTo(BigDecimal.ZERO) > 0){
            userUtils.setIncPoints(order.getUserId(), order.getPointsNum() ,"用户取消订单回退积分",null);
        }
        //更新订单状态
        Order updateBean = new Order();
        updateBean.setOrderId(order.getOrderId());
        order.setOrderStatus(isPay ? OrderStatusEnum.APPLY_CANCEL.getValue() : OrderStatusEnum.CANCELLED.getValue());
        return this.updateById(order);
    }

    /**
     * 用户确认收货
     * @param orderId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean receipt(Integer orderId) {
        Order order = this.getById(orderId);
        if (order.getDeliveryStatus() != 20 || order.getReceiptStatus() != 10) {
            throw new BusinessException("该订单不合法");
        }
        Order updateOrder = new Order();
        updateOrder.setOrderId(order.getOrderId());
        updateOrder.setReceiptStatus(20);
        updateOrder.setReceiptTime(new Date());
        updateOrder.setOrderStatus(30);
        this.updateById(updateOrder);
        //执行订单完成后操作
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        orderUtils.complete(orderList, order.getAppId());
        return true;

    }


    private List<UpdateProductStockVo> transStockOrderProductVo(List<OrderProduct> productList) {
        // 转成vo
        return productList.stream().map(e -> {
            UpdateProductStockVo vo = new UpdateProductStockVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取用户已购买商品数量
     * @param userId
     * @param productId
     * @return
     */
    public Integer getHasBuyOrderNum(Integer userId, Integer productId) {
        return orderMapper.getHasBuyOrderNum(userId, productId);
    }

    /**
     * 获取活动订单
     * 已付款，未取消
     * @param userId
     * @param productId
     * @return
     */
    public Integer getPlusOrderNum(Integer userId, Integer productId, Integer orderSource){
        return orderMapper.getPlusOrderNum(userId, productId, orderSource);
    }

    /**
     * 根据类型查询订单数量
     * @param userId
     * @param type
     * @return
     */
    public Integer getCount(Integer userId, String type) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        switch (type) {
            case "all":
                break;
            case "payment":
                wrapper.eq(Order::getPayStatus, OrderPayStatusEnum.PENDING.getValue());
                wrapper.eq(Order::getOrderStatus, 10);
                break;
            case "delivery":
                wrapper.eq(Order::getPayStatus, OrderPayStatusEnum.SUCCESS.getValue());
                wrapper.eq(Order::getOrderStatus, 10);
                wrapper.eq(Order::getDeliveryStatus, 10);
                break;
            case "received":
                wrapper.eq(Order::getPayStatus, OrderPayStatusEnum.SUCCESS.getValue());
                wrapper.eq(Order::getOrderStatus, 10);
                wrapper.eq(Order::getDeliveryStatus, 20);
                wrapper.eq(Order::getReceiptStatus, 10);
                break;
            case "comment":
                wrapper.eq(Order::getIsComment, 0);
                wrapper.eq(Order::getOrderStatus, 30);
                break;
        }
        wrapper.eq(Order::getUserId, userId);
        wrapper.eq(Order::getIsDelete, 0);
        wrapper.ne(Order::getOrderStatus, 20);
        return this.count(wrapper);
    }

    /**
     * 通过订单编号获取订单详情
     * @param orderNo
     * @return
     */
    public OrderDetailVo detailByNo(String orderNo) {
        Order order = this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
        OrderDetailVo vo = userOrderService.transOrderDetailVo(order);
        vo.setProduct(orderProductService.getProductVoList(order.getOrderId()));
        return vo;
    }

    /**
     * 通过订单Id获取订单详情
     * @param orderId
     * @return
     */
    public OrderDetailVo detail(Integer orderId) {
        Order order = this.getById(orderId);
        OrderDetailVo vo = userOrderService.transOrderDetailVo(order);
        vo.setProduct(orderProductService.getProductVoList(order.getOrderId()));
        return vo;
    }

    /**
     * 订单核销
     * @param orderId
     * @param extractClerkId
     * @return
     */
    public Boolean verificationOrder(Integer orderId, Integer extractClerkId) {
        Order order = this.getById(orderId);
        if (order.getPayStatus() != 20
                || order.getDeliveryType() != DeliveryTypeEnum.EXTRACT.getValue()
                || order.getDeliveryStatus() == 20
                || order.getOrderStatus() == 20
                || order.getOrderStatus() == 21
        ) {
            throw new BusinessException("该订单不满足核销条件");
        }
        order.setExtractClerkId(extractClerkId);
        order.setDeliveryStatus(20);
        order.setDeliveryTime(new Date());
        order.setReceiptStatus(20);
        order.setReceiptTime(new Date());
        order.setOrderStatus(30);
        boolean update = this.updateById(order);
        //新增核销记录
        storeOrderService.add(order.getOrderId(), order.getExtractStoreId(), order.getExtractClerkId(), OrderTypeEnum.MASTER.getValue(), order.getShopSupplierId());
        //完成订单完成后流程
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        orderUtils.complete(orderList, order.getAppId());
        return update;
    }

    /**
     * 累计成交数
     */
    public Integer getTotalPayOrder(Integer shopSupplierId) {
        return this.count(new LambdaQueryWrapper<Order>()
                .eq(Order::getShopSupplierId, shopSupplierId)
                .eq(Order::getPayStatus, 20)
                .in(Order::getOrderStatus, new Integer[]{10, 30}));
    }

    /**
     * 累计当天成交数
     */
    public Integer getTodayPayOrder(Integer shopSupplierId) {
        Date time = new Date();
        String startTime = DateUtil.format(time, "yyyy-MM-dd 00:00:00");
        String endTime = DateUtil.format(time, "yyyy-MM-dd 23:59:59");
        return this.count(new LambdaQueryWrapper<Order>()
                .eq(Order::getShopSupplierId, shopSupplierId)
                .eq(Order::getPayStatus, 20)
                .in(Order::getOrderStatus, new Integer[]{10, 30})
                .between(Order::getCreateTime, startTime, endTime));
    }

}
