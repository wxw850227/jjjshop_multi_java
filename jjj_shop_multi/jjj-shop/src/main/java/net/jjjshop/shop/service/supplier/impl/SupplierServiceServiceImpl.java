package net.jjjshop.shop.service.supplier.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierService;
import net.jjjshop.common.mapper.supplier.SupplierServiceMapper;
import net.jjjshop.common.service.supplier.SupplierServiceService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 供应商客服表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierServiceServiceImpl extends BaseServiceImpl<SupplierServiceMapper, SupplierService> implements SupplierServiceService {
}
