package net.jjjshop.front.service.user.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderRefund;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierCategory;
import net.jjjshop.common.entity.user.UserFavorite;
import net.jjjshop.common.mapper.user.UserFavoriteMapper;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.user.UserFavoritePageParam;
import net.jjjshop.front.param.user.UserFavoriteParam;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.front.service.supplier.SupplierCategoryService;
import net.jjjshop.front.service.supplier.SupplierService;
import net.jjjshop.front.service.user.UserFavoriteService;
import net.jjjshop.front.vo.product.ProductListVo;
import net.jjjshop.front.vo.supplier.SupplierListVo;
import net.jjjshop.front.vo.user.UserFavoriteVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 我的收藏 服务实现类
 *
 * @author jjjshop
 * @since 2022-08-02
 */

@Slf4j
@Service
public class UserFavoriteServiceImpl extends BaseServiceImpl<UserFavoriteMapper, UserFavorite> implements UserFavoriteService {

    @Autowired
    private ProductService productService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierCategoryService supplierCategoryService;


    /**
     * 收藏列表
     *
     * @param userFavoritePageParam
     * @return
     */
    public Paging<UserFavoriteVo> getProductList(UserFavoritePageParam userFavoritePageParam) {
        Integer userId = userFavoritePageParam.getUserId();
        Page<UserFavorite> page = new PageInfo<>(userFavoritePageParam);
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId);
        wrapper.eq(UserFavorite::getType, userFavoritePageParam.getType());
        wrapper.orderByDesc(UserFavorite::getCreateTime);
        IPage<UserFavorite> userFavorite = this.page(page, wrapper);
        IPage<UserFavoriteVo> result = userFavorite.convert(item -> {
            UserFavoriteVo vo = new UserFavoriteVo();
            BeanUtils.copyProperties(item, vo);
            Product product = productService.getById(vo.getPid());
            vo.setProductImage(uploadFileUtils.getImagePathByProductId(vo.getPid()));
            vo.setProductId(product.getProductId());
            vo.setProductName(product.getProductName());
            vo.setProductPrice(product.getProductPrice());
            vo.setLinePrice(product.getLinePrice());
            vo.setProductSales(product.getSalesActual());
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 添加收藏
     *
     * @param productId
     * @param userId
     * @return
     */
    public Boolean add(Integer productId, Integer userId) {
        UserFavorite favorite = new UserFavorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        return this.save(favorite);
    }

    /**
     * 取消收藏
     *
     * @param productId
     * @param userId
     * @return
     */
    public Boolean delById(Integer productId, Integer userId) {
        return this.remove(new LambdaQueryWrapper<UserFavorite>().eq(UserFavorite::getUserId, userId).eq(UserFavorite::getProductId, productId));
    }

    /**
     * 用户是否收藏
     *
     * @param productId
     * @param userId
     * @return
     */
    public Boolean isFav(Integer productId, Integer userId) {
        int count = this.count(new LambdaQueryWrapper<UserFavorite>().eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getProductId, productId));
        return count > 0;
    }

    /**
     * 取消收藏
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelFav(Integer id, Integer pid, Integer type) {
        if (type == 10) {
            Supplier supplier = supplierService.getById(pid);
            supplier.setFavCount(supplier.getFavCount() - 1);
            supplierService.updateById(supplier);
        }
        return this.removeById(id);
    }

    /**
     * 收藏
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean fav(UserFavoriteParam userFavoriteParam) {
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setUserId(userFavoriteParam.getUserId());
        userFavorite.setPid(userFavoriteParam.getPid());
        userFavorite.setType(userFavoriteParam.getType());
        if (userFavoriteParam.getType() == 10) {
            Supplier supplier = supplierService.getById(userFavoriteParam.getPid());
            supplier.setFavCount(supplier.getFavCount() + 1);
            supplierService.updateById(supplier);
            userFavorite.setShopSupplierId(supplier.getShopSupplierId());
            userFavorite.setProductId(0);
        } else {
            Product product = productService.getById(userFavoriteParam.getPid());
            userFavorite.setProductId(product.getProductId());
            userFavorite.setShopSupplierId(product.getShopSupplierId());
        }
        return this.save(userFavorite);
    }

    /**
     * 判断是否已收藏
     */
    public Integer isFollow(Integer pid, Integer userId, Integer type) {
        UserFavorite one = this.getOne(new LambdaQueryWrapper<UserFavorite>().eq(UserFavorite::getPid, pid).eq(UserFavorite::getUserId, userId).eq(UserFavorite::getType, type));
        if (one != null) {
            return one.getFavoriteId();
        } else {
            return null;
        }
    }

    /**
     * 获取关注店铺列表
     */
    public Paging<SupplierListVo> getMySupplier(UserFavoritePageParam param) {
        Page<UserFavorite> page = new PageInfo<>(param);
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, param.getUserId());
        wrapper.eq(UserFavorite::getType, param.getType());
        wrapper.orderByDesc(UserFavorite::getCreateTime);
        IPage<UserFavorite> iPage = this.page(page, wrapper);
        IPage<SupplierListVo> result = iPage.convert(item -> {
            SupplierListVo vo = new SupplierListVo();
            Supplier supplier = supplierService.getById(item.getPid());
            BeanUtils.copyProperties(supplier, vo);
            vo.setLogoFilePath(uploadFileUtils.getFilePath(vo.getLogoId()));
            //查询分类名称
            SupplierCategory category = supplierCategoryService.getById(vo.getCategoryId());
            vo.setCategoryName(category.getName());
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


}
