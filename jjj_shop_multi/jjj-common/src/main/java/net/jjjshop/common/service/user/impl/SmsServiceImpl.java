package net.jjjshop.common.service.user.impl;

import net.jjjshop.common.entity.Sms;
import net.jjjshop.common.mapper.user.SmsMapper;
import net.jjjshop.common.service.user.SmsService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户注册短信表 服务实现类
 *
 * @author jjjshop
 * @since 2022-09-10
 */
@Slf4j
@Service
public class SmsServiceImpl extends BaseServiceImpl<SmsMapper, Sms> implements SmsService {

    @Autowired
    private SmsMapper smsMapper;

}
