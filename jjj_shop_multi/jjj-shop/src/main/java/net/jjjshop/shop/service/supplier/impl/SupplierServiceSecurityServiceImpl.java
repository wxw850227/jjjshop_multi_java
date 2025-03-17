package net.jjjshop.shop.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.common.entity.supplier.SupplierServiceApply;
import net.jjjshop.common.entity.supplier.SupplierServiceSecurity;
import net.jjjshop.common.mapper.supplier.SupplierServiceSecurityMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.param.setting.ExpressParam;
import net.jjjshop.shop.param.supplier.SupplierSecurityParam;
import net.jjjshop.shop.service.supplier.SupplierServiceApplyService;
import net.jjjshop.shop.service.supplier.SupplierServiceSecurityService;
import net.jjjshop.shop.vo.supplier.SupplierSecurityVo;
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
     */
    public List<SupplierSecurityVo> getList(){
        return this.list(new LambdaQueryWrapper<SupplierServiceSecurity>()
                .orderByDesc(SupplierServiceSecurity::getCreateTime))
                .stream().map(e->{
                    SupplierSecurityVo vo = new SupplierSecurityVo();
                    BeanUtils.copyProperties(e, vo);
                    return vo;
                }).collect(Collectors.toList());
    }

    /**
     * 添加供应商服务保障
     * @param
     * @return
     */
    public boolean add(SupplierSecurityParam supplierSecurityParam) {
        SupplierServiceSecurity security = new SupplierServiceSecurity();
        BeanUtils.copyProperties(supplierSecurityParam, security);
        return this.save(security);
    }

    /**
     * 修改供应商服务保障
     * @param
     * @return
     */
    public boolean edit(SupplierSecurityParam supplierSecurityParam) {
        SupplierServiceSecurity security = new SupplierServiceSecurity();
        BeanUtils.copyProperties(supplierSecurityParam, security);
        return this.updateById(security);
    }

    /**
     * 真删除供应商服务保障
     * @param id
     * @return
     */
    public boolean delById(Integer id) {
        Integer count = supplierServiceApplyService.count(new LambdaQueryWrapper<SupplierServiceApply>()
                .eq(SupplierServiceApply::getServiceSecurityId, id)
                .in(SupplierServiceApply::getStatus,new Integer[]{1,2}));
        if (count.intValue() > 0) {
            throw new BusinessException("该服务使用中，不允许删除");
        }
        return this.removeById(id);
    }

}
