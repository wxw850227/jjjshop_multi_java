package net.jjjshop.shop.service.order;

import com.alibaba.fastjson.JSONObject;
import net.jjjshop.common.entity.order.OrderSettled;
import net.jjjshop.framework.common.service.BaseService;

import java.text.ParseException;
import java.util.Map;

/**
 * 订单结算表 服务类
 *
 * @author jjjshop
 * @since 2022-10-31
 */
public interface OrderSettledService extends BaseService<OrderSettled> {

    /**
     * 获取数据概况
     */
    JSONObject getData() throws ParseException;

    /**
     * 按日期获取结算数据
     */
    Map<String, Object> getSettledDataByDate(String startDate, String endDate);
}
