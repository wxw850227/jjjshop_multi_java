package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.SupplierAccount;
import net.jjjshop.common.mapper.supplier.SupplierAccountMapper;
import net.jjjshop.common.service.supplier.SupplierAccountService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 供应商提现账号表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierAccountServiceImpl extends BaseServiceImpl<SupplierAccountMapper, SupplierAccount> implements SupplierAccountService {

    @Autowired
    private SupplierAccountMapper supplierAccountMapper;

}
