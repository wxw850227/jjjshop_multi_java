package net.jjjshop.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductComment;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierServiceApply;
import net.jjjshop.common.entity.supplier.SupplierServiceSecurity;
import net.jjjshop.common.service.product.ProductCommentService;
import net.jjjshop.common.service.supplier.SupplierService;
import net.jjjshop.common.service.supplier.SupplierServiceApplyService;
import net.jjjshop.common.service.supplier.SupplierServiceSecurityService;
import net.jjjshop.common.vo.supplier.SupplierSecurityVo;
import net.jjjshop.framework.core.pagination.Paging;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class SupplierUtils {

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierServiceApplyService supplierServiceApplyService;
    @Autowired
    private SupplierServiceSecurityService supplierServiceSecurityService;
    @Autowired
    private ProductCommentService productCommentService;


    /**
     * 获取供应商
     * @param id
     * @return
     */
    public Supplier getSupplier(Integer id) {
        return supplierService.getById(id);
    }

    /**
     * 通过userId验证供应商是否存在
     * @param userId
     * @return
     */
    public Boolean checkExistByUserId(Integer userId) {
        Integer count = supplierService.count(new LambdaQueryWrapper<Supplier>().eq(Supplier::getUserId, userId));
        if (count > 0) {
            return false;
        }
        return true;
    }

    /**
     * 验证用户
     * @param supplier
     * @return
     */
    public Boolean update(Supplier supplier) {
        return supplierService.updateById(supplier);
    }

    /**
     * 获取供应商服务列表数据
     * @param shopSupplierId
     * @return
     */
    public List<SupplierSecurityVo> getServerList(Integer shopSupplierId) {
        LambdaQueryWrapper<SupplierServiceApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SupplierServiceApply::getStatus, 1);
        wrapper.eq(SupplierServiceApply::getShopSupplierId, shopSupplierId);
        wrapper.orderByAsc(SupplierServiceApply::getCreateTime);
        List<SupplierServiceApply> list = supplierServiceApplyService.list(wrapper);
        List<SupplierSecurityVo> result = list.stream().map(e -> {
            SupplierSecurityVo vo = new SupplierSecurityVo();
            SupplierServiceSecurity security = supplierServiceSecurityService.getById(e.getServiceSecurityId());
            vo.setName(security.getName());
            vo.setDescription(security.getDescription());
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 增加门店商品销量
     * @param shopSupplierId
     * @return
     */
    public Boolean incProductSales(Integer shopSupplierId){
        Supplier supplier = supplierService.getById(shopSupplierId);
        supplier.setProductSales(supplier.getProductSales()+1);
        return supplierService.updateById(supplier);
    }

    /**
     * 更新店铺评分
     * @param shopSupplierId
     * @return
     */
    public void updateScore(Integer shopSupplierId) {
        Supplier supplier = supplierService.getById(shopSupplierId);
        QueryWrapper<ProductComment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ProductComment::getStatus, 1);
        wrapper.lambda().eq(ProductComment::getIsDelete, 0);
        List<ProductComment> list = productCommentService.list(wrapper);
        if(CollectionUtils.isNotEmpty(list)) {
            Integer express = 0;
            Integer server = 0;
            Integer describe = 0;
            for (ProductComment comment : list) {
                express += comment.getExpressScore();
                server += comment.getServerScore();
                describe += comment.getDescribeScore();
            }
            supplier.setExpressScore(BigDecimal.valueOf((express * 1.0) / list.size()).setScale(1, RoundingMode.DOWN));
            supplier.setServerScore(BigDecimal.valueOf((server * 1.0) / list.size()).setScale(1, RoundingMode.DOWN));
            supplier.setDescribeScore(BigDecimal.valueOf((describe * 1.0) / list.size()).setScale(1, RoundingMode.DOWN));
        }
        supplierService.updateById(supplier);
    }

    /**
     * 资金冻结
     * @param shopSupplierId
     * @param money
     * @return
     */
    public Boolean freezeMoney(Integer shopSupplierId, BigDecimal money) {
        Supplier supplier = supplierService.getById(shopSupplierId);
        supplier.setMoney(supplier.getMoney().subtract(money));
        supplier.setFreezeMoney(supplier.getFreezeMoney().add(money));
        return supplierService.updateById(supplier);
    }

}
