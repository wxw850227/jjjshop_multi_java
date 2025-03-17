package net.jjjshop.supplier.service.user;

import net.jjjshop.common.entity.user.UserFavorite;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.user.UserFavoritePageParam;
import net.jjjshop.supplier.vo.user.UserVo;

/**
 * 我的收藏 服务类
 *
 * @author jjjshop
 * @since 2022-08-02
 */
public interface UserFavoriteService extends BaseService<UserFavorite> {

    /**
     * 获取关注店铺的用户
     * @param shopSupplierId
     * @param userFavoritePageParam
     * @return
     */
    Paging<UserVo> getUserList(Integer shopSupplierId, UserFavoritePageParam userFavoritePageParam);

    /**
     * 获取某天的关注用户数
     * @param day
     * @param shopSupplierId
     * @return
     */
    Integer getUserTotal(String day, Integer shopSupplierId);
}
