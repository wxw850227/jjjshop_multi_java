package net.jjjshop.common.service.page.impl;

import net.jjjshop.common.entity.page.AdCategory;
import net.jjjshop.common.mapper.page.AdCategoryMapper;
import net.jjjshop.common.service.page.AdCategoryService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * banner类型 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-25
 */
@Slf4j
@Service
public class AdCategoryServiceImpl extends BaseServiceImpl<AdCategoryMapper, AdCategory> implements AdCategoryService {

    @Autowired
    private AdCategoryMapper adCategoryMapper;

}
