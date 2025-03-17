package net.jjjshop.shop.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.user.UserPointsLog;
import net.jjjshop.shop.param.user.UserPointsLogPageParam;
import net.jjjshop.shop.vo.user.UserPointsLogVo;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用户积分变动明细表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-21
 */
@Repository
public interface UserPointsLogMapper extends BaseMapper<UserPointsLog> {
    /**
     * 通过昵称,日期区间查询用户积分明细
     * @param userPointsLogPageParam
     * @return
     */
    List<UserPointsLogVo> getList(UserPointsLogPageParam userPointsLogPageParam);

    /**
     * 通过昵称查询用户等级变动明细数量
     * @param userPointsLogPageParam
     * @return
     */
    Long getTotalCount(UserPointsLogPageParam userPointsLogPageParam);

}
