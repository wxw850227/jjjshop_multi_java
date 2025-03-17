package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.SupplierUserRole;
import net.jjjshop.common.mapper.supplier.SupplierUserRoleMapper;
import net.jjjshop.common.service.supplier.SupplierUserRoleService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商家用户角色记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-19
 */
@Slf4j
@Service
public class SupplierUserRoleServiceImpl extends BaseServiceImpl<SupplierUserRoleMapper, SupplierUserRole> implements SupplierUserRoleService {

    @Autowired
    private SupplierUserRoleMapper supplierUserRoleMapper;

}
