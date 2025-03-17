package net.jjjshop.shop.service.order.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderSettled;
import net.jjjshop.common.mapper.order.OrderSettledMapper;
import net.jjjshop.common.util.OrderDataUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.order.OrderSettledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 订单结算表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-31
 */
@Slf4j
@Service
public class OrderSettledServiceImpl extends BaseServiceImpl<OrderSettledMapper, OrderSettled> implements OrderSettledService {

    @Autowired
    private OrderDataUtils orderDataUtils;

    /**
     * 获取数据概况
     */
    public JSONObject getData() throws ParseException {
        //获取今天时间
        String today = DateUtil.format(DateUtil.offsetDay(new Date(), 0), "yyyy-MM-dd");
        //获取昨天时间
        String yesterday = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");

        // 供应商结算
        BigDecimal realSupplierMoneyT = orderDataUtils.getSettledData(null, today, "realSupplierMoney", null);
        BigDecimal realSupplierMoneyY = orderDataUtils.getSettledData(null, yesterday, "realSupplierMoney", null);
        // 平台提成
        BigDecimal realSysMoneyT = orderDataUtils.getSettledData(today, null, "realSysMoney", null);
        BigDecimal realSysMoneyY = orderDataUtils.getSettledData(yesterday, null, "realSysMoney", null);
        // 退款金额
        BigDecimal refundMoneyT = orderDataUtils.getSettledData(today, null, "refundMoney", null);
        BigDecimal refundMoneyY = orderDataUtils.getSettledData(yesterday, null, "refundMoney", null);

        JSONObject result = new JSONObject();
        result.put("realSupplierMoneyT", realSupplierMoneyT);
        result.put("realSupplierMoneyY", realSupplierMoneyY);

        result.put("realSysMoneyT", realSysMoneyT);
        result.put("realSysMoneyY", realSysMoneyY);

        result.put("refundMoneyT", refundMoneyT);
        result.put("refundMoneyY", refundMoneyY);

        return result;
    }

    /**
     * 按日期获取结算数据
     */
    public Map<String, Object> getSettledDataByDate(String startDate, String endDate) {
        Map<String, Object> map = new HashMap<>();
        Date startTime = DateUtil.parse(startDate);
        Date endTime = DateUtil.parse(endDate);
        List<JSONObject> data = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (Date t = startTime; t.before(endTime); t = DateUtil.offsetDay(t, 1)) {
            String day = DateUtil.format(t, "yyyy-MM-dd");
            BigDecimal realSupplierMoney = orderDataUtils.getSettledData(day, null, "realSupplierMoney", null);
            BigDecimal realSysMoney = orderDataUtils.getSettledData(day, null, "realSysMoney", null);
            BigDecimal refundMoney = orderDataUtils.getSettledData(day, null, "refundMoney", null);
            JSONObject json = new JSONObject();
            json.put("day", day);
            json.put("realSupplierMoney", realSupplierMoney);
            json.put("realSysMoney", realSysMoney);
            json.put("refundMoney", refundMoney);
            days.add(day);
            data.add(json);
        }
        map.put("data", data);
        map.put("days", days);
        return map;
    }

}
