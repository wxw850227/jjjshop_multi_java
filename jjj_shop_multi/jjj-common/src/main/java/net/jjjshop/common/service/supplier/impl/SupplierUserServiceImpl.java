package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.mapper.supplier.SupplierUserMapper;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商家用户记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
@Slf4j
@Service
public class SupplierUserServiceImpl extends BaseServiceImpl<SupplierUserMapper, SupplierUser> implements SupplierUserService {

    @Autowired
    private SupplierUserMapper supplierUserMapper;

}
