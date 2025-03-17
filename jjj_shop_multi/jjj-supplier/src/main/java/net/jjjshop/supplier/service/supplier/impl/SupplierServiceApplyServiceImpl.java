package net.jjjshop.supplier.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierServiceApply;
import net.jjjshop.common.mapper.supplier.SupplierServiceApplyMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.supplier.param.supplier.SupplierServiceApplyParam;
import net.jjjshop.supplier.service.supplier.SupplierServiceApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务保障申请 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierServiceApplyServiceImpl extends BaseServiceImpl<SupplierServiceApplyMapper, SupplierServiceApply> implements SupplierServiceApplyService {

    /**
     * 服务申请
     * @param supplierServiceApplyParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean apply(SupplierServiceApplyParam supplierServiceApplyParam){
        SupplierServiceApply apply = this.getOne(new LambdaQueryWrapper<SupplierServiceApply>().eq(SupplierServiceApply::getServiceSecurityId, supplierServiceApplyParam.getServiceSecurityId())
                .eq(SupplierServiceApply::getShopSupplierId, supplierServiceApplyParam.getShopSupplierId()));
        if(apply != null && apply.getStatus()<=1) {
            throw new BusinessException("已经成功申请或者审核中");
        }
        if(apply != null) {
            apply.setStatus(0);
            this.updateById(apply);
        }else {
            SupplierServiceApply serviceApply = new SupplierServiceApply();
            serviceApply.setServiceSecurityId(supplierServiceApplyParam.getServiceSecurityId());
            serviceApply.setShopSupplierId(supplierServiceApplyParam.getShopSupplierId());
            serviceApply.setStatus(supplierServiceApplyParam.getStatus());
            this.save(serviceApply);
        }
        return true;
    }

    /**
     * 退出服务
     * @param supplierServiceApplyParam
     * @return
     */
    public Boolean quit(SupplierServiceApplyParam supplierServiceApplyParam) {
        SupplierServiceApply apply = this.getOne(new LambdaQueryWrapper<SupplierServiceApply>().eq(SupplierServiceApply::getServiceSecurityId, supplierServiceApplyParam.getServiceSecurityId())
                .eq(SupplierServiceApply::getShopSupplierId, supplierServiceApplyParam.getShopSupplierId()));
        if(apply.getStatus() != 1) {
            throw new BusinessException("当前状态不允许退出");
        }
        apply.setStatus(2);
        return this.updateById(apply);
    }

    /**
     * 获取申请审核状态
     * @param value
     * @param shopSupplierId
     * @return
     */
    public Integer getApplyStatus(Integer value, Integer shopSupplierId) {
        SupplierServiceApply apply = this.getOne(new LambdaQueryWrapper<SupplierServiceApply>().eq(SupplierServiceApply::getServiceSecurityId, value)
                .eq(SupplierServiceApply::getShopSupplierId, shopSupplierId));
        if(apply!=null) {
            return apply.getStatus();
        }else {
            return -1;
        }
    }
}
