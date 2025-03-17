package net.jjjshop.front.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.page.Ad;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierCategory;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserFavorite;
import net.jjjshop.common.mapper.supplier.SupplierMapper;
import net.jjjshop.common.service.page.AdService;
import net.jjjshop.common.service.supplier.SupplierCategoryService;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.supplier.SupplierPageParam;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.front.service.supplier.SupplierService;
import net.jjjshop.front.service.user.UserFavoriteService;
import net.jjjshop.front.vo.page.AdVo;
import net.jjjshop.front.vo.product.ProductListVo;
import net.jjjshop.front.vo.supplier.SupplierDetailVo;
import net.jjjshop.front.vo.supplier.SupplierListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 供应商表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
@Slf4j
@Service
public class SupplierServiceImpl extends BaseServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierCategoryService supplierCategoryService;
    @Autowired
    private SupplierUserService supplierUserService;
    @Autowired
    private UserFavoriteService userFavoriteService;
    @Autowired
    private AdService adService;

    /**
     * 供应商分页列表
     * @param supplierPageParam
     * @return
     */
    public Paging<SupplierListVo> supplierList(SupplierPageParam supplierPageParam) {
        Page<Supplier> page = new PageInfo<>(supplierPageParam);
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        //排序规则
        if ("all".equals(supplierPageParam.getSortType())) {
            wrapper.orderByDesc(Supplier::getCreateTime);
        } else if ("sales".equals(supplierPageParam.getSortType())) {
            wrapper.orderByDesc(Supplier::getProductSales);
        } else if ("sortType".equals(supplierPageParam.getSortType())) {
            wrapper.orderByDesc(Supplier::getServerScore);
        }

        if (StringUtils.isNotEmpty(supplierPageParam.getName())) {
            wrapper.like(Supplier::getName, supplierPageParam.getName());
        }
        wrapper.eq(Supplier::getIsDelete, 0);
        wrapper.eq(Supplier::getIsRecycle, 0);
        //wrapper.eq(Supplier::getIsFull, 1);

        //查询列表数据
        IPage<Supplier> iPage = this.page(page, wrapper);
        IPage<SupplierListVo> result = iPage.convert(item -> {
            SupplierListVo vo = new SupplierListVo();
            BeanUtils.copyProperties(item, vo);
            vo.setLogoFilePath(uploadFileUtils.getFilePath(vo.getLogoId()));
            //查询分类名称
            SupplierCategory category = supplierCategoryService.getById(vo.getCategoryId());
            vo.setCategoryName(category.getName());
            //查询商品数据
            List<Product> list = productService.list(new LambdaQueryWrapper<Product>()
                    .eq(Product::getShopSupplierId, item.getShopSupplierId())
                    .eq(Product::getProductStatus, 10)
                    .eq(Product::getAuditStatus, 10)
                    .eq(Product::getIsDelete, 0)
                    .orderByDesc(Product::getProductId)
                    .orderByAsc(Product::getProductSort)
                    .last("LIMIT 0,3"));
            List<ProductListVo> productList = list.stream().map(e -> {
                ProductListVo productListVo = new ProductListVo();
                BeanUtils.copyProperties(e, productListVo);
                productListVo.setProductImage(uploadFileUtils.getImagePathByProductId(productListVo.getProductId()));
                return productListVo;
            }).collect(Collectors.toList());
            vo.setProductList(productList);
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 获取店铺信息
     * @param shopSupplierId
     * @param user
     * @return
     */
    public SupplierDetailVo getDetail(Integer shopSupplierId, User user) {
        Supplier supplier = this.getById(shopSupplierId);
        SupplierDetailVo vo = new SupplierDetailVo();
        if (supplier != null) {
            BeanUtils.copyProperties(supplier, vo);
            vo.setLogoFilePath(uploadFileUtils.getFilePath(vo.getLogoId()));
            //查询分类名称
            SupplierCategory category = supplierCategoryService.getById(vo.getCategoryId());
            vo.setCategoryName(category.getName());
            vo.setIsfollow(0);
            SupplierUser supplierUser = supplierUserService.getOne(new LambdaQueryWrapper<SupplierUser>().eq(SupplierUser::getShopSupplierId, vo.getShopSupplierId()).last("LIMIT 1"));
            vo.setSupplierUserId(supplierUser.getSupplierUserId());
            if (user != null) {
                int count = userFavoriteService.count(new LambdaQueryWrapper<UserFavorite>()
                        .eq(UserFavorite::getPid, shopSupplierId)
                        .eq(UserFavorite::getUserId, user.getUserId())
                        .eq(UserFavorite::getType, 10));
                vo.setIsfollow(count);
            }
        }
        return vo;
    }

    /**
     * 获取广告图
     * @param shopSupplierId
     * @return
     */
    public List<AdVo> getAdList(Integer shopSupplierId) {
        List<Ad> list = adService.list(new LambdaQueryWrapper<Ad>().eq(Ad::getShopSupplierId, shopSupplierId).orderByAsc(Ad::getSort).last("LIMIT 0,5"));
        return list.stream().map(e -> {
            AdVo vo = new AdVo();
            BeanUtils.copyProperties(e, vo);
            vo.setFilePath(uploadFileUtils.getFilePath(e.getImageId()));
            return vo;
        }).collect(Collectors.toList());
    }


}
