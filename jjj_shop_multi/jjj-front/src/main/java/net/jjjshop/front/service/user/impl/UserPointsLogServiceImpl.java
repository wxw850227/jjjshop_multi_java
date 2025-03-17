package net.jjjshop.front.service.user.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.UserPointsLog;
import net.jjjshop.common.mapper.user.UserPointsLogMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.user.UserPointsLogService;
import org.springframework.stereotype.Service;

/**
 * 用户积分变动明细表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-21
 */
@Slf4j
@Service
public class UserPointsLogServiceImpl extends BaseServiceImpl<UserPointsLogMapper, UserPointsLog> implements UserPointsLogService {
}
