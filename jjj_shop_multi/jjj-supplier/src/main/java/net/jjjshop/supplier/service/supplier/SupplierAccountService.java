package net.jjjshop.supplier.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierAccount;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.supplier.param.supplier.SupplierAccountParam;
import net.jjjshop.supplier.vo.supplier.SupplierAccountVo;

/**
 * 供应商提现账号表 服务类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
public interface SupplierAccountService extends BaseService<SupplierAccount> {

    /**
     * 获取列表数据
     * @param shopSupplierId
     * @return
     */
    SupplierAccountVo detail(Integer shopSupplierId);

    /**
     * 获取列表数据
     * @param shopSupplierId
     * @param supplierAccountParam
     * @return
     */
    Boolean add(Integer shopSupplierId, SupplierAccountParam supplierAccountParam);
}
