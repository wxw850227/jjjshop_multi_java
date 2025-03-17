package net.jjjshop.supplier.service.ad.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.page.AdCategory;
import net.jjjshop.common.mapper.page.AdCategoryMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.supplier.service.ad.AdCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
