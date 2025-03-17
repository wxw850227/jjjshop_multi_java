package net.jjjshop.supplier.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierOptLog;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.auth.OptLogPageParam;
import net.jjjshop.supplier.vo.auth.SupplierOptLogVo;

/**
 * 管理员操作记录表 服务类
 *
 * @author jjjshop
 * @since 2022-10-31
 */
public interface SupplierOptLogService extends BaseService<SupplierOptLog> {

    /**
     * 获取列表数据
     * @param shopSupplierId
     * @param optLogPageParam
     * @return
     */
    Paging<SupplierOptLogVo> getList(Integer shopSupplierId, OptLogPageParam optLogPageParam);

}
