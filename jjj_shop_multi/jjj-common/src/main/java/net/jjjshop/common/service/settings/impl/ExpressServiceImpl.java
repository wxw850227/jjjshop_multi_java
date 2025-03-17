package net.jjjshop.common.service.settings.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.common.mapper.settings.ExpressMapper;
import net.jjjshop.common.service.settings.ExpressService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 物流公司记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-19
 */
@Slf4j
@Service
public class ExpressServiceImpl extends BaseServiceImpl<ExpressMapper, Express> implements ExpressService {
}
