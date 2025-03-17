package net.jjjshop.supplier.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopLoginLog;
import net.jjjshop.common.entity.supplier.SupplierLoginLog;
import net.jjjshop.common.mapper.supplier.SupplierLoginLogMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.auth.LoginLogPageParam;
import net.jjjshop.supplier.service.supplier.SupplierLoginLogService;
import net.jjjshop.supplier.vo.auth.SupplierLoginLogVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理员登录记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-31
 */
@Slf4j
@Service
public class SupplierLoginLogServiceImpl extends BaseServiceImpl<SupplierLoginLogMapper, SupplierLoginLog> implements SupplierLoginLogService {

    /**
     * 获取列表数据
     * @param shopSupplierId
     * @param loginLogPageParam
     * @return
     */
    public Paging<SupplierLoginLogVo> getList(Integer shopSupplierId, LoginLogPageParam loginLogPageParam) {
        Page<SupplierLoginLog> page = new PageInfo(loginLogPageParam);
        LambdaQueryWrapper<SupplierLoginLog> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(loginLogPageParam.getUsername())){
            wrapper.like(SupplierLoginLog::getUsername, loginLogPageParam.getUsername());
        }
        wrapper.eq(SupplierLoginLog::getShopSupplierId, shopSupplierId);
        wrapper.orderByDesc(SupplierLoginLog::getCreateTime);
        IPage<SupplierLoginLog> iPage = this.page(page, wrapper);
        IPage<SupplierLoginLogVo> result = iPage.convert(item -> {
            SupplierLoginLogVo vo = new SupplierLoginLogVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });
        return new Paging(result);
    }

}
