package net.jjjshop.front.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopFullreduce;
import net.jjjshop.common.mapper.shop.ShopFullreduceMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.shop.ShopFullreduceService;
import net.jjjshop.front.vo.shop.ShopFullreduceVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 满减设置表 服务实现类
 *
 * @author jjjshop
 * @since 2022-08-22
 */
@Slf4j
@Service
public class ShopFullreduceServiceImpl extends BaseServiceImpl<ShopFullreduceMapper, ShopFullreduce> implements ShopFullreduceService {
    /**
     * 获取满减
     * @param totalPrice
     * @param totalNum
     * @return
     */
    public ShopFullreduceVo getReductList(BigDecimal totalPrice, Integer totalNum, Integer productId, Integer shopSupplierId){
        // 获取所有满减活动
        List<ShopFullreduce>  list =  new ArrayList<>();
        if(productId > 0){
            // 商品满减
            list = this.list(new LambdaQueryWrapper<ShopFullreduce>()
                    .eq(ShopFullreduce::getProductId, productId).eq(ShopFullreduce::getIsDelete, 0)
                    .orderByAsc(ShopFullreduce::getCreateTime));
        }else if(shopSupplierId > 0){
            // 店铺满减
            list = this.list(new LambdaQueryWrapper<ShopFullreduce>()
                    .eq(ShopFullreduce::getShopSupplierId, shopSupplierId).eq(ShopFullreduce::getIsDelete, 0)
                    .orderByAsc(ShopFullreduce::getCreateTime));
        }

        List<ShopFullreduceVo> data = new ArrayList<>();
        for (ShopFullreduce reduce:list) {
            // 满额
            if (reduce.getFullType() == 1) {
                if (totalPrice.compareTo(BigDecimal.valueOf(reduce.getFullValue())) < 0) {
                    continue;
                }
            } else {
                // 满件数
                if (totalNum < reduce.getFullValue()) {
                    continue;
                }
            }
            ShopFullreduceVo vo = new ShopFullreduceVo();
            BeanUtils.copyProperties(reduce, vo);
            // 计算减的金额
            if (reduce.getReduceType() == 1) {
                //满金额
                vo.setReducedPrice(BigDecimal.valueOf(reduce.getReduceValue()));
            } else{
                // 折扣比例
                vo.setReducedPrice(BigDecimal.valueOf(reduce.getReduceValue() * 0.01).multiply(totalPrice));
            }
            data.add(vo);
        }
        if(data.size() == 0){
            return null;
        }else{
            // 根据折扣金额排序并返回第一个
            List<ShopFullreduceVo> temp = data.stream().sorted(Comparator.comparing(ShopFullreduceVo::getReducedPrice)
                    .reversed()).collect(Collectors.toList());
            return temp.get(0);
        }
    }
}
