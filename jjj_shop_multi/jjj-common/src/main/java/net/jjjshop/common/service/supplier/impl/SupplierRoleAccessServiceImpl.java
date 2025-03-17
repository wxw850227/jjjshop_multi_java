package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.SupplierRoleAccess;
import net.jjjshop.common.mapper.supplier.SupplierRoleAccessMapper;
import net.jjjshop.common.service.supplier.SupplierRoleAccessService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商家用户角色权限关系表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-28
 */
@Slf4j
@Service
public class SupplierRoleAccessServiceImpl extends BaseServiceImpl<SupplierRoleAccessMapper, SupplierRoleAccess> implements SupplierRoleAccessService {

    @Autowired
    private SupplierRoleAccessMapper supplierRoleAccessMapper;

}
