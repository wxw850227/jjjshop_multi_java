package net.jjjshop.front.service.order;

import net.jjjshop.common.entity.order.OrderSettled;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.order.OrderSettledPageParam;
import net.jjjshop.front.vo.order.OrderSettledListVo;

/**
 * 订单结算表 服务类
 *
 * @author jjjshop
 * @since 2022-10-31
 */
public interface OrderSettledService extends BaseService<OrderSettled> {

    /**
     * 获取售后单列表
     */
    Paging<OrderSettledListVo> getList(Integer shopSupplierId, OrderSettledPageParam param);
}
