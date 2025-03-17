package net.jjjshop.shop.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierCash;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.supplier.SupplierCashPageParam;
import net.jjjshop.shop.param.supplier.SupplierCashParam;
import net.jjjshop.shop.vo.supplier.SupplierCashVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 供应商提现明细表 服务类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
public interface SupplierCashService extends BaseService<SupplierCash> {

    /**
     * 获取列表数据
     */
    Paging<SupplierCashVo> getList(SupplierCashPageParam supplierCashPageParam);

    /**
     * 分销商提现审核
     */
    Boolean submit(SupplierCashParam supplierCashParam);


    /**
     * 确认已打款
     */
    Boolean money(Integer id);

}
