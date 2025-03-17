package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.SupplierCapital;
import net.jjjshop.common.mapper.supplier.SupplierCapitalMapper;
import net.jjjshop.common.service.supplier.SupplierCapitalService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 供应商资金明细表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierCapitalServiceImpl extends BaseServiceImpl<SupplierCapitalMapper, SupplierCapital> implements SupplierCapitalService {

    @Autowired
    private SupplierCapitalMapper supplierCapitalMapper;

}
