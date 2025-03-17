package net.jjjshop.shop.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierCategory;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.shop.param.supplier.SupplierCategoryParam;
import net.jjjshop.shop.vo.supplier.SupplierCategoryVo;

import java.util.List;

/**
 * banner类型 服务类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
public interface SupplierCategoryService extends BaseService<SupplierCategory> {
    /**
     * 商户分类分页列表
     * @param
     * @return
     */
    List<SupplierCategoryVo> getList();

    /**
     * 商户分类分页列表
     * @param
     * @return
     */
    SupplierCategoryVo detail(Integer id);
    /**
     * 添加商户分类
     * @param
     * @return
     */
    boolean add(SupplierCategoryParam supplierCategoryParam);

    /**
     * 修改商户分类
     * @param
     * @return
     */
    boolean edit(SupplierCategoryParam supplierCategoryParam);

    /**
     * 真删除商户分类
     * @param id
     * @return
     */
    boolean delById(Integer id);
}
