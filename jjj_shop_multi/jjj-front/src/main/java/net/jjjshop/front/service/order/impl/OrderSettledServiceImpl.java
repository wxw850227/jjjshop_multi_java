package net.jjjshop.front.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderSettled;
import net.jjjshop.common.mapper.order.OrderSettledMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.order.OrderSettledPageParam;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.order.OrderSettledService;
import net.jjjshop.front.vo.order.OrderSettledListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private OrderService orderService;

    /**
     * 获取售后单列表
     */
    public Paging<OrderSettledListVo> getList(Integer shopSupplierId, OrderSettledPageParam param) {
        Page<OrderSettled> page = new PageInfo<>(param);
        LambdaQueryWrapper<OrderSettled> wrapper = new LambdaQueryWrapper<>();
        //查询：订单号
        if(StringUtils.isNotBlank(param.getOrderNo())) {
            List<Order> list = orderService.list(new LambdaQueryWrapper<Order>().like(Order::getOrderNo, param.getOrderNo()));
            List<Integer> orderIds = list.stream().map(Order::getOrderId).collect(Collectors.toList());
            wrapper.in(OrderSettled::getOrderId, orderIds);
        }
        if(StringUtils.isNotBlank(param.getStartDay())) {
            wrapper.ge(OrderSettled::getCreateTime, param.getStartDay());
        }
        if(StringUtils.isNotBlank(param.getEndDay())) {
            wrapper.lt(OrderSettled::getCreateTime, param.getEndDay());
        }
        wrapper.eq(OrderSettled::getShopSupplierId, shopSupplierId);
        wrapper.orderByDesc(OrderSettled::getCreateTime);
        IPage<OrderSettled> iPage = this.page(page, wrapper);
        IPage<OrderSettledListVo> result = iPage.convert(item -> {
            OrderSettledListVo vo = new OrderSettledListVo();
            BeanUtils.copyProperties(item, vo);
            vo.setOrderNo(orderService.getById(vo.getOrderId()).getOrderNo());
            return vo;
        });
        return new Paging<>(result);
    }
}
