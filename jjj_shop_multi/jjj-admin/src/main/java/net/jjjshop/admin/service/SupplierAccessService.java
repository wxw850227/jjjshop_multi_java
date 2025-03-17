package net.jjjshop.admin.service;

import net.jjjshop.admin.param.SupplierAccessParam;
import net.jjjshop.common.entity.supplier.SupplierAccess;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 商家用户权限表 服务类
 *
 * @author jjjshop
 * @since 2022-06-22
 */
public interface SupplierAccessService extends BaseService<SupplierAccess> {

    Boolean editStatusById(Integer accessId, Integer status);

    /**
     * 真删除
     * @param accessId
     * @return
     */
    Boolean delById(Integer accessId);
    /**
     * 新增
     * @param supplierAccessParam
     * @return
     */
    Boolean add(SupplierAccessParam supplierAccessParam);
    /**
     * 修改
     * @param supplierAccessParam
     * @return
     */
    Boolean edit(SupplierAccessParam supplierAccessParam);
}
