package net.jjjshop.front.util.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderTrade;
import net.jjjshop.common.entity.settings.DeliveryRule;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.OrderSourceEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.service.user.UserGradeService;
import net.jjjshop.common.settings.vo.FullFreeVo;
import net.jjjshop.common.settings.vo.PointsVo;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.settings.vo.TradeVo;
import net.jjjshop.common.util.OrderUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.front.param.order.OrderBuyParam;
import net.jjjshop.front.param.order.OrderCreateParam;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.order.OrderTradeService;
import net.jjjshop.front.service.product.ProductCategoryService;
import net.jjjshop.front.service.settings.DeliveryRuleService;
import net.jjjshop.front.service.settings.DeliveryService;
import net.jjjshop.front.service.shop.ShopFullreduceService;
import net.jjjshop.front.service.store.StoreService;
import net.jjjshop.front.service.user.UserAddressService;
import net.jjjshop.front.util.order.vo.CommonOrderData;
import net.jjjshop.front.util.order.vo.OrderData;
import net.jjjshop.front.util.order.vo.OrderSource;
import net.jjjshop.front.util.order.vo.SettledRule;
import net.jjjshop.front.vo.product.ProductBuyVo;
import net.jjjshop.front.vo.supplier.SupplierBuyVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单结算父类
 */
@Slf4j
@Component
@Scope("prototype")
public class OrderSettledUtils {
    @Autowired
    private OrderService orderService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private DeliveryRuleService deliveryRuleService;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private UserGradeService userGradeService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ShopFullreduceService shopFullreduceService;
    @Autowired
    private OrderTradeService orderTradeService;

    // 用户信息
    protected User user;
    // 购买商品信息
    protected List<SupplierBuyVo> supplierData;
    // 前端参数
    protected OrderBuyParam orderBuyParam;
    // 订单来源
    protected OrderSource orderSource;
    // 结算规则
    protected SettledRule settledRule;
    // 订单信息
    protected CommonOrderData commonOrderData;

    public void init(){
        // 订单来源
        this.orderSource = new OrderSource();
        // 结算规则
        this.settledRule = new SettledRule();
        // 订单信息
        this.commonOrderData = new CommonOrderData();
    }

    public Map<String, Object> settlement(User user, List<SupplierBuyVo> supplierData, OrderBuyParam orderBuyParam){
        this.user = user;
        this.supplierData = supplierData;
        this.orderBuyParam = orderBuyParam;
        // 验证商品状态, 是否允许购买
        this.validateProductList();
        // 总数据
        Integer orderTotalNum = 0;
        BigDecimal orderTotalPrice = BigDecimal.ZERO;
        BigDecimal orderPayPrice = BigDecimal.ZERO;
        // BigDecimal expressPrice = BigDecimal.ZERO;
        BigDecimal totalPointsMoney = BigDecimal.ZERO;
        Integer totalPoints = 0;
        // BigDecimal totalProductReduce = BigDecimal.ZERO;
        Map<String, Object> result = new HashMap<>();
        // 订单信息，初始化
        this.getCommonOrderData();
        // 供应商
        for(SupplierBuyVo supplier:this.supplierData){
            // 订单数据
            supplier.setOrderData(this.getOrderData(supplier));
            // 只有主订单才有下面优惠
            Integer totalNum = supplier.getProductList().stream().mapToInt(ProductBuyVo::getTotalNum).sum();
            orderTotalNum += totalNum;
            if (this.orderSource.getSource().intValue() == OrderSourceEnum.MASTER.getValue().intValue()) {
                this.setDistinctMoney(result, supplier, totalNum);
            } else {
                // 设置订单商品总金额(不含优惠折扣)
                this.setOrderTotalPrice(supplier);
            }
            // 计算订单商品的实际付款金额
            this.setOrderProductPayPrice(supplier);
            // 配送费
            if (supplier.getOrderData().getDelivery().intValue() == DeliveryTypeEnum.EXPRESS.getValue().intValue()) {
                this.setOrderExpress(supplier);
            }
            orderTotalPrice = orderTotalPrice.add(supplier.getOrderData().getOrderTotalPrice());
            // totalProductReduce = totalProductReduce.add(supplier.getOrderData().getProductReduceMoney());
            // 计算订单最终金额
            this.setOrderPayPrice(supplier);
        }
        //最终价格
        orderPayPrice = this.setOrderFinalPrice();
        // 计算完成后再计算积分可抵扣
        for(SupplierBuyVo supplier:this.supplierData){
            // 计算可用积分抵扣
            this.setOrderPoints(supplier);
            totalPointsMoney = totalPointsMoney.add(supplier.getOrderData().getPointsMoney());
            totalPoints = totalPoints + supplier.getOrderData().getPointsNum();
            // 再次计算订单最终金额
            this.setOrderPayPrice(supplier);
        }
        if(this.commonOrderData.getIsUsePoints()){
            orderPayPrice = orderPayPrice.subtract(totalPointsMoney);
        }
        // 计算订单积分赠送数量
        this.setOrderPointsBonus();
        this.commonOrderData.setOrderTotalPrice(orderTotalPrice);
        this.commonOrderData.setOrderPayPrice(orderPayPrice);
        this.commonOrderData.setPointsMoney(totalPointsMoney);
        result.put("orderData", this.commonOrderData);
        result.put("supplierList", this.supplierData);
        result.put("settledRule", this.settledRule);
        return result;
    }

    private void setDistinctMoney(Map<String, Object> result, SupplierBuyVo supplier, Integer orderTotalNum){
        // 设置订单商品会员折扣价
        this.setOrderGrade(supplier);
        // 设置订单商品总金额(不含优惠折扣)
        this.setOrderTotalPrice(supplier);
    }

    /**
     * 订单基本信息
     */
    private void getCommonOrderData(){
        // 默认地址。
        this.commonOrderData.setExistAddress(this.user.getAddressId() > 0?true:false);
        if(this.user.getAddressId() > 0){
            this.commonOrderData.setAddress(userAddressService.detail(this.user.getAddressId()));
        }
        // 是否允许使用积分抵扣
        this.commonOrderData.setIsAllowPoints(true);
        // 是否使用积分抵扣
        this.commonOrderData.setIsUsePoints(orderBuyParam.getIsUsePoints());
    }

    /**
     * 订单基本信息
     */
    private OrderData getOrderData(SupplierBuyVo supplier){
        OrderData orderData = new OrderData();
        // 配送方式
        JSONObject vo = settingUtils.getSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = JSONObject.parseObject(vo.toJSONString(), StoreVo.class);
        // 门店id
        orderData.setStoreId(0);
        // 自提门店信息
        orderData.setExtractStore(null);
        Integer delivery = 0;
        if(supplier.getProductList().get(0).getIsVirtual() == 1){
            delivery = 30;
        }else{
            if (this.orderBuyParam.getSupplier() != null) {
                delivery = this.orderBuyParam.getSupplier().get(supplier.getShopSupplierId()).getDelivery();
                orderData.setStoreId(this.orderBuyParam.getSupplier().get(supplier.getShopSupplierId()).getStoreId());
                if(orderData.getStoreId() > 0) {
                    orderData.setExtractStore(storeService.getStoreVoById(orderData.getStoreId()));
                }
            } else {
                delivery = storeVo.getDeliveryType().get(0);
            }
        }
        orderData.setDelivery(delivery);
        orderData.setDeliverySetting(storeVo.getDeliveryType());
        // 默认地址。
        orderData.setExistAddress(this.user.getAddressId() > 0?true:false);
        if(this.user.getAddressId() > 0){
            orderData.setAddress(userAddressService.detail(this.user.getAddressId()));
        }
        // 配送费用
        orderData.setExpressPrice(BigDecimal.ZERO);
        // 当前用户收货城市是否存在配送规则中
        orderData.setIntraRegion(true);
        // 是否允许使用积分抵扣
        orderData.setIsAllowPoints(true);
        // 是否使用积分抵扣
        orderData.setIsUsePoints(orderBuyParam.getIsUsePoints());
        return orderData;
    }

    /**
     * 设置订单的商品总金额(不含优惠折扣)
     */
    private void setOrderTotalPrice(SupplierBuyVo supplier)
    {
        // 订单商品的总金额(不含优惠券折扣)
        supplier.getOrderData().setOrderTotalPrice(supplier.getProductList().stream().map(ProductBuyVo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    /**
     * 计算订单商品的实际付款金额
     */
    private void setOrderProductPayPrice(SupplierBuyVo supplier)
    {
        for(ProductBuyVo product:supplier.getProductList()){
            BigDecimal value = product.getTotalPrice();
            product.setTotalPayPrice(value);
        }
    }

    /**
     * 设置最终支付金额
     */
    private void setOrderPayPrice(SupplierBuyVo supplier){
        // 订单金额(含优惠折扣)
        supplier.getOrderData().setOrderPrice(supplier.getProductList().stream().map(ProductBuyVo::getTotalPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 订单实付款金额(订单金额 + 运费)
        supplier.getOrderData().setOrderPayPrice(supplier.getOrderData().getOrderPrice().add(supplier.getOrderData().getExpressPrice()));
    }

    /**
     * 验证商品是否可以购买，子类实现
     */
    protected void validateProductList(){

    }

    /**
     * 创建新订单
     */
    public List<Integer> createOrder(User user, List<SupplierBuyVo> supplierData, OrderBuyParam orderBuyParam)
    {
        // 结算数据
        this.settlement(user, supplierData, orderBuyParam);
        // 表单验证
        this.validateOrderForm(orderBuyParam);
        List<Integer> orderIds = new ArrayList<>();
        // 创建订单
        for(SupplierBuyVo vo:supplierData){
            OrderCreateParam params = new OrderCreateParam();
            params.setOrderBuyParam(orderBuyParam);
            params.setCommonOrderData(commonOrderData);
            params.setSettledRule(settledRule);
            params.setOrderSource(orderSource);
            params.setUser(user);
            params.setSupplier(vo);
            orderIds.add(orderService.createOrder(params));
        }
        // 写入订单交易中间表
        if(orderIds.size() > 0){
            List<OrderTrade> tradeList = new ArrayList<>();
            for(Integer orderId:orderIds){
                OrderTrade trade = new OrderTrade();
                trade.setOutTradeNo(OrderUtils.geneOrderNo(user.getUserId()));
                trade.setOrderId(orderId);
                tradeList.add(trade);
            }
            orderTradeService.saveBatch(tradeList);
        }
        return orderIds;
    }

    /**
     * 验证参数
     */
    private void validateOrderForm(OrderBuyParam orderBuyParam){
        /*if(this.orderData.getDelivery().intValue() == DeliveryTypeEnum.EXPRESS.getValue().intValue()){
            // 快递配送
            if(this.orderData.getAddress() == null){
                throw new BusinessException("请先选择收货地址");
            }
        }else if(this.orderData.getDelivery().intValue() == DeliveryTypeEnum.EXTRACT.getValue().intValue()){
            // 上门自提
            if(this.orderData.getExtractStore() == null){
                throw new BusinessException("请先选择自提门店");
            }
            if(StringUtils.isEmpty(orderBuyParam.getLinkman()) || StringUtils.isEmpty(orderBuyParam.getPhone())){
                throw new BusinessException("请填写联系人和电话");
            }
        }*/
    }

    private Map<String,String> getLastExtract(Integer userId){
        Map<String,String> result = new HashMap<>();
        result.put("linkman", "");
        result.put("phone", "");
        return result;
    }

    /**
     * 赠送积分
     */
    private void setOrderPointsBonus(){
        // 初始化商品积分赠送数量
        for(SupplierBuyVo supplier:this.supplierData) {
            for (ProductBuyVo product : supplier.getProductList()) {
                product.setPointsBonus(0);
            }
            supplier.getOrderData().setPointsBonus(0);
        }
        // 积分设置
        JSONObject vo = settingUtils.getSetting(SettingEnum.POINTS.getKey(), null);
        PointsVo pointsVo = JSONObject.parseObject(vo.toJSONString(), PointsVo.class);
        // 条件：后台开启开启购物送积分
        if (!pointsVo.getIsShoppingDiscount()) {
            return;
        }
        // 设置商品积分赠送数量/先计算总赠送，然后最后一个补余
        // 订单积分赠送数量
        for(SupplierBuyVo supplier:this.supplierData) {
            Integer totalPointsBonus = supplier.getOrderData().getOrderPrice().multiply(new BigDecimal(pointsVo.getGiftRatio())).divide(new BigDecimal(100)).intValue();
            supplier.getOrderData().setPointsBonus(totalPointsBonus);
            Integer tempPointsBonus = 0;
            long productCount = supplier.getProductList().stream().filter(product -> product.getIsPointsGift() == 1).count();
            long productIndex = 0;
            for (ProductBuyVo product : supplier.getProductList()) {
                // 计算赠送积分数量
                if (product.getIsPointsGift() == 1) {
                    // 最后一个
                    if (productIndex == productCount - 1) {
                        product.setPointsBonus(supplier.getOrderData().getPointsBonus() - tempPointsBonus);
                    } else {
                        product.setPointsBonus(product.getTotalPayPrice().multiply(new BigDecimal(pointsVo.getGiftRatio())).divide(new BigDecimal(100)).intValue());
                        tempPointsBonus += product.getPointsBonus();
                    }
                    productIndex++;
                }
            }
        }
     }

    /**
     * 积分抵扣
     */
    private void setOrderPoints(SupplierBuyVo supplier){
        supplier.getOrderData().setPointsMoney(BigDecimal.ZERO);
        // 积分抵扣总数量
        supplier.getOrderData().setPointsNum(0);
        // 允许积分抵扣
        supplier.getOrderData().setIsAllowPoints(false);
        for(ProductBuyVo product:supplier.getProductList()){
            product.setPointsMoney(BigDecimal.ZERO);
        }
        // 积分设置
        if(!this.settledRule.getIsUsePoints()){
            return;
        }
        JSONObject vo = settingUtils.getSetting(SettingEnum.POINTS.getKey(), null);
        PointsVo pointsVo = JSONObject.parseObject(vo.toJSONString(), PointsVo.class);
        // 条件：后台开启下单使用积分抵扣
        if (!pointsVo.getIsShoppingDiscount()) {
            return;
        }
        // 条件：订单金额满足[?]元
        if(supplier.getOrderData().getOrderTotalPrice().compareTo(pointsVo.getDiscount().getFullOrderPrice()) < 0){
            return;
        }
        // 计算订单商品最多可抵扣的积分数量
        this.setOrderProductMaxPointsNum(supplier, pointsVo);
        // 订单最多可抵扣的积分总数量
        Integer maxPointsNumCount = new Integer(0);
        if(CollectionUtils.isNotEmpty(supplier.getProductList())){
            for(ProductBuyVo productBuyVo : supplier.getProductList()){
                if(productBuyVo.getMaxPointsNum() != null){
                    maxPointsNumCount = maxPointsNumCount + productBuyVo.getMaxPointsNum();
                }
            }
        }
        // 实际可抵扣的积分数量
        int actualPointsNum = maxPointsNumCount > this.user.getPoints()?this.user.getPoints():maxPointsNumCount;
        if (actualPointsNum < 1) {
            supplier.getOrderData().setPointsMoney(BigDecimal.ZERO);
            // 积分抵扣总数量
            supplier.getOrderData().setPointsNum(0);
            // 允许积分抵扣
            supplier.getOrderData().setIsAllowPoints(true);
            return;
        }
        // 计算订单商品实际抵扣的积分数量和金额
        PointsDeductUtils.setProductPoints(supplier.getProductList(), maxPointsNumCount, actualPointsNum, pointsVo);
        // 积分抵扣总金额
        BigDecimal orderPointsMoney = BigDecimal.ZERO;
        for (ProductBuyVo product:supplier.getProductList()) {
            orderPointsMoney = orderPointsMoney.add(product.getPointsMoney());
        }
        supplier.getOrderData().setPointsMoney(orderPointsMoney);
        // 积分抵扣总数量
        supplier.getOrderData().setPointsNum(actualPointsNum);
        // 允许积分抵扣
        supplier.getOrderData().setIsAllowPoints(true);
        // 减去积分抵扣
        for(ProductBuyVo product:supplier.getProductList()){
            BigDecimal value = product.getTotalPayPrice();
            // 减去积分抵扣金额
            if (supplier.getOrderData().getIsAllowPoints() && supplier.getOrderData().getIsUsePoints()) {
                value = value.subtract(product.getPointsMoney());
            }
            product.setTotalPayPrice(value);
        }
    }

    /**
     * 最大积分抵扣数量
     */
    private void setOrderProductMaxPointsNum(SupplierBuyVo supplier, PointsVo pointsVo){
        // 积分设置
        for (ProductBuyVo product:supplier.getProductList()) {
            // 商品不允许积分抵扣
            if (product.getIsPointsDiscount() == 0) continue;
            if( pointsVo.getDiscount().getDiscountRatio() == null || pointsVo.getDiscount().getDiscountRatio() <= 0 ) continue;
            // 最多可抵扣的金额
            BigDecimal maxPointsMoney = BigDecimal.valueOf(pointsVo.getDiscount().getMaxMoneyRatio() * product.getTotalPayPrice().doubleValue() * 0.01).setScale(2, RoundingMode.DOWN);
            // 最多可抵扣的积分数量
            BigDecimal maxPointsNum = maxPointsMoney.divide(BigDecimal.valueOf(pointsVo.getDiscount().getDiscountRatio()), RoundingMode.DOWN).setScale(0, RoundingMode.DOWN);
            product.setMaxPointsNum(maxPointsNum.intValue());
            // 如果超过商品最大抵扣数量
            if(product.getMaxPointsDiscount() != 0 && product.getMaxPointsNum() > product.getMaxPointsDiscount()){
                product.setMaxPointsNum(product.getMaxPointsDiscount());
            }
        }
    }

    /**
     * 计算运费
     */
    private void setOrderExpress(SupplierBuyVo supplier){
        // 设置默认数据：配送费用
        for (ProductBuyVo product:supplier.getProductList()) {
            product.setExpressPrice(BigDecimal.ZERO);
            // 运费模板
            product.setDelivery(deliveryService.getById(product.getDeliveryId()));
            // 运费规则
            product.setDeliveryRuleList(deliveryRuleService.list(new LambdaQueryWrapper<DeliveryRule>()
                    .eq(DeliveryRule::getDeliveryId, product.getDeliveryId())));
        }
        // 当前用户收货城市id
        Integer cityId = 0;
        if(supplier.getOrderData().getAddress() != null){
            cityId = supplier.getOrderData().getAddress().getCityId();
        }

        // 获取不支持当前城市配送的商品
        ProductBuyVo notInRuleProduct = ExpressUtils.getNotInRuleProduct(supplier.getProductList(), cityId);

        // 验证商品是否在配送范围
        supplier.getOrderData().setIntraRegion(notInRuleProduct == null);

        if (!supplier.getOrderData().getIntraRegion()) {
            throw new BusinessException(String.format("很抱歉，您的收货地址不在商品 [%s] 的配送范围内", notInRuleProduct.getProductName()));
        } else {
            // 计算配送金额
            JSONObject vo = settingUtils.getSetting(SettingEnum.FULL_FREE.getKey(), null);
            FullFreeVo fullFreeVo = JSONObject.parseObject(vo.toJSONString(), FullFreeVo.class);
            ExpressUtils.setExpressPrice(supplier.getProductList(), cityId, this.orderSource.getSource(), fullFreeVo);
        }

        // 运费设置
        JSONObject vo = settingUtils.getSetting(SettingEnum.TRADE.getKey(), null);
        TradeVo tradeVo = JSONObject.parseObject(vo.toJSONString(), TradeVo.class);
        // 订单总运费金额
        supplier.getOrderData().setExpressPrice(ExpressUtils.getTotalFreight(supplier.getProductList(), tradeVo));
    }

    /**
     * 设置订单商品会员折扣价
     */
    private void setOrderGrade(SupplierBuyVo supplier)
    {
        // 设置默认数据
        for (ProductBuyVo product:supplier.getProductList()) {
            product.setIsUserGrade(false);
            product.setGradeRatio(BigDecimal.ZERO);
            product.setGradeProductPrice(BigDecimal.ZERO);
            product.setGradeTotalMoney(BigDecimal.ZERO);
        }
        // 是否开启会员等级折扣
        if(!this.settledRule.getIsUserGrade()){
            return;
        }
        // 计算抵扣金额
        for (ProductBuyVo product:supplier.getProductList()) {
            // 判断商品是否参与会员折扣
            if(product.getIsEnableGrade() == 0){
                continue;
            }
            BigDecimal discountRatio = null;
            int aloneGradeType = 10;
            // 商品单独设置了会员折扣
            JSONObject aloneGradeEquity = JSON.parseObject(product.getAloneGradeEquity());
            if(product.getIsAloneGrade() == 1 && StringUtils.isNotEmpty(aloneGradeEquity.getString("" +user.getGradeId()))){
                if(product.getAloneGradeType() == 10){
                    // 折扣比例
                    discountRatio = BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId()) * 0.01).setScale(2, RoundingMode.DOWN);
                }else{
                    aloneGradeType = 20;
                    discountRatio = BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId())).divide(product.getTotalPrice(),2, RoundingMode.DOWN);
                }
            }else{
                UserGrade grade = userGradeService.getById(user.getGradeId());
                // 折扣比例
                discountRatio = BigDecimal.valueOf(grade.getEquity() * 0.01);
            }
            BigDecimal gradeTotalPrice = null;
            BigDecimal gradeProductPrice = null;
            if (discountRatio.compareTo(BigDecimal.ONE) < 0) {
                // 会员折扣后的商品总金额
                if (aloneGradeType == 20) {
                    gradeTotalPrice = aloneGradeEquity.getBigDecimal("" + user.getGradeId()).multiply(new BigDecimal(product.getTotalNum()));
                    gradeProductPrice = aloneGradeEquity.getBigDecimal("" + user.getGradeId());
                } else {
                    gradeTotalPrice = product.getTotalPrice().multiply(discountRatio);
                    if(gradeTotalPrice.compareTo(BigDecimal.ZERO) < 0){
                        gradeTotalPrice = BigDecimal.ZERO;
                    }
                    gradeProductPrice = product.getTotalPrice().multiply(discountRatio);
                }
                product.setIsUserGrade(true);
                product.setGradeRatio(discountRatio);
                product.setGradeProductPrice(gradeProductPrice);
                //会员折扣的总额差
                product.setGradeTotalMoney(product.getTotalPrice().subtract(gradeTotalPrice));
                //商品总价
                product.setTotalPrice(gradeTotalPrice);
            }
        }
    }

    /**
     * 获取所有支付价格
     */
    private BigDecimal setOrderFinalPrice()
    {
        // 抽点配置
        JSONObject vo = settingUtils.getSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = JSONObject.parseObject(vo.toJSONString(), StoreVo.class);
        Integer sysPercent = storeVo.getCommissionRate();
        Integer supplierPercent = 100 - sysPercent;
        for(SupplierBuyVo supplier:this.supplierData){
            // 供应商结算金额，包括运费
            BigDecimal supplierMoney = supplier.getOrderData().getOrderPrice().multiply(BigDecimal.valueOf(supplierPercent)).divide(new BigDecimal(100));
            supplier.getOrderData().setSupplierMoney(supplierMoney.add(supplier.getOrderData().getExpressPrice()));
            // 平台分佣金额
            BigDecimal sysMoney = supplier.getOrderData().getOrderPrice().multiply(BigDecimal.valueOf(sysPercent)).divide(new BigDecimal(100));
            supplier.getOrderData().setSysMoney(sysMoney);
            // 产品价格
            // 结算金额不包括运费
            for(ProductBuyVo product:supplier.getProductList()){
                product.setSupplierPrice(product.getTotalPayPrice().multiply(new BigDecimal(supplierPercent)).divide(new BigDecimal(100)));
                product.setSysMoney(product.getTotalPayPrice().multiply(new BigDecimal(sysPercent)).divide(new BigDecimal(100)));
            }
        }

        BigDecimal price = BigDecimal.ZERO;
        for(SupplierBuyVo supplier:this.supplierData){
            price = price.add(supplier.getOrderData().getOrderPayPrice());
        }
        return price;
    }
}
