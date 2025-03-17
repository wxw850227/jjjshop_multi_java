package net.jjjshop.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.OrderTypeEnum;
import net.jjjshop.common.service.app.AppService;
import net.jjjshop.common.util.wx.WxPayUtils;
import net.jjjshop.common.vo.WxPayResult;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.core.bean.RequestDetail;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

@Slf4j
@Component
public class PayUtils {

    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private WxPayUtils wxPayUtils;
    @Autowired
    private AppService appService;

    /**
     * 微信支付
     * @return
     */
    public Object onPaymentByWechat(User user, String tradeNo, BigDecimal onlineMoney,
                                     String paySource, Map<String, Object> result, Integer orderType,
                                        Integer multiple) throws Exception{
        App app = appService.getById(user.getAppId());
        if(app.getWxPayKind() == 2){
            return this.onPaymentByWechatV2(user,tradeNo, onlineMoney,
                    paySource,result,orderType,multiple);
        }else {
            return this.onPaymentByWechatV3(user, tradeNo, onlineMoney,
                    paySource,result,orderType,multiple);
        }

    }

    public Object onPaymentByWechatV2(User user, String tradeNo, BigDecimal onlineMoney,
                                      String paySource, Map<String, Object> result, Integer orderType,
                                      Integer multiple) throws Exception{
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setBody(tradeNo);
        request.setOutTradeNo(tradeNo);
        request.setTotalFee(onlineMoney.multiply(new BigDecimal(100)).intValue());
        RequestDetail requestDetail = RequestDetailThreadLocal.getRequestDetail();
        request.setSpbillCreateIp(requestDetail.getIp());
        request.setNotifyUrl(springBootJjjProperties.getServerIp() + "/api/job/notify/wxPay");
        request.setTradeType("JSAPI");
        if("mp".equals(user.getRegSource())){
            request.setOpenid(user.getMpopenId());
        }else if("wx".equals(user.getRegSource())){
            request.setOpenid(user.getOpenId());
        }
        if("h5".equals(paySource)){
            request.setTradeType("MWEB");
            //场景信息 必要参数
            JSONObject sceneInfo = new JSONObject();
            sceneInfo.put("type", "Wap");
            sceneInfo.put("wap_url", springBootJjjProperties.getServerIp());
            sceneInfo.put("wap_name", "支付");
            request.setSceneInfo(sceneInfo.toJSONString());
            request.setOpenid("");
            result.put("returnUrl", URLEncoder.encode(springBootJjjProperties.getServerIp() + "/h5/pages/order/myorder", "UTF-8"));
        }
        JSONObject attach = new JSONObject();
        attach.put("orderType", orderType);
        attach.put("paySource", paySource);
        attach.put("multiple", multiple);
        request.setAttach(attach.toJSONString());
        // 先设置，再调用
        WxPayResult wxPayResult = wxPayUtils.getConfig(wxPayService, paySource, null);
        this.wxPayService.switchover(wxPayResult.getMchId(),wxPayResult.getAppId());
        return this.wxPayService.createOrder(request);
    }


    public Object onPaymentByWechatV3(User user, String tradeNo, BigDecimal onlineMoney,
                                      String paySource, Map<String, Object> result, Integer orderType,
                                      Integer multiple) throws Exception{
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        request.setOutTradeNo(tradeNo);
        WxPayUnifiedOrderV3Request.Amount am = new WxPayUnifiedOrderV3Request.Amount();
        am.setTotal(onlineMoney.multiply(new BigDecimal(100)).intValue());
        am.setCurrency("CNY");
        request.setAmount(am);
        RequestDetail requestDetail = RequestDetailThreadLocal.getRequestDetail();
        request.setNotifyUrl(springBootJjjProperties.getServerIp() + "/api/job/notify/wxPay");
        request.setDescription(tradeNo);

        WxPayUnifiedOrderV3Request.SceneInfo scen = new WxPayUnifiedOrderV3Request.SceneInfo();
        scen.setPayerClientIp(requestDetail.getIp());

        TradeTypeEnum tradeTypeEnum = null;
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(user.getOpenId());
        tradeTypeEnum = TradeTypeEnum.JSAPI;
        request.setSceneInfo(scen);
        request.setPayer(payer);
        JSONObject attach = new JSONObject();
        attach.put("orderType", orderType);
        attach.put("paySource", paySource);
        attach.put("multiple", multiple);
        request.setAttach(attach.toJSONString());
        // 先设置，再调用
        WxPayResult wxPayResult =  wxPayUtils.getConfig(wxPayService, paySource, null);
        this.wxPayService.switchover(wxPayResult.getMchId(),wxPayResult.getAppId());
        return this.wxPayService.createOrderV3(tradeTypeEnum, request);
    }

    /**
     * 支付宝支付
     * @return
     */
    public Object onPaymentByAlipay(User user, String tradeNo, BigDecimal onlineMoney,
                                     String paySource, Integer orderType, int multiple) throws Exception{
        App app = appService.getById(user.getAppId());
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipay.com/gateway.do",
                app.getAlipayAppid(), app.getAlipayPrivatekey(),"json","UTF-8",
                app.getAlipayPublickey(),"RSA2");
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setNotifyUrl(springBootJjjProperties.getServerIp() + "/api/job/notify/alipayNotify?orderType="+orderType
                +"&paySource="+paySource+"&ple="+multiple);
        request.setReturnUrl(springBootJjjProperties.getServerIp() + "/api/job/notify/alipayReturn?orderType="+orderType
                +"&paySource="+paySource+"&ple="+multiple);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", tradeNo);
        bizContent.put("total_amount", onlineMoney);
        bizContent.put("subject", "订单支付");
        bizContent.put("product_code", "QUICK_WAP_WAY");
        request.setBizContent(bizContent.toString());
        AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return response.getBody();
    }
}
