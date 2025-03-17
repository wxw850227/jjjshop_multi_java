package net.jjjshop.shop.service.order.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.mapper.order.OrderProductMapper;
import net.jjjshop.common.vo.order.OrderProductVo;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.order.OrderProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 订单商品记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderProductServiceImpl extends BaseServiceImpl<OrderProductMapper, OrderProduct> implements OrderProductService {
}
