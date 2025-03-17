package net.jjjshop.shop.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierApply;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.supplier.SupplierApplyPageParam;
import net.jjjshop.shop.param.supplier.SupplierApplyParam;
import net.jjjshop.shop.vo.supplier.SupplierApplyVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商户申请表 服务类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
public interface SupplierApplyService extends BaseService<SupplierApply> {

    /**
     * 获取列表数据
     */
    Paging<SupplierApplyVo> getList(SupplierApplyPageParam supplierApplyPageParam);

    /**
     * 获取详情数据
     */
    SupplierApplyVo detail(Integer supplierApplyId);


    /**
     * 审核
     */
    Boolean audit(SupplierApplyParam supplierApplyParam);
}
