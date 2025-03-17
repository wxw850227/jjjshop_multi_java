package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.SupplierServiceSecurity;
import net.jjjshop.common.mapper.supplier.SupplierServiceSecurityMapper;
import net.jjjshop.common.service.supplier.SupplierServiceSecurityService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 供应商服务保障 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierServiceSecurityServiceImpl extends BaseServiceImpl<SupplierServiceSecurityMapper, SupplierServiceSecurity> implements SupplierServiceSecurityService {

    @Autowired
    private SupplierServiceSecurityMapper supplierServiceSecurityMapper;

}
