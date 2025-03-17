package net.jjjshop.supplier.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierRoleAccess;
import net.jjjshop.common.entity.supplier.SupplierRole;
import net.jjjshop.common.mapper.shop.ShopUserRoleMapper;
import net.jjjshop.common.mapper.supplier.SupplierRoleMapper;
import net.jjjshop.common.mapper.supplier.SupplierUserRoleMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.supplier.SupplierRoleParam;
import net.jjjshop.supplier.service.supplier.SupplierRoleAccessService;
import net.jjjshop.supplier.service.supplier.SupplierRoleService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家用户角色表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-28
 */
@Slf4j
@Service
public class SupplierRoleServiceImpl extends BaseServiceImpl<SupplierRoleMapper, SupplierRole> implements SupplierRoleService {
    @Autowired
    private SupplierRoleAccessService supplierRoleAccessService;
    @Autowired
    private SupplierUserRoleMapper supplierUserRoleMapper;

    /**
     * 角色列表
     * @param
     * @return
     */
    public List<SupplierRole> getList(){
        return this.list(new LambdaQueryWrapper<SupplierRole>()
                .eq(SupplierRole::getShopSupplierId,SupplierLoginUtil.getShopSupplierId())
                .orderByAsc(SupplierRole::getSort)
                .orderByAsc(SupplierRole::getCreateTime));
    }
    /**
     * 新增角色
     * @param supplierRoleParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(SupplierRoleParam supplierRoleParam){
        SupplierRole role = new SupplierRole();
        BeanUtils.copyProperties(supplierRoleParam, role);
        role.setShopSupplierId(SupplierLoginUtil.getShopSupplierId());
        // 保存主表
        this.save(role);
        // 保存权限
        this.saveAccess(supplierRoleParam, role.getRoleId());
        return true;
    }
    /**
     * 获取已选择权限
     * @param roleId
     * @return
     */
    public List<Integer> getSelectList(Integer roleId){
        List<SupplierRoleAccess> accessList = supplierRoleAccessService.list(new LambdaQueryWrapper<SupplierRoleAccess>()
                .eq(SupplierRoleAccess::getRoleId, roleId));
        List<Integer> idList = new ArrayList<>();
        accessList.forEach(item ->{
            idList.add(item.getAccessId());
        });
        return idList;
    }

    /**
     * 编辑角色
     * @param supplierRoleParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(SupplierRoleParam supplierRoleParam){
        SupplierRole role = new SupplierRole();
        BeanUtils.copyProperties(supplierRoleParam, role);
        // 保存主表
        this.updateById(role);
        // 先删除权限
        supplierRoleAccessService.remove(new LambdaQueryWrapper<SupplierRoleAccess>()
                .eq(SupplierRoleAccess::getRoleId, supplierRoleParam.getRoleId()));
        // 保存权限
        this.saveAccess(supplierRoleParam, role.getRoleId());
        return true;
    }

    /**
     * 保存权限
     * @param supplierRoleParam
     * @param roleId
     */
    private void saveAccess(SupplierRoleParam supplierRoleParam, Integer roleId){
        List<SupplierRoleAccess> accessList = new ArrayList<>();
        supplierRoleParam.getAccessId().forEach(item -> {
            SupplierRoleAccess access = new SupplierRoleAccess();
            access.setRoleId(roleId);
            access.setAccessId(item);
            accessList.add(access);
        });
        supplierRoleAccessService.saveBatch(accessList);
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Integer roleId){
        // 判断是否有用户引用该角色
        Integer count = supplierUserRoleMapper.getUserRoleCount(roleId);
        if(count > 0){
            throw new BusinessException("当前角色下存在用户，不允许删除");
        }
        supplierRoleAccessService.remove(new LambdaQueryWrapper<SupplierRoleAccess>().eq(SupplierRoleAccess::getRoleId, roleId));
        return this.removeById(roleId);
    }

}
