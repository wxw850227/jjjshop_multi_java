package net.jjjshop.supplier.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierServiceApply;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.supplier.param.supplier.SupplierServiceApplyParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务保障申请 服务类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
public interface SupplierServiceApplyService extends BaseService<SupplierServiceApply> {

    /**
     * 服务申请
     * @param supplierServiceApplyParam
     * @return
     */
    Boolean apply(SupplierServiceApplyParam supplierServiceApplyParam);

    /**
     * 退出服务
     * @param supplierServiceApplyParam
     * @return
     */
    Boolean quit(SupplierServiceApplyParam supplierServiceApplyParam);

    /**
     * 获取申请审核状态
     * @param value
     * @param shopSupplierId
     * @return
     */
    Integer getApplyStatus(Integer value, Integer shopSupplierId);
}
