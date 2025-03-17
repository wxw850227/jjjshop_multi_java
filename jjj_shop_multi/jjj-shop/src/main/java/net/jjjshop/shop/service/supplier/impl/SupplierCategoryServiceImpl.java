package net.jjjshop.shop.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierCategory;
import net.jjjshop.common.mapper.supplier.SupplierCategoryMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.param.setting.ExpressParam;
import net.jjjshop.shop.param.supplier.SupplierCategoryParam;
import net.jjjshop.shop.service.supplier.SupplierCategoryService;
import net.jjjshop.shop.service.supplier.SupplierService;
import net.jjjshop.shop.vo.supplier.SupplierCategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * banner类型 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
@Slf4j
@Service
public class SupplierCategoryServiceImpl extends BaseServiceImpl<SupplierCategoryMapper, SupplierCategory> implements SupplierCategoryService {
    @Autowired
    private SupplierService supplierService;

    /**
     * 商户分类分页列表
     * @param
     * @return
     */
    public List<SupplierCategoryVo> getList(){
        return this.list(new LambdaQueryWrapper<SupplierCategory>()
                .orderByAsc(SupplierCategory::getCreateTime))
                .stream().map(e->{
                    SupplierCategoryVo vo = new SupplierCategoryVo();
                    BeanUtils.copyProperties(e, vo);
                    return vo;
                }).collect(Collectors.toList());
    }

    /**
     * 商户分类分页列表
     * @param
     * @return
     */
    public SupplierCategoryVo detail(Integer id){
        SupplierCategory supplierCategory = this.getById(id);
        SupplierCategoryVo vo = new SupplierCategoryVo();
        BeanUtils.copyProperties(supplierCategory, vo);
        return vo;
    }

    /**
     * 添加商户分类
     * @param
     * @return
     */
    public boolean add(SupplierCategoryParam supplierCategoryParam) {
        SupplierCategory supplierCategory = new SupplierCategory();
        BeanUtils.copyProperties(supplierCategoryParam,supplierCategory);
        return this.save(supplierCategory);
    }

    /**
     * 修改商户分类
     * @param
     * @return
     */
    public boolean edit(SupplierCategoryParam supplierCategoryParam) {
        SupplierCategory supplierCategory = new SupplierCategory();
        BeanUtils.copyProperties(supplierCategoryParam,supplierCategory);
        return this.updateById(supplierCategory);
    }

    /**
     * 真删除商户分类
     * @param id
     * @return
     */
    public boolean delById(Integer id) {
        Integer count = supplierService.count(new LambdaQueryWrapper<Supplier>().eq(Supplier::getCategoryId, id));
        if (count.intValue() > 0) {
            throw new BusinessException("已经有" + count + "个订单在使用该物流公司，不允许删除");
        }
        return this.removeById(id);
    }
}
