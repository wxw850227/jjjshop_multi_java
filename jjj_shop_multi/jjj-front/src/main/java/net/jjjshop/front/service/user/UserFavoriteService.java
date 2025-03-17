package net.jjjshop.front.service.user;

import net.jjjshop.common.entity.user.UserFavorite;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.user.UserFavoritePageParam;
import net.jjjshop.front.param.user.UserFavoriteParam;
import net.jjjshop.front.vo.supplier.SupplierListVo;
import net.jjjshop.front.vo.user.UserFavoriteVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 我的收藏 服务类
 * @author jjjshop
 * @since 2022-08-02
 */

public interface UserFavoriteService extends BaseService<UserFavorite> {

    /**
     * 收藏列表
     * @param userFavoritePageParam
     * @return
     */
    Paging<UserFavoriteVo> getProductList(UserFavoritePageParam userFavoritePageParam);

    /**
     * 添加收藏
     * @param productId
     * @param userId
     * @return
     */
    Boolean add(Integer productId, Integer userId);

    /**
     * 取消收藏
     * @param productId
     * @param userId
     * @return
     */
    Boolean delById(Integer productId, Integer userId);

    /**
     * 用户是否收藏
     * @param productId
     * @param userId
     * @return
     */
    Boolean isFav(Integer productId, Integer userId);

    /**
     * 取消收藏
     */
    Boolean cancelFav(Integer id, Integer pid, Integer type);

    /**
     * 收藏
     */
    Boolean fav(UserFavoriteParam userFavoriteParam);

    /**
     * 判断是否已收藏
     */
    Integer isFollow(Integer pid, Integer userId, Integer type);

    /**
     * 获取关注店铺列表
     */
    Paging<SupplierListVo> getMySupplier (UserFavoritePageParam param);

}
