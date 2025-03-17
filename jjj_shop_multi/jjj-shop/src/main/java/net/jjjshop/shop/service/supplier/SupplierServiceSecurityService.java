package net.jjjshop.shop.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierServiceSecurity;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.shop.param.supplier.SupplierSecurityParam;
import net.jjjshop.shop.vo.supplier.SupplierSecurityVo;

import java.util.List;

/**
 * 供应商服务保障 服务类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
public interface SupplierServiceSecurityService extends BaseService<SupplierServiceSecurity> {

    /**
     * 获取列表
     */
    List<SupplierSecurityVo> getList();

    /**
     * 添加供应商服务保障
     * @param
     * @return
     */
    boolean add(SupplierSecurityParam supplierSecurityParam);

    /**
     * 修改供应商服务保障
     * @param
     * @return
     */
    boolean edit(SupplierSecurityParam supplierSecurityParam);

    /**
     * 真删除供应商服务保障
     * @param id
     * @return
     */
    boolean delById(Integer id);
}
