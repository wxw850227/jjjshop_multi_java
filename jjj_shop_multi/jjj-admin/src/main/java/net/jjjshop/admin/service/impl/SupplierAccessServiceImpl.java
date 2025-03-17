package net.jjjshop.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.param.SupplierAccessParam;
import net.jjjshop.admin.service.SupplierAccessService;
import net.jjjshop.common.entity.supplier.SupplierAccess;
import net.jjjshop.common.mapper.supplier.SupplierAccessMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 商家用户权限表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-22
 */
@Slf4j
@Service
public class SupplierAccessServiceImpl extends BaseServiceImpl<SupplierAccessMapper, SupplierAccess> implements SupplierAccessService {
    @Autowired
    private SupplierAccessMapper supplierAccessMapper;


    public Boolean editStatusById(Integer accessId, Integer status){
        SupplierAccess access = new SupplierAccess();
        access.setAccessId(accessId);
        access.setIsShow(status);
        return this.updateById(access);
    }

    /**
     * 真删除
     * @param accessId
     * @return
     */
    public Boolean delById(Integer accessId){
        // 是否存在子菜单
        LambdaQueryWrapper<SupplierAccess> countWrapper = Wrappers.lambdaQuery();
        countWrapper.eq(SupplierAccess::getParentId, accessId);
        if(this.count(countWrapper) > 0){
            throw new BusinessException("当前菜单下存在子权限，请先删除");
        }
        return this.removeById(accessId);
    }

    /**
     * 新增
     * @param supplierAccessParam
     * @return
     */
    public Boolean add(SupplierAccessParam supplierAccessParam){
        int num = this.count(new LambdaQueryWrapper<SupplierAccess>().eq(SupplierAccess::getPath,supplierAccessParam.getPath()));
        if(num > 0){
            throw new BusinessException("该路径已存在，请更改后再试");
        }
        SupplierAccess access = new SupplierAccess();
        BeanUtils.copyProperties(supplierAccessParam, access);
        // 11位时间戳
        String timestamp = String.valueOf((new Date()).getTime()/1000);
        access.setAccessId(Integer.valueOf(timestamp));
        return this.save(access);
    }

    /**
     * 修改
     * @param supplierAccessParam
     * @return
     */
    public Boolean edit(SupplierAccessParam supplierAccessParam){
        SupplierAccess access = new SupplierAccess();
        BeanUtils.copyProperties(supplierAccessParam, access);
        return this.updateById(access);
    }
}
