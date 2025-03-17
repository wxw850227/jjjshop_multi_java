package net.jjjshop.supplier.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierLoginLog;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.auth.LoginLogPageParam;
import net.jjjshop.supplier.vo.auth.SupplierLoginLogVo;

/**
 * 管理员登录记录表 服务类
 *
 * @author jjjshop
 * @since 2022-10-31
 */
public interface SupplierLoginLogService extends BaseService<SupplierLoginLog> {

    /**
     * 获取列表数据
     * @param shopSupplierId
     * @param loginLogPageParam
     * @return
     */
    Paging<SupplierLoginLogVo> getList(Integer shopSupplierId, LoginLogPageParam loginLogPageParam);

}
