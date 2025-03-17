package net.jjjshop.common.factory.paysuccess.source.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.factory.paysuccess.source.PaySuccessSourceFactoryService;
import net.jjjshop.common.factory.printer.PrinterFactory;
import net.jjjshop.common.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 支付成功
 */
@Slf4j
@Service
public class SeckillPaySuccessSourceFactoryService extends PaySuccessSourceFactoryService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private PrinterFactory printerFactory;
    /**
     * 支付成功处理
     */
    @Async
    public void onPaySuccess(Integer orderId){
        Order order = orderService.getById(orderId);
        // 公共事件
        this.onCommonEvent(order);
        // 小票打印
        printerFactory.print(order);
    }
}
