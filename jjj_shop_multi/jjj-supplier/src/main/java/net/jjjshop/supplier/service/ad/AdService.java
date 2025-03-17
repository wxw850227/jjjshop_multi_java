package net.jjjshop.supplier.service.ad;

import net.jjjshop.common.entity.page.Ad;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.ad.AdPageParam;
import net.jjjshop.supplier.param.ad.AdParam;
import net.jjjshop.supplier.vo.ad.AdVo;

/**
 * banner图 服务类
 *
 * @author jjjshop
 * @since 2022-10-25
 */
public interface AdService extends BaseService<Ad> {

    /**
     * 获取广告分页列表
     * @param adPageParam
     * @return
     */
    Paging<AdVo> getList(AdPageParam adPageParam);

    /**
     * 获取广告详情
     * @param adId
     * @return
     */
    AdVo detail(Integer adId);

    /**
     * 添加新记录
     * @param adParam
     * @return
     */
    Boolean add(AdParam adParam);

    /**
     * 编辑
     * @param adParam
     * @return
     */
    Boolean edit(AdParam adParam);

    /**
     * 删除记录
     * @param adId
     * @return
     */
    Boolean delById(Integer adId);
}
