package net.jjjshop.common.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.mapper.order.OrderProductMapper;
import net.jjjshop.common.mapper.statistics.ProductRankingMapper;
import net.jjjshop.common.service.order.OrderProductService;
import net.jjjshop.common.service.product.ProductService;
import net.jjjshop.common.vo.order.OrderProductVo;
import net.jjjshop.common.vo.product.ProductVo;
import net.jjjshop.common.vo.statistics.ProductRefundRankingVo;
import net.jjjshop.common.vo.statistics.ProductSaleRankingVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductDataUtils {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private ProductRankingMapper productRankingMapper;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private OrderProductService orderProductService;

    /**
     * 获取商品统计数据
     * @param
     * @return
     */
    //获取商品统计数据
    public JSONObject getProductData(Integer shopSupplierId) throws ParseException {
        //获取今天时间
        String today = DateUtil.format(DateUtil.offsetDay(new Date(), 0), "yyyy-MM-dd");
        //获取昨天时间
        String yesterday = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");

        Integer saleToday = productService.count(new LambdaQueryWrapper<Product>().eq(Product::getIsDelete, 0).eq(Product::getProductStatus, 10));
        String saleYesterday = "--";
        Integer noPayToday = this.getOrderProductData(today, null, "no_pay",shopSupplierId);
        Integer noPayYesterday = this.getOrderProductData(yesterday, null, "no_pay",shopSupplierId);
        Integer payToday = this.getOrderProductData(today, null, "pay",shopSupplierId);
        Integer payYesterday = this.getOrderProductData(yesterday, null, "pay",shopSupplierId);

        JSONObject json = new JSONObject();
        json.put("saleToday", saleToday);
        json.put("saleYesterday", saleYesterday);
        json.put("noPayToday", noPayToday);
        json.put("noPayYesterday", noPayYesterday);
        json.put("payToday", payToday);
        json.put("payYesterday", payYesterday);
        return json;
    }


    /**
     * 按照时间范围获取商品订单数据
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    public int getOrderProductData(String startDate, String endDate, String type,Integer shopSupplierId) {
        LambdaQueryWrapper<OrderProduct> wrapper = new LambdaQueryWrapper<>();
        Date startTime = null;
        Date endTime = null;
        if (StringUtils.isNotEmpty(startDate)) {
            startTime = DateUtil.parse(startDate + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(endDate)) {
            endTime = DateUtil.parse(endDate + " 23:59:59");
        } else if (StringUtils.isNotEmpty(startDate)) {
            endTime = DateUtil.parse(startDate + " 23:59:59");
        }
        int t = 0;
        if ("no_pay".equals(type)) {
            t = 10;
        } else if ("pay".equals(type)) {
            t = 20;
        }
        List<OrderProductVo> productData = orderProductMapper.getProductData(startTime, endTime, t,shopSupplierId);
        return productData.size();
    }



    /**
     * 获取商品销售榜单
     * @param
     * @return
     */
    public List<ProductSaleRankingVo> getSaleRanking(Integer shopSupplierId) {
        List<ProductSaleRankingVo> result = productRankingMapper.getSaleRanking(shopSupplierId);
        result.stream().forEach(e -> {
            e.setImagePath(uploadFileUtils.getImagePathByProductId(e.getProductId()));
            e.setViewTimes(productService.getById(e.getProductId()).getViewTimes());
        });
        return result;
    }

    /**
     * 获取商品浏览榜单
     * @param
     * @return
     */
    public List<ProductVo> getViewsRanking(Integer shopSupplierId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.gt(Product::getViewTimes, 0);
        wrapper.orderByDesc(Product::getViewTimes);
        if(shopSupplierId != null && shopSupplierId>0) {
            wrapper.eq(Product::getShopSupplierId, shopSupplierId);
        }
        wrapper.last("LIMIT 0,10");
        List<Product> list = productService.list(wrapper);
        List<ProductVo> result = list.stream().map(e -> {
            ProductVo vo = new ProductVo();
            BeanUtils.copyProperties(e, vo);
            vo.setImagePath(uploadFileUtils.getImagePathByProductId(vo.getProductId()));
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 获取商品退款榜单
     * @param
     * @return
     */
    public List<ProductRefundRankingVo> getRefundRanking(Integer shopSupplierId) {
        List<ProductRefundRankingVo> result = productRankingMapper.getRefundRanking(shopSupplierId);
        result.stream().forEach(e -> {
            e.setImagePath(uploadFileUtils.getImagePathByProductId(e.getProductId()));
            //名称已经有了
            //e.setProductName(productService.getById(e.getProductId()).getProductName());
        });
        return result;
    }

    /**
     * 获取商品总数
     * @param
     * @return
     */
    public Integer getProductTotal(Integer shopSupplierId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getIsDelete, 0);
        if(shopSupplierId != null && shopSupplierId>0) {
            wrapper.eq(Product::getShopSupplierId, shopSupplierId);
        }
        return productService.count(wrapper);
    }

    /**
     * 获取商品库存总数
     * @param
     * @return
     */
    public Integer getProductStockTotal(Integer shopSupplierId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getIsDelete, 0);
        wrapper.lt(Product::getProductStock, 20);
        if(shopSupplierId != null && shopSupplierId>0) {
            wrapper.eq(Product::getShopSupplierId, shopSupplierId);
        }
        return productService.count(wrapper);
    }

    /**
     * 通过时间范围查询商品统计数据
     * @param startDate,endDate
     * @return
     */
    public Map<String, Object> getProductDataByDate(String startDate, String endDate, Integer shopSupplierId) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Date startTime = DateUtil.parse(startDate);
        Date endTime = DateUtil.parse(endDate);
        //endTime加一天
        endTime = DateUtil.offsetDay(endTime,1);
        List<JSONObject> data = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (Date t = startTime; t.before(endTime); t = DateUtil.offsetDay(t,1)) {
            String day = DateUtil.format(t, "yyyy-MM-dd");
            Integer totalNum = this.getOrderProductData(day, null, "pay", shopSupplierId);
            JSONObject json = new JSONObject();
            json.put("day", day);
            json.put("totalNum", totalNum);
            days.add(day);
            data.add(json);
        }
        map.put("data", data);
        map.put("days", days);
        return map;
    }
}
