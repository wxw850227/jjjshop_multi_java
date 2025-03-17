package net.jjjshop.common.service.order.impl;

import net.jjjshop.common.entity.order.OrderSettled;
import net.jjjshop.common.mapper.order.OrderSettledMapper;
import net.jjjshop.common.service.order.OrderSettledService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
    private OrderSettledMapper orderSettledMapper;


}
