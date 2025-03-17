package net.jjjshop.front.service.order.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderTrade;
import net.jjjshop.common.mapper.order.OrderTradeMapper;
import net.jjjshop.front.service.order.OrderTradeService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 外部交易号跟内部订单对应关系表 服务实现类
 *
 * @author jjjshop
 * @since 2022-11-03
 */
@Slf4j
@Service
public class OrderTradeServiceImpl extends BaseServiceImpl<OrderTradeMapper, OrderTrade> implements OrderTradeService {

    @Autowired
    private OrderTradeMapper orderTradeMapper;

}
