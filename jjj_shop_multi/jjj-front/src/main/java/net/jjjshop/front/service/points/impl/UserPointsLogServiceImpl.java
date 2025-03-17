package net.jjjshop.front.service.points.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserPointsLog;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.mapper.user.UserPointsLogMapper;
import net.jjjshop.common.settings.vo.PointsVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.points.UserPointsLogPageParam;
import net.jjjshop.front.service.points.UserPointsLogService;
import net.jjjshop.front.vo.points.UserPointsLogVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户积分变动明细表 服务实现类
 * @author jjjshop
 * @since 2022-07-21
 */
@Slf4j
@Service
public class UserPointsLogServiceImpl extends BaseServiceImpl<UserPointsLogMapper, UserPointsLog> implements UserPointsLogService {

    @Autowired
    private SettingUtils settingUtils;

    /**
     * 分页查询用户积分明细
     * @param userPointsLogPageParam
     * @param user
     * @return
     */
    public Map<String, Object> getList(UserPointsLogPageParam userPointsLogPageParam, User user) {
        Map<String, Object> map = new HashMap<>();
        // 开始页码
        Page<UserPointsLog> page = new PageInfo(userPointsLogPageParam);
        LambdaQueryWrapper<UserPointsLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPointsLog::getUserId, user.getUserId());
        wrapper.orderByDesc(UserPointsLog::getCreateTime);
        IPage<UserPointsLog> iPage = this.page(page, wrapper);
        IPage<UserPointsLogVo> voList = iPage.convert(item -> {
            UserPointsLogVo vo = new UserPointsLogVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });
        JSONObject json = settingUtils.getSetting(SettingEnum.POINTS.getKey(), null);
        PointsVo pointsVo = json.toJavaObject(PointsVo.class);
        JSONObject setting = settingUtils.getSetting(SettingEnum.POINTSMALL.getKey(), null);
        map.put("list", new Paging(voList));
        map.put("discountRatio", pointsVo.getDiscount().getDiscountRatio());
        map.put("points", user.getPoints());
        return map;
    }
}
