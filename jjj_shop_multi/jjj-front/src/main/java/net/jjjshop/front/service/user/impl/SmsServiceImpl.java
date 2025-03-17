package net.jjjshop.front.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.Sms;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.factory.sms.impl.AliyunSmsFactoryService;
import net.jjjshop.common.mapper.user.SmsMapper;
import net.jjjshop.common.settings.vo.SmsVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.user.SmsService;
import net.jjjshop.front.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private SettingUtils settingUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private AliyunSmsFactoryService aliyunSmsFactoryService;

    /**
     * 发送短信
     * @param mobile
     * @param type
     * @return
     */
    public Boolean send(String mobile, String type){
        if(StringUtils.isEmpty(mobile)){
            throw new BusinessException("手机号码不能为空");
        }
        // 短信设置
        JSONObject vo = settingUtils.getSetting(SettingEnum.SMS.getKey(), null);
        SmsVo smsVo = JSONObject.toJavaObject(vo, SmsVo.class);
        String templateCode = smsVo.getTemplateCode();
        if (StringUtils.isEmpty(templateCode)) {
            throw new BusinessException("短信登录未开启");
        }
        // 如果是注册
        if ("register".equals(type)) {
            //判断是否已经注册
            Integer count = userService.count(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile));
            if (count > 0) {
                throw new BusinessException("手机号码已存在");
            }
        }
        // 随机6位数验证码
        String code = String.valueOf((int)(Math.random()*900000 + 100000));
        JSONObject templateParam = new JSONObject();
        templateParam.put("code", code);
        Boolean sendFlag = aliyunSmsFactoryService.sendSms(mobile, templateCode, templateParam.toJSONString());
        if(sendFlag){
            Sms sms = new Sms();
            sms.setCode(code);
            sms.setMobile(mobile);
            this.save(sms);
        }
        return true;
    }
}
