package net.jjjshop.common.service.order.impl;

import net.jjjshop.common.entity.order.OrderTrade;
import net.jjjshop.common.mapper.order.OrderTradeMapper;
import net.jjjshop.common.service.order.OrderTradeService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
