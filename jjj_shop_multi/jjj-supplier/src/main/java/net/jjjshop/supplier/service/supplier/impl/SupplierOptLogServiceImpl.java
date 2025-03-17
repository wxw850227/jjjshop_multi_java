package net.jjjshop.supplier.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierOptLog;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.mapper.supplier.SupplierOptLogMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.auth.OptLogPageParam;
import net.jjjshop.supplier.service.supplier.SupplierOptLogService;
import net.jjjshop.supplier.service.supplier.SupplierUserService;
import net.jjjshop.supplier.vo.auth.SupplierOptLogVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    private SupplierUserService supplierUserService;


    /**
     * 获取列表数据
     * @param shopSupplierId
     * @param optLogPageParam
     * @return
     */
    public Paging<SupplierOptLogVo> getList(Integer shopSupplierId, OptLogPageParam optLogPageParam) {
        Page<SupplierOptLog> page = new PageInfo(optLogPageParam);
        LambdaQueryWrapper<SupplierOptLog> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(optLogPageParam.getUsername())){
            List<Integer> userIds = supplierUserService.list(new LambdaQueryWrapper<SupplierUser>()
                            .and(i -> i.or().like(SupplierUser::getUserName, optLogPageParam.getUsername())
                                    .or().like(SupplierUser::getRealName, optLogPageParam.getUsername())))
                    .stream().map(SupplierUser::getSupplierUserId).collect(Collectors.toList());
            wrapper.in(SupplierOptLog::getSupplierUserId, userIds);
        }
        wrapper.eq(SupplierOptLog::getShopSupplierId, shopSupplierId);
        wrapper.orderByDesc(SupplierOptLog::getCreateTime);
        IPage<SupplierOptLog> iPage = this.page(page, wrapper);
        IPage<SupplierOptLogVo> result = iPage.convert(item -> {
            SupplierOptLogVo vo = new SupplierOptLogVo();
            BeanUtils.copyProperties(item, vo);
            SupplierUser user = supplierUserService.getById(vo.getSupplierUserId());
            vo.setUserName(user.getUserName());
            vo.setRealName(user.getRealName());
            return vo;
        });
        return new Paging<>(result);
    }

}
