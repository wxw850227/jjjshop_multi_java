package net.jjjshop.supplier.service.supplier.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierRoleAccess;
import net.jjjshop.common.mapper.supplier.SupplierRoleAccessMapper;
import net.jjjshop.supplier.service.supplier.SupplierRoleAccessService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商家用户角色权限关系表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-28
 */
@Slf4j
@Service
public class SupplierRoleAccessServiceImpl extends BaseServiceImpl<SupplierRoleAccessMapper, SupplierRoleAccess> implements SupplierRoleAccessService {
}
