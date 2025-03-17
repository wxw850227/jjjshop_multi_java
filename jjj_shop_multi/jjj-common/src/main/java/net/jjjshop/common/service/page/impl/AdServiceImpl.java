package net.jjjshop.common.service.page.impl;

import net.jjjshop.common.entity.page.Ad;
import net.jjjshop.common.mapper.page.AdMapper;
import net.jjjshop.common.service.page.AdService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * banner图 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-25
 */
@Slf4j
@Service
public class AdServiceImpl extends BaseServiceImpl<AdMapper, Ad> implements AdService {

    @Autowired
    private AdMapper adMapper;

}
