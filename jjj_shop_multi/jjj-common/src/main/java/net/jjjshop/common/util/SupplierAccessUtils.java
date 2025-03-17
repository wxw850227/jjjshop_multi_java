package net.jjjshop.common.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopAccess;
import net.jjjshop.common.entity.supplier.SupplierAccess;
import net.jjjshop.common.service.shop.ShopAccessService;
import net.jjjshop.common.service.supplier.SupplierAccessService;
import net.jjjshop.common.vo.shop.ShopAccessVo;
import net.jjjshop.common.vo.shop.SupplierAccessVo;
import net.jjjshop.config.constant.CommonConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SupplierAccessUtils {
    @Autowired
    private SupplierAccessService supplierAccessService;

    public List<SupplierAccessVo> getAll(){
        LambdaQueryWrapper<SupplierAccess> wrapper = Wrappers.lambdaQuery();
        wrapper.comment(CommonConstant.NOT_WITH_App_Id);
        wrapper.orderByAsc(SupplierAccess::getSort).orderByAsc(SupplierAccess::getCreateTime);
        // 获取所有权限列表
        List<SupplierAccess> list = supplierAccessService.list(wrapper);
        // 转成vo
        List<SupplierAccessVo> voList = list.stream().map(e -> {
            SupplierAccessVo supplierAccessVo = new SupplierAccessVo();
            BeanUtils.copyProperties(e, supplierAccessVo);
            return supplierAccessVo;
        }).collect(Collectors.toList());
        // 遍历成树形结构
        List<SupplierAccessVo> collect = voList.stream()
                // 2. 找出所有顶级（规定 0 为顶级）
                .filter(o -> StrUtil.equals("0", String.valueOf(o.getParentId())))
                // 3.给当前父级的 childList 设置子
                .peek(o -> o.setChildren(getChildList(o, voList)))
                // 4.收集
                .collect(Collectors.toList());
        return collect;
    }

    // 根据当前父类 找出子类， 并通过递归找出子类的子类
    private List<SupplierAccessVo> getChildList(SupplierAccess bean, List<SupplierAccessVo> voList) {
        List<SupplierAccessVo> list = voList.stream()
                //筛选出父节点id == parentId 的所有对象 => list
                .filter(o -> StrUtil.equals(String.valueOf(bean.getAccessId()), String.valueOf(o.getParentId())))
                .peek(o -> o.setChildren(getChildList(o, voList)))
                .collect(Collectors.toList());
        if(list.size() == 0){
            return null;
        }
        return list;
    }
}
