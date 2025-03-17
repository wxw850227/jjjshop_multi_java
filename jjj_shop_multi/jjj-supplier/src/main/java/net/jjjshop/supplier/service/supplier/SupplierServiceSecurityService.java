package net.jjjshop.supplier.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierServiceSecurity;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.supplier.vo.supplier.SupplierSecurityVo;

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
     * @param shopSupplierId
     * @return
     */
    List<SupplierSecurityVo> getList(Integer shopSupplierId);

}
