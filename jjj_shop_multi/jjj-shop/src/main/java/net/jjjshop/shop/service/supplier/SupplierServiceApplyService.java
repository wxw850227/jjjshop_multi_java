package net.jjjshop.shop.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierServiceApply;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.supplier.SupplierServiceApplyPageParam;
import net.jjjshop.shop.param.supplier.SupplierServiceApplyParam;
import net.jjjshop.shop.vo.supplier.SupplierServiceApplyVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务保障申请 服务类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
public interface SupplierServiceApplyService extends BaseService<SupplierServiceApply> {

    /**
     * 获取列表数据
     */
    Paging<SupplierServiceApplyVo> getList(SupplierServiceApplyPageParam param);

    /**
     * 退押金审核
     */
    Boolean verify(SupplierServiceApplyParam param);

}
