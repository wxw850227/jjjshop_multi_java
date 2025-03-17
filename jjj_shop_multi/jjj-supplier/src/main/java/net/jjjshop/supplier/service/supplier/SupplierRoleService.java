package net.jjjshop.supplier.service.supplier;

import net.jjjshop.common.entity.shop.ShopRole;
import net.jjjshop.common.entity.supplier.SupplierRole;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.supplier.param.supplier.SupplierRoleParam;

import java.util.List;

/**
 * 商家用户角色表 服务类
 *
 * @author jjjshop
 * @since 2022-10-28
 */
public interface SupplierRoleService extends BaseService<SupplierRole> {
    /**
     * 角色列表
     * @return
     */
    List<SupplierRole> getList();

    /**
     * 新增角色
     * @param supplierRoleParam
     * @return
     */
    Boolean add(SupplierRoleParam supplierRoleParam);

    /**
     * 获取已选择权限
     * @param roleId
     * @return
     */
    List<Integer> getSelectList(Integer roleId);

    /**
     * 编辑角色
     * @param supplierRoleParam
     * @return
     */
    Boolean edit(SupplierRoleParam supplierRoleParam);

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    Boolean delete(Integer roleId);
}
