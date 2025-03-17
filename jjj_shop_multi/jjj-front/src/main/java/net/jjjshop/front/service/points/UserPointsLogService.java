package net.jjjshop.front.service.points;

import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserPointsLog;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.param.points.UserPointsLogPageParam;

import java.util.Map;

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
    Map<String, Object> getList(UserPointsLogPageParam userPointsLogPageParam, User user);
}
