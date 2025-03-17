package net.jjjshop.common.service.user.impl;

import net.jjjshop.common.entity.user.UserVisit;
import net.jjjshop.common.mapper.user.UserVisitMapper;
import net.jjjshop.common.service.user.UserVisitService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户访问记录 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-25
 */
@Slf4j
@Service
public class UserVisitServiceImpl extends BaseServiceImpl<UserVisitMapper, UserVisit> implements UserVisitService {

    @Autowired
    private UserVisitMapper userVisitMapper;

}
