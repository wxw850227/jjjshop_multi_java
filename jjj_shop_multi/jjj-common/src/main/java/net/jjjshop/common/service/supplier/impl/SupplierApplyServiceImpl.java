package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.SupplierApply;
import net.jjjshop.common.mapper.supplier.SupplierApplyMapper;
import net.jjjshop.common.service.supplier.SupplierApplyService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商户申请表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierApplyServiceImpl extends BaseServiceImpl<SupplierApplyMapper, SupplierApply> implements SupplierApplyService {

    @Autowired
    private SupplierApplyMapper supplierApplyMapper;

}
