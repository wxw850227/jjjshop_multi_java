

package net.jjjshop.job.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beust.jcommander.internal.Maps;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.v3.util.AesUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.enums.OrderPayStatusEnum;
import net.jjjshop.common.enums.OrderPayTypeEnum;
import net.jjjshop.common.enums.OrderTypeEnum;
import net.jjjshop.common.factory.paysuccess.type.PaySuccessOrder;
import net.jjjshop.common.factory.paysuccess.type.PaySuccessTypeFactory;
import net.jjjshop.common.service.app.AppService;
import net.jjjshop.common.util.wx.WxPayUtils;
import net.jjjshop.common.vo.WxPayResult;
import net.jjjshop.common.vo.order.PayDataVo;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.common.controller.BaseController;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.job.service.order.OrderService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/job/notify")
public class NotifyController extends BaseController {

    @Autowired
    private PaySuccessTypeFactory paySuccessTypeFactory;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private WxPayUtils wxPayUtils;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private AppService appService;

    @RequestMapping(value = "/wxPay", method = RequestMethod.POST)
    @OperationLog(name = "wxPay")
    @ApiOperation(value = "微信支付回调", response = String.class)
    public String wxPay(HttpServletRequest request) {
        String wechatpaySerial = request.getHeader("wechatpay-serial");
        log.info("wechatpaySerial="+wechatpaySerial);
        if(StringUtils.hasText(wechatpaySerial)){
            App app = appService.list(new LambdaQueryWrapper<App>()
                    .eq(App::getWechatpaySerial, wechatpaySerial)).get(0);
            return this.wxPayV3(request, app);
        }else{
            return this.wxPayV2(request);
        }
    }

    public String wxPayV2(HttpServletRequest request) {
        try {
            log.info("微信支付回调");
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            log.info("xmlResult"+ xmlResult);
            // 未验签，仅取值
            WxPayOrderNotifyResult preResult = WxPayOrderNotifyResult.fromXML(xmlResult);
            // 附加字段
            JSONObject attach = JSON.parseObject(preResult.getAttach());
            PaySuccessOrder order = paySuccessTypeFactory.getFactory(attach.getInteger("orderType")).getOrder(preResult.getOutTradeNo());
            if(order.getPayStatus().intValue() == OrderPayStatusEnum.SUCCESS.getValue().intValue()){
                return WxPayNotifyResponse.success("处理成功!");
            }
            // 验签结果
            WxPayResult wxPayResult =  wxPayUtils.getConfig(wxPayService, attach.getString("paySource"), Long.valueOf(order.getAppId()));
            WxPayOrderNotifyResult result = wxPayService.switchoverTo(wxPayResult.getMchId(),wxPayResult.getAppId())
                    .parseOrderNotifyResult(xmlResult, "MD5");
            // 处理支付回调
            PayDataVo payDataVo = new PayDataVo();
            payDataVo.setTransaction_id(result.getTransactionId());
            payDataVo.setAttach(attach);
            paySuccessTypeFactory.getFactory(attach.getInteger("orderType"))
                    .onPaySuccess(result.getOutTradeNo(), 10, OrderPayTypeEnum.WECHAT.getValue(), payDataVo);
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因:", e);
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

    public String wxPayV3(HttpServletRequest request, App app) {
        try {
            log.info("微信支付回调");
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                // 打印Header参数的名称和值
                log.info("Header Name: " + headerName + ", Value: " + headerValue);
            }
            // 先解密
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            JSONObject jsonData = JSONObject.parseObject(xmlResult);
            String decryptData = AesUtils.decryptToString(jsonData.getJSONObject("resource").getString("associated_data"),
                    jsonData.getJSONObject("resource").getString("nonce"),
                    jsonData.getJSONObject("resource").getString("ciphertext"),
                    app.getApikey());
            log.info("decryptData"+ decryptData);
            JSONObject decryptJson = JSONObject.parseObject(decryptData);

            // 附加字段
            JSONObject attach = decryptJson.getJSONObject("attach");
            PaySuccessOrder order = paySuccessTypeFactory.getFactory(attach.getInteger("orderType")).getOrder(decryptJson.getString("out_trade_no"));
            if(order.getPayStatus().intValue() == OrderPayStatusEnum.SUCCESS.getValue().intValue()){
                return WxPayNotifyResponse.success("处理成功!");
            }
            // 处理支付回调
            PayDataVo payDataVo = new PayDataVo();
            payDataVo.setTransaction_id(decryptJson.getString("transaction_id"));
            payDataVo.setAttach(attach);
            paySuccessTypeFactory.getFactory(attach.getInteger("orderType"))
                    .onPaySuccess(decryptJson.getString("out_trade_no"), 10, OrderPayTypeEnum.WECHAT.getValue(), payDataVo);
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因:", e);
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

    @RequestMapping(value = "/alipayNotify", method = RequestMethod.POST)
    @OperationLog(name = "alipayNotify")
    @ApiOperation(value = "支付宝支付回调", response = String.class)
    public String alipayNotify(HttpServletRequest request) {
        try {
            log.info("支付宝支付回调");
            // 回调函数，也就是在支付成功之后，可以调用我们支付之后需要执行的相关方法，从而达到数据库的数据和我们的操作相统一。
            Map<String,String> params = Maps.newHashMap();
            Map requestParams = request.getParameterMap();
            log.info("支付宝回调,requestParams参数:{}", requestParams.toString());
            for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
                String name = (String)iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for(int i = 0 ; i <values.length;i++){
                    valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
                }
                params.put(name,valueStr);
            }
            log.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());
            PaySuccessOrder order = paySuccessTypeFactory.getFactory(Integer.valueOf(params.get("orderType"))).getOrder(params.get("out_trade_no"));
            if(order.getPayStatus().intValue() == OrderPayStatusEnum.SUCCESS.getValue().intValue()){
                return "success";
            }
            // 非常重要，验证回调的正确性，是不是支付宝发的。并且呢还要避免重复通知。
            // 因为支付宝要求我们除去两个参数 sign（rsaCheckV2函数已经帮我们除去）、sign_type（自己手动除去）
            params.remove("sign_type");
            String orderType = params.get("orderType");
            String paySource = params.get("paySource");
            // 附加的参数
            params.remove("orderType");
            params.remove("paySource");
            App app = appService.getById(order.getAppId());
            // RSA2
            // 第二参数注意是支付宝公共密钥，而不是应用密钥
            // 这里编码是与 new AlipayTradeServiceImpl.ClientBuilder().build(); 里的编码一样，build() 默认为UTF-8，当然也可以自定义，但是只要保证两个一样即可，我们统一保持UTF-8
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, app.getAlipayPublickey(),"utf-8", "RSA2");
            if(!alipayRSACheckedV2){
                log.info("支付宝回调验签失败");
                return "error";
            }
            log.info("支付宝回调验签成功!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            // 加入自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
            JSONObject attach = new JSONObject();
            attach.put("orderType", orderType);
            attach.put("paySource", paySource);
            PayDataVo payDataVo = new PayDataVo();
            payDataVo.setTransaction_id(params.get("trade_no"));
            payDataVo.setAttach(attach);
            log.info("支付宝支付成功处理");
            paySuccessTypeFactory.getFactory(attach.getInteger("orderType"))
                    .onPaySuccess(params.get("out_trade_no"), 10, OrderPayTypeEnum.ALIPAY.getValue(), payDataVo);
            return "success";
        } catch (Exception e) {
            log.error("支付宝支付回调异常,异常原因:", e);
            return "error";
        }
    }


    @RequestMapping(value = "/alipayReturn", method = RequestMethod.GET)
    @OperationLog(name = "alipayReturn")
    @ApiOperation(value = "支付宝支付回调", response = String.class)
    public void alipayReturn(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("支付宝支付回调return");
            // 回调函数，也就是在支付成功之后，可以调用我们支付之后需要执行的相关方法，从而达到数据库的数据和我们的操作相统一。
            Map<String,String> params = Maps.newHashMap();
            Map requestParams = request.getParameterMap();
            log.info("支付宝支付回调return,requestParams参数:{}", requestParams.toString());
            for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
                String name = (String)iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for(int i = 0 ; i <values.length;i++){
                    valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
                }
                params.put(name,valueStr);
            }
            log.info("支付宝支付回调return,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());
            // 非常重要，验证回调的正确性，是不是支付宝发的。并且呢还要避免重复通知。
            // 因为支付宝要求我们除去两个参数 sign（rsaCheckV2函数已经帮我们除去）、sign_type（自己手动除去）
            params.remove("sign_type");
            String orderType = params.get("orderType");
            String paySource = params.get("paySource");
            // 附加的参数
            params.remove("orderType");
            params.remove("paySource");
            // 订单
            Order order = orderService.detailByTradeNo(params.get("out_trade_no"));
            // 获取app配置
            App app = appService.getById(order.getAppId());
            // RSA2
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, app.getAlipayPublickey(),"utf-8", "RSA2");
            if(!alipayRSACheckedV2){
                log.info("支付宝回调验签失败");
                return;
            }
            log.info("支付宝回调return验签成功!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            String url = springBootJjjProperties.getServerIp() + "/h5";
            if(Integer.valueOf(orderType).intValue() == OrderTypeEnum.MASTER.getValue().intValue()){
                url = springBootJjjProperties.getServerIp() + "/h5/pages/order/pay-success/pay-success?orderId=" + order.getOrderId();
            }
            response.sendRedirect(url);
        } catch (Exception e) {
            log.error("支付宝支付回调异常,异常原因:", e);
        }
    }
}
