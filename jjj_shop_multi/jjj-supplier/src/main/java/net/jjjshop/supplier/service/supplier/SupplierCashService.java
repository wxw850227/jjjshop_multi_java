package net.jjjshop.supplier.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierCash;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.supplier.SupplierCashPageParam;
import net.jjjshop.supplier.param.supplier.SupplierCashParam;
import net.jjjshop.supplier.vo.supplier.SupplierCashVo;

/**
 * 供应商提现明细表 服务类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
public interface SupplierCashService extends BaseService<SupplierCash> {

    /**
     * 获取列表
     * @param shopSupplierId
     * @param supplierCashPageParam
     * @return
     */
    Paging<SupplierCashVo> getList(Integer shopSupplierId, SupplierCashPageParam supplierCashPageParam);

    /**
     * 提交申请
     * @param shopSupplierId
     * @param supplierCashParam
     * @return
     */
    Boolean submit(Integer shopSupplierId, SupplierCashParam supplierCashParam);

}
