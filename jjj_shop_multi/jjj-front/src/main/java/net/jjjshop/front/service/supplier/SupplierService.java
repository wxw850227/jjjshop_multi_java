package net.jjjshop.front.service.supplier;

import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.supplier.SupplierPageParam;
import net.jjjshop.front.vo.page.AdVo;
import net.jjjshop.front.vo.supplier.SupplierDetailVo;
import net.jjjshop.front.vo.supplier.SupplierListVo;

import java.util.List;

/**
 * 供应商表 服务类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
public interface SupplierService extends BaseService<Supplier> {

    /**
     * 供应商分页列表
     * @param supplierPageParam
     * @return
     */
    Paging<SupplierListVo> supplierList(SupplierPageParam supplierPageParam);

    /**
     * 获取店铺信息
     * @param shopSupplierId
     * @param user
     * @return
     */
    SupplierDetailVo getDetail(Integer shopSupplierId, User user);

    /**
     * 获取广告图
     * @param shopSupplierId
     * @return
     */
    List<AdVo> getAdList(Integer shopSupplierId);
}
