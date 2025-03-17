package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.SupplierRole;
import net.jjjshop.common.mapper.supplier.SupplierRoleMapper;
import net.jjjshop.common.service.supplier.SupplierRoleService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商家用户角色表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-28
 */
@Slf4j
@Service
public class SupplierRoleServiceImpl extends BaseServiceImpl<SupplierRoleMapper, SupplierRole> implements SupplierRoleService {

    @Autowired
    private SupplierRoleMapper supplierRoleMapper;

}
