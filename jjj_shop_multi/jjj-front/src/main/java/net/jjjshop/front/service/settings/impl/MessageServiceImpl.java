package net.jjjshop.front.service.settings.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.Message;
import net.jjjshop.common.entity.settings.MessageSettings;
import net.jjjshop.common.mapper.settings.MessageMapper;
import net.jjjshop.config.constant.CommonConstant;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.settings.MessageService;
import net.jjjshop.front.service.settings.MessageSettingsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用消息表 服务实现类
 * @author jjjshop
 * @since 2022-06-24
 */
@Slf4j
@Service
public class MessageServiceImpl extends BaseServiceImpl<MessageMapper, Message> implements MessageService {
    @Autowired
    private MessageSettingsService messageSettingsService;
    /**
     * 获取消息
     */
    public List<String> getMessageByNameArr(String platform, String[] messageENameArr){
        List<String> templateArr = new ArrayList<>();
        //只适用于微信
        if(!"wx".equals(platform)){
            return templateArr;
        }
        List<Message> list = this.list(new LambdaQueryWrapper<Message>()
                .eq(Message::getIsDelete, false).in(Message::getMessageEname, messageENameArr)
                .orderByAsc(Message::getSort));
        for(Message message:list){
            MessageSettings settings = messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                    .eq(MessageSettings::getMessageId, message.getMessageId()));
            if(settings != null && settings.getWxStatus() == 1 && StringUtils.isNotBlank(settings.getWxTemplate())){
                templateArr.add(JSONObject.parseObject(settings.getWxTemplate()).getString("templateId"));
            }
        }
        return templateArr;
    }
}
