package net.jjjshop.common.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.user.UserPointsLog;
import net.jjjshop.common.vo.user.UserPointsLogVo;
import org.apache.ibatis.annotations.Param;
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
     * @param nickName,startDate,endDate
     * @return
     */
    List<UserPointsLogVo> getList(@Param("nickName") String nickName,
                                  @Param("pageIndex") Long pageIndex,
                                  @Param("pageSize") Long pageSize,
                                  @Param("startDate") String startDate,
                                  @Param("endDate") String endDate);

}
