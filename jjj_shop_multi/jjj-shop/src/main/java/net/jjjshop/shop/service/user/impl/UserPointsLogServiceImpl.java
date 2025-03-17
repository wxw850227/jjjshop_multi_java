package net.jjjshop.shop.service.user.impl;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.UserPointsLog;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.mapper.user.UserPointsLogMapper;
import net.jjjshop.shop.param.user.UserPointsLogPageParam;
import net.jjjshop.shop.service.user.UserPointsLogService;
import net.jjjshop.shop.vo.user.UserPointsLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户积分变动明细表 服务实现类
 * @author jjjshop
 * @since 2022-07-21
 */
@Slf4j
@Service
public class UserPointsLogServiceImpl extends BaseServiceImpl<UserPointsLogMapper, UserPointsLog> implements UserPointsLogService {

    @Autowired
    private UserPointsLogMapper userPointsLogMapper;

    /**
     * 分页查询用户积分明细
     * @param userPointsLogPageParam
     * @return
     */
    public Paging<UserPointsLogVo> getList(UserPointsLogPageParam userPointsLogPageParam) {
        // 开始页码
        userPointsLogPageParam.setStartIndex((userPointsLogPageParam.getPageIndex() - 1) * userPointsLogPageParam.getPageSize());
        if(userPointsLogPageParam.getStartDate() != null){
            userPointsLogPageParam.setStartDate(DateUtil.parse(DateUtil.format(userPointsLogPageParam.getStartDate(), "yyyy-MM-dd 00:00:00")));
        }
        if(userPointsLogPageParam.getEndDate() != null){
            userPointsLogPageParam.setEndDate(DateUtil.parse(DateUtil.format(userPointsLogPageParam.getEndDate(), "yyyy-MM-dd 23:59:59")));
        }
        // 查询当前页列表
        List<UserPointsLogVo> list = userPointsLogMapper.getList(userPointsLogPageParam);
        // 将结果封装Paging对象输出
        PageInfo<UserPointsLogVo> pageInfo = new PageInfo<>();
        pageInfo.setRecords(list);
        pageInfo.setTotal(userPointsLogMapper.getTotalCount(userPointsLogPageParam));
        return new Paging<>(pageInfo);
    }
}
