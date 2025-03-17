package net.jjjshop.supplier.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierCapital;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.supplier.SupplierCapitalPageParam;

/**
 * 供应商资金明细表 服务类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
public interface SupplierCapitalService extends BaseService<SupplierCapital> {

    /**
     * 获取列表数据
     * @param shopSupplierId
     * @param param
     * @return
     */
    Paging getList(Integer shopSupplierId, SupplierCapitalPageParam param);
}
