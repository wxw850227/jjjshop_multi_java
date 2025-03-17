package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.SupplierCash;
import net.jjjshop.common.mapper.supplier.SupplierCashMapper;
import net.jjjshop.common.service.supplier.SupplierCashService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 供应商提现明细表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierCashServiceImpl extends BaseServiceImpl<SupplierCashMapper, SupplierCash> implements SupplierCashService {

    @Autowired
    private SupplierCashMapper supplierCashMapper;

}
