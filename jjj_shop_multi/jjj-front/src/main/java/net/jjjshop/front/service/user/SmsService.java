package net.jjjshop.front.service.user;

import net.jjjshop.common.entity.Sms;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 用户注册短信表 服务类
 *
 * @author jjjshop
 * @since 2022-09-10
 */
public interface SmsService extends BaseService<Sms> {
    /**
     * 发送短信
     * @param mobile
     * @param type
     * @return
     */
    Boolean send(String mobile, String type);
}
