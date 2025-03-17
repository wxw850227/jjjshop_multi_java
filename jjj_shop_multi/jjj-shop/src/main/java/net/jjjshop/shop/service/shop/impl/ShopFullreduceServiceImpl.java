package net.jjjshop.shop.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopFullreduce;
import net.jjjshop.common.mapper.shop.ShopFullreduceMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.shop.FullReducePageParam;
import net.jjjshop.shop.param.shop.FullReduceParam;
import net.jjjshop.shop.service.shop.ShopFullreduceService;
import net.jjjshop.shop.vo.shop.FullReduceVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
     * 获取所有满减活动
     * @param fullReducePageParam
     * @return
     */
    public Paging<FullReduceVo> getList(FullReducePageParam fullReducePageParam) {
        Page<ShopFullreduce> page = new PageInfo(fullReducePageParam);
        LambdaQueryWrapper<ShopFullreduce> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopFullreduce::getIsDelete, 0);
        wrapper.orderByAsc(ShopFullreduce::getCreateTime);
        IPage<ShopFullreduce> iPage = this.page(page, wrapper);
        IPage<FullReduceVo> result = iPage.convert(item -> {
            FullReduceVo vo = new FullReduceVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 获取所有满减活动
     * @param
     * @return
     */
    public List<FullReduceVo> getAll(){
        LambdaQueryWrapper<ShopFullreduce> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopFullreduce::getIsDelete, 0);
        wrapper.eq(ShopFullreduce::getProductId, 0);
        wrapper.orderByAsc(ShopFullreduce::getCreateTime);
        List<FullReduceVo> result = this.list(wrapper).stream().map(e -> {
            FullReduceVo vo = new FullReduceVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 添加满减活动
     * @param fullReduceParam
     * @return
     */
    public Boolean add(FullReduceParam fullReduceParam){
        ShopFullreduce shopFullreduce = new ShopFullreduce();
        BeanUtils.copyProperties(fullReduceParam, shopFullreduce);
        return this.save(shopFullreduce);
    }

    /**
     * 编辑满减活动
     * @param fullReduceParam
     * @return
     */
    public Boolean edit(FullReduceParam fullReduceParam){
        ShopFullreduce shopFullreduce = this.getById(fullReduceParam.getFullreduceId());
        BeanUtils.copyProperties(fullReduceParam, shopFullreduce);
        return this.updateById(shopFullreduce);
    }

    /**
     * 软删除满减活动
     * @param fullReduceId
     * @return
     */
    public Boolean setDelete(Integer fullReduceId){
        return this.update(new LambdaUpdateWrapper<ShopFullreduce>().eq(ShopFullreduce::getFullreduceId, fullReduceId).set(ShopFullreduce::getIsDelete, 1));
    }

    /**
     * 满减活动详情
     * @param fullReduceId
     * @return
     */
    public FullReduceVo detail(Integer fullReduceId){
        ShopFullreduce fullreduce = this.getById(fullReduceId);
        FullReduceVo vo = new FullReduceVo();
        BeanUtils.copyProperties(fullreduce, vo);
        return vo;
    }

    /**
     * 获取商品参加的满减活动
     * @param productId
     * @return
     */
    public List<FullReduceVo> getProductAll(Integer productId){
        LambdaQueryWrapper<ShopFullreduce> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopFullreduce::getIsDelete, 0);
        wrapper.eq(ShopFullreduce::getProductId, productId);
        wrapper.orderByAsc(ShopFullreduce::getCreateTime);
        List<FullReduceVo> result = this.list(wrapper).stream().map(e -> {
            FullReduceVo vo = new FullReduceVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

}
