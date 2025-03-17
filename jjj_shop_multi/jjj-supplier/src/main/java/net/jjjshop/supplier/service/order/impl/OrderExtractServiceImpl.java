package net.jjjshop.supplier.service.order.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderExtract;
import net.jjjshop.common.mapper.order.OrderExtractMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.supplier.service.order.OrderExtractService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderExtractServiceImpl extends BaseServiceImpl<OrderExtractMapper, OrderExtract> implements OrderExtractService {
}
