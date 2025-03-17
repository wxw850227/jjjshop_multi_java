package net.jjjshop.front.service.shop;

import net.jjjshop.common.entity.shop.ShopFullreduce;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.vo.shop.ShopFullreduceVo;

import java.math.BigDecimal;

/**
 * 满减设置表 服务类
 *
 * @author jjjshop
 * @since 2022-08-22
 */
public interface ShopFullreduceService extends BaseService<ShopFullreduce> {
    /**
     * 获取满减
     * @param totalPrice
     * @param totalNum
     * @return
     */
    ShopFullreduceVo getReductList(BigDecimal totalPrice, Integer totalNum, Integer productId, Integer shopSupplierId);
}
