package net.jjjshop.front.service.product.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.ProductCategoryCache;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserCart;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.mapper.product.ProductMapper;
import net.jjjshop.common.service.user.UserGradeService;
import net.jjjshop.common.settings.vo.RecommendVo;
import net.jjjshop.common.util.ProductUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.util.SupplierUtils;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.vo.product.ProductSkuVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.order.OrderBuyParam;
import net.jjjshop.front.param.product.ProductParam;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.front.service.supplier.SupplierCategoryService;
import net.jjjshop.front.service.supplier.SupplierService;
import net.jjjshop.front.service.user.UserCartService;
import net.jjjshop.front.vo.product.ProductBuyVo;
import net.jjjshop.front.vo.product.ProductCartVo;
import net.jjjshop.front.vo.product.ProductListVo;
import net.jjjshop.front.vo.product.ProductVo;
import net.jjjshop.front.vo.supplier.SupplierBuyVo;
import net.jjjshop.front.vo.supplier.SupplierVo;
import net.jjjshop.front.vo.supplier.SupplierVo;
import net.jjjshop.front.vo.supplier.ProductSupplierVo;
import net.jjjshop.front.vo.supplier.SupplierVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-01
 */
@Slf4j
@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductUtils productUtils;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private UserCartService userCartService;
    @Autowired
    private ProductCategoryCache productCategoryCache;
    @Autowired
    private UserGradeService userGradeService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierCategoryService supplierCategoryService;
    @Autowired
    private SupplierUtils supplierUtils;

    /**
     * 商品详情
     *
     * @param product
     * @param user
     * @return
     */
    public ProductVo getDetail(Product product, User user) {
        // 商品详情
        if (product == null || product.getIsDelete() == 1 || product.getProductStatus() != 10) {
            throw new BusinessException("很抱歉，商品信息不存在或已下架");
        }
        ProductVo vo = new ProductVo();
        BeanUtils.copyProperties(product, vo);
        // skuList
        vo.setSkuList(productUtils.getSkuByProductId(vo.getProductId()));
        // sku
        vo.setProductSku(this.getShowSku(vo));
        // image
        vo.setImage(productUtils.getListByProductId(vo.getProductId(), 0));
        // 实际销量
        vo.setProductSales(vo.getSalesActual() + vo.getSalesInitial());
        //商品视频
        vo.setVideoFilePath(vo.getVideoId()!=0 ? uploadFileUtils.getFilePath(vo.getVideoId()) : "");
        vo.setPosterFilePath(vo.getPosterId()!=0 ? uploadFileUtils.getFilePath(vo.getPosterId()) : "");
        // 商品内容
        if (vo.getIsPicture() == 1) {
            vo.setContent("");
            vo.setContentImage(productUtils.getListByProductId(vo.getProductId(), 1));
        }
        // 访问量
        this.update(new LambdaUpdateWrapper<Product>().eq(Product::getProductId, product.getProductId())
                .setSql("`view_times` = `view_times` + 1 "));
        // 计算并设置商品会员价
        this.setProductGradeMoney(vo, user);
        //设置店铺信息
        if(vo.getShopSupplierId() != null && vo.getShopSupplierId()>0) {
            Supplier supplier = supplierService.getById(vo.getShopSupplierId());
            ProductSupplierVo productSupplierVo = new ProductSupplierVo();
            BeanUtils.copyProperties(supplier, productSupplierVo);
            productSupplierVo.setLogoFilePath(uploadFileUtils.getFilePath(productSupplierVo.getLogoId()));
            productSupplierVo.setCategoryName(supplierCategoryService.getById(supplier.getCategoryId()).getName());
            vo.setSupplier(productSupplierVo);
            //设置店铺服务信息
            vo.setServer(supplierUtils.getServerList(vo.getShopSupplierId()));
        }
        return vo;
    }

    /**
     * 设置商品的会员价
     *
     * @param vo
     * @param user
     * @return
     */
    public void setProductGradeMoney(ProductVo vo, User user) {
        vo.setIsUserGrade(false);
        if (user == null || user.getGradeId() == 0) {
            return;
        }
        UserGrade grade = userGradeService.getById(user.getGradeId());
        if (grade == null || grade.getIsDelete() == 1) {
            return;
        }
        // 判断商品是否参与会员折扣
        if (vo.getIsEnableGrade() == 0) {
            return;
        }
        BigDecimal discountRatio = null;
        int aloneGradeType = 10;
        // 商品单独设置了会员折扣
        JSONObject aloneGradeEquity = JSON.parseObject(vo.getAloneGradeEquity());
        if (vo.getIsAloneGrade() == 1 &&
                StringUtils.isNotEmpty(aloneGradeEquity.getString("" + user.getGradeId()))) {
            if (vo.getAloneGradeType() == 10) {
                // 折扣比例
                discountRatio = BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId())).divide(new BigDecimal(100));
            } else {
                aloneGradeType = 20;
                discountRatio = BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId())).divide(vo.getProductPrice());
            }
        } else {
            // 折扣比例
            discountRatio = new BigDecimal(grade.getEquity()).divide(new BigDecimal(100),2, RoundingMode.DOWN);
        }
        if (discountRatio.compareTo(BigDecimal.ONE) < 0) {
            // 标记参与会员折扣
            vo.setIsUserGrade(true);
            // 会员折扣后的商品总金额
            if (aloneGradeType == 20) {
                vo.setProductPrice(aloneGradeEquity.getBigDecimal("" + user.getGradeId()));
                vo.setHighPrice(aloneGradeEquity.getBigDecimal("" + user.getGradeId()));
            } else {
                vo.setProductPrice(vo.getProductPrice().multiply(discountRatio));
                vo.setHighPrice(vo.getHighPrice().multiply(discountRatio));
            }

            // 会员折扣价
            for (ProductSkuVo skuVo : vo.getSkuList()) {
                if (aloneGradeType == 20) {
                    skuVo.setProductPrice(BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId())));
                } else {
                    skuVo.setProductPrice(skuVo.getProductPrice().multiply(discountRatio));
                }
            }
        } else {
            vo.setIsUserGrade(false);
        }
    }

    /**
     * 设置商品的会员价
     *
     * @param vo
     * @param user
     * @return
     */
    public void setProductListGradeMoney(ProductListVo vo, User user) {
        vo.setIsUserGrade(false);
        if (user == null || user.getGradeId() == 0) {
            return;
        }
        UserGrade grade = userGradeService.getById(user.getGradeId());
        if (grade == null || grade.getIsDelete() == 1) {
            return;
        }
        // 判断商品是否参与会员折扣
        if (vo.getIsEnableGrade() == 0) {
            return;
        }
        BigDecimal discountRatio = null;
        int aloneGradeType = 10;
        // 商品单独设置了会员折扣
        JSONObject aloneGradeEquity = JSON.parseObject(vo.getAloneGradeEquity());
        if (vo.getIsAloneGrade() == 1 &&
                StringUtils.isNotEmpty(aloneGradeEquity.getString("" + user.getGradeId()))) {
            if (vo.getAloneGradeType() == 10) {
                // 折扣比例
                discountRatio = BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId())).divide(new BigDecimal(100));
            } else {
                aloneGradeType = 20;
                discountRatio = BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId())).divide(vo.getProductPrice());
            }
        } else {
            // 折扣比例
            discountRatio = new BigDecimal(grade.getEquity()).divide(new BigDecimal(100),2, RoundingMode.DOWN);
        }
        if (discountRatio.compareTo(BigDecimal.ONE) < 0) {
            // 标记参与会员折扣
            vo.setIsUserGrade(true);
            // 会员折扣后的商品总金额
            if (aloneGradeType == 20) {
                vo.setProductPrice(aloneGradeEquity.getBigDecimal("" + user.getGradeId()));
            } else {
                vo.setProductPrice(vo.getProductPrice().multiply(discountRatio));
            }
        } else {
            vo.setIsUserGrade(false);
        }
    }

    /**
     * 获取显示sku
     *
     * @param vo
     * @return
     */
    private ProductSkuVo getShowSku(ProductVo vo) {
        if (vo.getSpecType() == 10) {
            return vo.getSkuList().get(0);
        } else {
            //多规格返回最低价
            for (ProductSkuVo sku : vo.getSkuList()) {
                if (vo.getProductPrice().compareTo(sku.getProductPrice()) == 0) {
                    return sku;
                }
            }
        }
        // 如果找不到返回第一个
        return vo.getSkuList().get(0);
    }

    /**
     * 购买商品信息
     *
     * @param masterOrderBuyParam
     * @return
     */
    public List<SupplierBuyVo> getOrderProductListByNow(OrderBuyParam masterOrderBuyParam) {
        Product product = this.getById(masterOrderBuyParam.getProductId());
        ProductBuyVo vo = new ProductBuyVo();
        BeanUtils.copyProperties(product, vo);
        vo.setProductImage(uploadFileUtils.getImagePathByProductId(vo.getProductId()));
        this.transBuyVo(vo, masterOrderBuyParam.getSpecSkuId(), masterOrderBuyParam.getProductNum());
        List<ProductBuyVo> productList = new ArrayList<>();
        productList.add(vo);
        // 转list兼容购物车
        List<SupplierBuyVo> list = new ArrayList<>();
        SupplierBuyVo supplierBuyVo = new SupplierBuyVo();
        supplierBuyVo.setProductList(productList);
        Supplier supplier = supplierService.getById(product.getShopSupplierId());
        SupplierVo supplierVo = new SupplierVo();
        BeanUtils.copyProperties(supplier, supplierVo);
        supplierBuyVo.setSupplier(supplierVo);
        supplierBuyVo.setShopSupplierId(product.getShopSupplierId());
        list.add(supplierBuyVo);
        return list;
    }

    /**
     * 商品查询
     *
     * @param productParam
     * @param user
     * @return
     */
    public Paging<ProductListVo> getList(ProductParam productParam, User user) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Product::getIsDelete, 0);
        // 名称搜索
        if (StringUtils.isNotEmpty(productParam.getSearch())) {
            wrapper.lambda().like(Product::getProductName, productParam.getSearch());
        }
        // 分类搜索
        if (productParam.getCategoryId() != null && productParam.getCategoryId() > 0) {
            List<Integer> subCategoryIds = productCategoryCache.getSubCategoryId(productParam.getCategoryId());
            wrapper.lambda().in(Product::getCategoryId, subCategoryIds);
        }
        // 排序
        if ("sales".equals(productParam.getSortType())) {
            wrapper.orderByDesc("sales_initial + sales_actual");
        } else if ("price".equals(productParam.getSortType())) {
            if (productParam.getSortPrice() == 0) {
                wrapper.lambda().orderByDesc(Product::getProductPrice);
            } else {
                wrapper.lambda().orderByAsc(Product::getProductPrice);
            }
        } else {
            wrapper.lambda().orderByAsc(Product::getProductSort).orderByDesc(Product::getCreateTime);
        }
        // 根据type设置查询参数
        if (StringUtils.isNotEmpty(productParam.getType())) {
            this.setWrapperByType(wrapper, productParam.getType());
        }
        //
        if (productParam.getShopSupplierId() != null && productParam.getShopSupplierId() > 0) {
            wrapper.lambda().eq(Product::getShopSupplierId, productParam.getShopSupplierId());
        }
        if (productParam.getProductId() != null && productParam.getProductId() > 0) {
            wrapper.lambda().notIn(Product::getProductId, productParam.getProductId());
        }
        Page<Product> page = new PageInfo<>(productParam);
        IPage<Product> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<ProductListVo> resultPage = iPage.convert(result -> {
            return this.transProductListData(result, user);
        });
        return new Paging(resultPage);
    }

    /**
     * 根据类型设置查询条件
     *
     * @param wrapper
     * @param type
     * @return
     */
    private void setWrapperByType(QueryWrapper<Product> wrapper, String type) {
        // 销售中
        if (type.equalsIgnoreCase("sell")) {
            wrapper.lambda().eq(Product::getProductStatus, 10)
                    .eq(Product::getAuditStatus, 10);
        }
        //仓库中
        if (type.equalsIgnoreCase("lower")) {
            wrapper.lambda().eq(Product::getProductStatus, 20)
                    .eq(Product::getAuditStatus, 10);
        }
        // 回收站
        if (type.equalsIgnoreCase("recovery")) {
            wrapper.lambda().eq(Product::getProductStatus, 30);
        }
        //库存紧张
        if (type.equalsIgnoreCase("stock")) {
            wrapper.lambda().ge(Product::getProductStock, 1)
                    .le(Product::getProductStock, 10)
                    .eq(Product::getProductStatus, 10)
                    .eq(Product::getAuditStatus, 10);
        }
        //待审核
        if (type.equalsIgnoreCase("audit")) {
            wrapper.lambda().eq(Product::getAuditStatus, 0);
        }
        //未通过
        if (type.equalsIgnoreCase("noAudit")) {
            wrapper.lambda().eq(Product::getAuditStatus, 20);
        }
        //草稿
        if (type.equalsIgnoreCase("draft")) {
            wrapper.lambda().eq(Product::getAuditStatus, 40);
        }
    }

    /**
     * 商品推荐
     *
     * @param location
     * @param user
     * @return
     */
    public Map<String, Object> getRecommend(Integer location, User user) {
        Map<String, Object> result = new HashMap<>();
        // 获取配置
        JSONObject vo = settingUtils.getSetting(SettingEnum.RECOMMEDND.getKey(), null);
        RecommendVo recommendVo = JSONObject.toJavaObject(vo, RecommendVo.class);
        Boolean isRecommend = this.checkRecommend(recommendVo, location);
        if (isRecommend) {
            // 推荐名称
            result.put("recommendName", recommendVo.getName());
            // 推荐商品
            result.put("list", this.getRecommendProduct(recommendVo, user));
        }
        result.put("isRecommend", isRecommend);
        return result;
    }

    /**
     * 是否展示商品推荐
     *
     * @param recommendVo
     * @param location
     * @return
     */
    private Boolean checkRecommend(RecommendVo recommendVo, Integer location) {
        Boolean isRecommend = true;
        if (recommendVo.getIsRecommend().intValue() > 0) {
            if (recommendVo.getLocation() == null) {
                return false;
            }
            if (!Arrays.asList(recommendVo.getLocation()).contains(location)) {
                isRecommend = false;
            }
        } else {
            isRecommend = false;
        }
        return isRecommend;
    }

    private List<ProductListVo> getRecommendProduct(RecommendVo recommendVo, User user) {
        List<Product> productList = null;
        // 手动选择
        if (recommendVo.getChoice().intValue() == 1) {
            List<Integer> productIds = new ArrayList<>();
            for (int i = 0; i < recommendVo.getProduct().length; i++) {
                productIds.add(recommendVo.getProduct()[i].getProductId());
            }
            productList = this.list(new LambdaQueryWrapper<Product>().in(Product::getProductId, productIds)
                    .eq(Product::getProductStatus, 10).eq(Product::getIsDelete, 0).eq(Product::getAuditStatus, 10));
        } else {
            LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Product::getProductStatus, 10).eq(Product::getIsDelete, 0).eq(Product::getAuditStatus, 10).last("LIMIT 0," + recommendVo.getNum());
            // 排序
            if (recommendVo.getType().intValue() == 10) {
                // 销量排序
                wrapper.orderByDesc(Product::getSalesActual);
            } else if (recommendVo.getType().intValue() == 20) {
                // 时间排序
                wrapper.orderByDesc(Product::getCreateTime);
            } else if (recommendVo.getType().intValue() == 30) {
                // 查看次数排序
                wrapper.orderByDesc(Product::getViewTimes);
            }
            productList = this.list(wrapper);
        }
        return productList.stream().map(e -> {
            return this.transProductListData(e, user);
        }).collect(Collectors.toList());
    }

    /**
     * 商品转成商品列表显示vo
     *
     * @param product
     * @param user
     * @return
     */
    private ProductListVo transProductListData(Product product, User user) {
        ProductListVo vo = new ProductListVo();
        BeanUtil.copyProperties(product, vo);
        Supplier supplier = supplierService.getById(vo.getShopSupplierId());
        if(supplier!=null) {
            JSONObject json = new JSONObject();
            json.put("address", supplier.getAddress());
            json.put("name", supplier.getName());
            json.put("shopSupplierId", supplier.getShopSupplierId());
            json.put("logoId", supplier.getLogoId());
            vo.setSupplier(json);
        }
        vo.setProductImage(uploadFileUtils.getImagePathByProductId(vo.getProductId()));
        vo.setProductSales(product.getSalesActual() + product.getSalesInitial());
        this.setProductListGradeMoney(vo, user);
        return vo;
    }

    /**
     * 通过商品id集合获取商品列表
     *
     * @param productIds
     * @param user
     * @return
     */
    public List<ProductListVo> getListByProductIds(List<Integer> productIds, User user) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (productIds != null && productIds.size() > 0) {
            wrapper.in(Product::getProductId, productIds);
        }
        wrapper.eq(Product::getProductStatus, 10)
                //审核状态0待审核10审核通过20审核未通过30强制下架40草稿
                .eq(Product::getAuditStatus, 10)
                .eq(Product::getIsDelete, 0);
        List<Product> productList = this.list(wrapper);
        return productList.stream().map(e -> {
            return this.transProductListData(e, user);
        }).collect(Collectors.toList());
    }

    /**
     * 通过商品分类id集合获取商品列表
     *
     * @param categoryIds
     * @param user
     * @return
     */
    public List<ProductListVo> getListByCategoryIds(List<Integer> categoryIds, User user) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        if (categoryIds != null && categoryIds.size() > 0) {
            wrapper.in(Product::getCategoryId, categoryIds);
        }
        wrapper.eq(Product::getProductStatus, 10).eq(Product::getIsDelete, 0);
        List<Product> productList = this.list(wrapper);
        return productList.stream().map(e -> {
            return this.transProductListData(e, user);
        }).collect(Collectors.toList());
    }


    /**
     * 购物车商品
     *
     * @param user
     * @param orderBuyParam
     * @return
     */
    public List<SupplierBuyVo> getOrderProductListByCart(User user, OrderBuyParam orderBuyParam) {
        String[] cartIds = orderBuyParam.getCartIds().split(",");
        LinkedMultiValueMap<Integer, String> productIds = new LinkedMultiValueMap<>();
        for (String cartId : cartIds) {
            int index = cartId.indexOf("_");
            Integer productId = Integer.valueOf(cartId.substring(0, index));
            String specSkuId = cartId.substring(index + 1);
            productIds.add(productId, specSkuId);
        }
        // 查找商品
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (productIds != null && productIds.size() > 0) {
            wrapper.in(Product::getProductId, new ArrayList<>(productIds.keySet()));
        }
        wrapper.eq(Product::getProductStatus, 10).eq(Product::getIsDelete, 0);
        List<Product> productList = this.list(wrapper);
        // 转换成购买vo
        List<ProductBuyVo> voList = new ArrayList<>();
        productList.forEach(item -> {
            List<String> ids = productIds.get(item.getProductId());
            for (String id : ids) {
                ProductBuyVo vo = new ProductBuyVo();
                BeanUtils.copyProperties(item, vo);
                vo.setProductImage(uploadFileUtils.getImagePathByProductId(vo.getProductId()));
                UserCart cart = userCartService.detail(user.getUserId(), item.getProductId(), id);
                this.transBuyVo(vo, id, cart.getTotalNum());
                voList.add(vo);
            }
        });
        // 按店铺分类
        Map<Integer, List<ProductBuyVo>> subListMap = voList.stream().collect(Collectors.groupingBy(ProductBuyVo::getShopSupplierId));
        List<SupplierBuyVo> list = new ArrayList<>();
        subListMap.forEach((key, value) -> {
            SupplierBuyVo item = new SupplierBuyVo();
            item.setProductList(value);
            Supplier supplier = supplierService.getById(key);
            SupplierVo vo = new SupplierVo();
            BeanUtils.copyProperties(supplier, vo);
            item.setShopSupplierId(key);
            item.setSupplier(vo);
            list.add(item);
        });
        return list;
    }

    /**
     * 转换成购买vo
     *
     * @param vo
     * @param specSkuId
     * @param productNum
     */
    private void transBuyVo(ProductBuyVo vo, String specSkuId, Integer productNum) {
        // sku
        vo.setProductSku(productUtils.getProductSku(vo.getProductId(), vo.getSpecType(), specSkuId));
        // 购买信息
        vo.setSpecSkuId(vo.getProductSku().getSpecSkuId());
        vo.setTotalNum(productNum);
        vo.setProductPrice(vo.getProductSku().getProductPrice());
        vo.setTotalPrice(vo.getProductPrice().multiply(BigDecimal.valueOf(vo.getTotalNum())));
    }

    /**
     * 商品上下架
     */
    public Boolean editStatus(Integer productId, Integer productStatus) {
        Product product = this.getById(productId);
        product.setProductStatus(productStatus);
        return this.updateById(product);
    }

}
