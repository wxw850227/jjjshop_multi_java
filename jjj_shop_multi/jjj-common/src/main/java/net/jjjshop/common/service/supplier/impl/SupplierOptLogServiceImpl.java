package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.SupplierOptLog;
import net.jjjshop.common.mapper.supplier.SupplierOptLogMapper;
import net.jjjshop.common.service.supplier.SupplierOptLogService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 管理员操作记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-31
 */
@Slf4j
@Service
public class SupplierOptLogServiceImpl extends BaseServiceImpl<SupplierOptLogMapper, SupplierOptLog> implements SupplierOptLogService {

    @Autowired
    private SupplierOptLogMapper supplierOptLogMapper;

}
