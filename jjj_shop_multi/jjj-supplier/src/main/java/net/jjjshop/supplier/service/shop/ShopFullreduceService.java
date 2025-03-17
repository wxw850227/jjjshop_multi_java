package net.jjjshop.supplier.service.shop;

import net.jjjshop.common.entity.shop.ShopFullreduce;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.shop.FullReducePageParam;
import net.jjjshop.supplier.param.shop.FullReduceParam;
import net.jjjshop.supplier.vo.shop.FullReduceVo;

import java.util.List;

/**
 * 满减设置表 服务类
 *
 * @author jjjshop
 * @since 2022-08-22
 */
public interface ShopFullreduceService extends BaseService<ShopFullreduce> {

    /**
     * 获取所有满减活动
     * @param fullReducePageParam
     * @return
     */
    Paging<FullReduceVo> getList(FullReducePageParam fullReducePageParam);

    /**
     * 获取所有满减活动
     * @param
     * @return
     */
    List<FullReduceVo> getAll();

    /**
     * 添加满减活动
     * @param fullReduceParam
     * @return
     */
    Boolean add(FullReduceParam fullReduceParam);

    /**
     * 编辑满减活动
     * @param fullReduceParam
     * @return
     */
    Boolean edit(FullReduceParam fullReduceParam);

    /**
     * 软删除满减活动
     * @param fullReduceId
     * @return
     */
    Boolean setDelete(Integer fullReduceId);

    /**
     * 满减活动详情
     * @param fullReduceId
     * @return
     */
    FullReduceVo detail(Integer fullReduceId);

    /**
     * 获取商品参加的满减活动
     * @param productId
     * @return
     */
    List<FullReduceVo> getProductAll(Integer productId);

}
