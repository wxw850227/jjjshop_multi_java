package net.jjjshop.supplier.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierServiceSecurity;
import net.jjjshop.common.mapper.supplier.SupplierServiceSecurityMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.supplier.service.supplier.SupplierServiceApplyService;
import net.jjjshop.supplier.service.supplier.SupplierServiceSecurityService;
import net.jjjshop.supplier.vo.supplier.SupplierSecurityVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    private SupplierServiceApplyService supplierServiceApplyService;

    /**
     * 获取列表
     * @param shopSupplierId
     * @return
     */
    public List<SupplierSecurityVo> getList(Integer shopSupplierId){
        return this.list(new LambdaQueryWrapper<SupplierServiceSecurity>()
                        .eq(SupplierServiceSecurity::getStatus, 1)
                .orderByDesc(SupplierServiceSecurity::getCreateTime))
                .stream().map(e->{
                    SupplierSecurityVo vo = new SupplierSecurityVo();
                    BeanUtils.copyProperties(e, vo);
                    Integer applyStatus = supplierServiceApplyService.getApplyStatus(vo.getServiceSecurityId(), shopSupplierId);
                    vo.setStatus(applyStatus>-1?applyStatus:2);
                    return vo;
                }).collect(Collectors.toList());
    }

}
