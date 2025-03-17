package net.jjjshop.supplier.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderSettled;
import net.jjjshop.common.entity.supplier.SupplierCapital;
import net.jjjshop.common.mapper.supplier.SupplierCapitalMapper;
import net.jjjshop.config.constant.CommonConstant;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.supplier.SupplierCapitalPageParam;
import net.jjjshop.supplier.service.supplier.SupplierCapitalService;
import net.jjjshop.supplier.vo.supplier.SupplierCapitalVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 供应商资金明细表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierCapitalServiceImpl extends BaseServiceImpl<SupplierCapitalMapper, SupplierCapital> implements SupplierCapitalService {

    /**
     * 获取列表数据
     * @param shopSupplierId
     * @param param
     * @return
     */
    public Paging getList(Integer shopSupplierId, SupplierCapitalPageParam param) {
        Page<SupplierCapital> page = new PageInfo<>(param);
        LambdaQueryWrapper<SupplierCapital> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(param.getStartDay())) {
            wrapper.ge(SupplierCapital::getCreateTime, param.getStartDay()+" 00:00:00");
        }
        if (StringUtils.isNotEmpty(param.getEndDay())) {
            wrapper.lt(SupplierCapital::getCreateTime, param.getEndDay()+" 23:59:59");
        }
        if(param.getFlowType() != null && param.getFlowType() > 0) {
            wrapper.eq(SupplierCapital::getFlowType, param.getFlowType());
        }
        wrapper.eq(SupplierCapital::getShopSupplierId, shopSupplierId);
        wrapper.comment(CommonConstant.NOT_WITH_App_Id);
        wrapper.orderByDesc(SupplierCapital::getCreateTime);
        IPage<SupplierCapital> iPage = this.page(page, wrapper);
        IPage<SupplierCapitalVo> result = iPage.convert(item -> {
            SupplierCapitalVo vo = new SupplierCapitalVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });
        return new Paging(result);
    }

}
