package net.jjjshop.front.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierCategory;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.vo.supplier.SupplierCategoryListVo;

import java.util.List;

/**
 * banner类型 服务类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
public interface SupplierCategoryService extends BaseService<SupplierCategory> {

    /**
     * 所有供应商分类
     * @param
     * @return
     */
    List<SupplierCategoryListVo> getAll();

    /**
     * 供应商分类详情
     * @param
     * @return
     */
    SupplierCategoryListVo detail(Integer categoryId);
}
