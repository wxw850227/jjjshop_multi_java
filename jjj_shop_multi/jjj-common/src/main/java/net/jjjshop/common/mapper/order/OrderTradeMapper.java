package net.jjjshop.common.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.order.OrderTrade;

import org.springframework.stereotype.Repository;


/**
 * 外部交易号跟内部订单对应关系表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-11-03
 */
@Repository
public interface OrderTradeMapper extends BaseMapper<OrderTrade> {


}
