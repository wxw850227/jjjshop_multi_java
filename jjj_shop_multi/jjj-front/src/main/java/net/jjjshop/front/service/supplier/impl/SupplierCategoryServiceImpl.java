package net.jjjshop.front.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierCategory;
import net.jjjshop.common.mapper.supplier.SupplierCategoryMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.supplier.SupplierCategoryService;
import net.jjjshop.front.vo.supplier.SupplierCategoryListVo;
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

    /**
     * 所有供应商分类
     * @param
     * @return
     */
    public List<SupplierCategoryListVo> getAll() {
        List<SupplierCategory> list = this.list(new LambdaQueryWrapper<SupplierCategory>().orderByAsc(SupplierCategory::getCreateTime));
        List<SupplierCategoryListVo> result = list.stream().map(e -> {
            SupplierCategoryListVo vo = new SupplierCategoryListVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 供应商分类详情
     * @param
     * @return
     */
    public SupplierCategoryListVo detail(Integer categoryId) {
        SupplierCategory category = this.getById(categoryId);
        SupplierCategoryListVo vo = new SupplierCategoryListVo();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }
}
