package net.jjjshop.supplier.service.order;


import net.jjjshop.common.entity.order.OrderRefund;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.order.OrderRefundPageParam;
import net.jjjshop.supplier.param.order.OrderRefundParam;
import net.jjjshop.supplier.vo.order.OrderRefundVo;

import java.math.BigDecimal;
import java.text.ParseException;

/**
 * 售后单记录表 服务类
 * @author jjjshop
 * @since 2022-07-04
 */
public interface OrderRefundService extends BaseService<OrderRefund> {

    /**
     * 获取售后订单分页列表
     * @param orderRefundPageParam
     * @return
     */
    Paging<OrderRefundVo> getList(OrderRefundPageParam orderRefundPageParam, Integer shopSupplierId);

    /**
     * 获取售后单数据
     * @param orderRefundPageParam
     * @return
     */
    Integer getCountByStatus(OrderRefundPageParam orderRefundPageParam, Integer shopSupplierId);

    /**
     * 提交售后单
     * @param orderRefundParam
     * @return
     */
    Boolean audit(OrderRefundParam orderRefundParam);

    /**
     * 获取售后订单详情
     * @param orderRefundId
     * @return
     */
    OrderRefundVo detail(Integer orderRefundId, Integer shopSupplierId);

    /**
     * 获取退款订单总数
     * @param
     * @return
     */
    Integer getRefundTotal(Integer shopSupplierId);

    /**
     * 确认收货退款
     * @param orderRefundParam
     * @return
     */
    Boolean receipt(OrderRefundParam orderRefundParam);


    /**
     * 已同意的退款
     */
    BigDecimal getRefundMoney(Integer shopSupplierId);
}
