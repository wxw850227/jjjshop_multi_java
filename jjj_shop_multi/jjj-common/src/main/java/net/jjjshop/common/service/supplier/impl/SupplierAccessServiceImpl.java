package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.SupplierAccess;
import net.jjjshop.common.mapper.supplier.SupplierAccessMapper;
import net.jjjshop.common.service.supplier.SupplierAccessService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商家用户权限表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierAccessServiceImpl extends BaseServiceImpl<SupplierAccessMapper, SupplierAccess> implements SupplierAccessService {

    @Autowired
    private SupplierAccessMapper supplierAccessMapper;

}
