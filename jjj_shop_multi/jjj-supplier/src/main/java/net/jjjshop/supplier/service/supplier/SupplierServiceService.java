package net.jjjshop.supplier.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierService;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.supplier.param.supplier.SupplierServiceParam;

/**
 * 供应商客服表 服务类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
public interface SupplierServiceService extends BaseService<SupplierService> {



    /**
     * 保存供应商服务
     * @param shopSupplierId
     * @param serviceParam
     * @return
     */
    Boolean saveService(Integer shopSupplierId, SupplierServiceParam serviceParam);

}
