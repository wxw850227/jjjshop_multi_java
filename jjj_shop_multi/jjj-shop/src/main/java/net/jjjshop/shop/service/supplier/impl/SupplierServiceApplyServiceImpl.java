package net.jjjshop.shop.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierServiceApply;
import net.jjjshop.common.entity.supplier.SupplierServiceSecurity;
import net.jjjshop.common.mapper.supplier.SupplierServiceApplyMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.supplier.SupplierServiceApplyPageParam;
import net.jjjshop.shop.param.supplier.SupplierServiceApplyParam;
import net.jjjshop.shop.service.supplier.SupplierService;
import net.jjjshop.shop.service.supplier.SupplierServiceApplyService;
import net.jjjshop.shop.service.supplier.SupplierServiceSecurityService;
import net.jjjshop.shop.service.supplier.SupplierServiceService;
import net.jjjshop.shop.vo.supplier.SupplierServiceApplyVo;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierServiceSecurityService securityService;

    /**
     * 获取列表数据
     */
    public Paging<SupplierServiceApplyVo> getList(SupplierServiceApplyPageParam param) {
        Page<SupplierServiceApply> page = new PageInfo<>(param);
        LambdaQueryWrapper<SupplierServiceApply> wrapper = new LambdaQueryWrapper<>();
        if (param.getStatus() != null) {
            wrapper.eq(SupplierServiceApply::getStatus, param.getStatus());
        }
        wrapper.orderByDesc(SupplierServiceApply::getCreateTime);
        IPage<SupplierServiceApply> iPage = this.page(page, wrapper);
        IPage<SupplierServiceApplyVo> result = iPage.convert(item -> {
            SupplierServiceApplyVo vo = new SupplierServiceApplyVo();
            BeanUtils.copyProperties(item, vo);
            vo.setSupplierName(supplierService.getById(vo.getShopSupplierId()).getName());
            SupplierServiceSecurity security = securityService.getById(vo.getServiceSecurityId());
            vo.setServerName(security.getName());
            vo.setServerLogo(security.getLogo());
            return vo;
        });
        return new Paging<>(result);
    }

    /**
     * 退押金审核
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean verify(SupplierServiceApplyParam param) {
        SupplierServiceApply apply = this.getById(param.getServiceApplyId());
        apply.setStatus(param.getState());
        return this.updateById(apply);
    }

}
