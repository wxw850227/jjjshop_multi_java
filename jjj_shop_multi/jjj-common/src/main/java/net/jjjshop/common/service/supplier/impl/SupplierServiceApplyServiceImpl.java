package net.jjjshop.common.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjshop.common.entity.supplier.SupplierServiceApply;
import net.jjjshop.common.entity.supplier.SupplierServiceSecurity;
import net.jjjshop.common.mapper.supplier.SupplierServiceApplyMapper;
import net.jjjshop.common.service.supplier.SupplierServiceApplyService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 服务保障申请 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierServiceApplyServiceImpl extends BaseServiceImpl<SupplierServiceApplyMapper, SupplierServiceApply> implements SupplierServiceApplyService {

    @Autowired
    private SupplierServiceApplyMapper supplierServiceApplyMapper;


}
