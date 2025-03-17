package net.jjjshop.supplier.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderSettled;
import net.jjjshop.common.mapper.order.OrderSettledMapper;
import net.jjjshop.config.constant.CommonConstant;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.order.OrderSettledPageParam;
import net.jjjshop.supplier.service.order.OrderService;
import net.jjjshop.supplier.service.order.OrderSettledService;
import net.jjjshop.supplier.vo.order.OrderSettledVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
    private OrderService orderService;

    /**
     * 获取售后单列表
     */
    public Paging<OrderSettledVo> getList(Integer shopSupplierId, OrderSettledPageParam param) {
        Page<OrderSettled> page = new PageInfo<>(param);
        LambdaQueryWrapper<OrderSettled> wrapper = new LambdaQueryWrapper<>();
        //查询条件，订单号
        if (StringUtils.isNotEmpty(param.getOrderNo())) {
            List<Order> list = orderService.list(new LambdaQueryWrapper<Order>().like(Order::getOrderNo, param.getOrderNo()));
            List<Integer> orderIds = list.stream().map(Order::getOrderId).collect(Collectors.toList());
            wrapper.in(OrderSettled::getOrderId, orderIds);
        }
        if (StringUtils.isNotEmpty(param.getStartDay())) {
            wrapper.ge(OrderSettled::getCreateTime, param.getStartDay()+" 00:00:00");
        }
        if (StringUtils.isNotEmpty(param.getEndDay())) {
            wrapper.lt(OrderSettled::getCreateTime, param.getEndDay()+" 23:59:59");
        }
        wrapper.eq(OrderSettled::getShopSupplierId, shopSupplierId);
        wrapper.comment(CommonConstant.NOT_WITH_App_Id);
        wrapper.orderByDesc(OrderSettled::getCreateTime);
        IPage<OrderSettled> iPage = this.page(page, wrapper);
        IPage<OrderSettledVo> result = iPage.convert(item -> {
            OrderSettledVo vo = new OrderSettledVo();
            BeanUtils.copyProperties(item, vo);
            vo.setOrderMaster(orderService.detail(vo.getOrderId()));
            return vo;
        });
        return new Paging<>(result);
    }

    public OrderSettledVo detail(Integer settledId) {
        OrderSettled settled = this.getById(settledId);
        OrderSettledVo vo = new OrderSettledVo();
        BeanUtils.copyProperties(settled, vo);
        vo.setOrderMaster(orderService.detail(vo.getOrderId()));
        return vo;
    }

}
