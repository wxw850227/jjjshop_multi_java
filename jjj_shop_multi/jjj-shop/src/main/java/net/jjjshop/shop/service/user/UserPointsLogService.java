package net.jjjshop.shop.service.user;

import net.jjjshop.common.entity.user.UserPointsLog;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.user.UserPointsLogPageParam;
import net.jjjshop.shop.vo.user.UserPointsLogVo;

/**
 * 用户积分变动明细表 服务类
 * @author jjjshop
 * @since 2022-07-21
 */
public interface UserPointsLogService extends BaseService<UserPointsLog> {

    /**
     * 分页查询用户积分明细
     * @param userPointsLogPageParam
     * @return
     */
    Paging<UserPointsLogVo> getList(UserPointsLogPageParam userPointsLogPageParam);
}
