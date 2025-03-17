package net.jjjshop.supplier.service.ad.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.page.Ad;
import net.jjjshop.common.mapper.page.AdMapper;
import net.jjjshop.common.service.page.AdCategoryService;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.ad.AdPageParam;
import net.jjjshop.supplier.param.ad.AdParam;
import net.jjjshop.supplier.service.ad.AdService;
import net.jjjshop.supplier.vo.ad.AdVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * banner图 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-25
 */
@Slf4j
@Service
public class AdServiceImpl extends BaseServiceImpl<AdMapper, Ad> implements AdService {
    @Autowired
    private AdCategoryService adCategoryService;
    @Autowired
    private UploadFileUtils uploadFileUtils;

    /**
     * 获取广告分页列表
     * @param adPageParam
     * @return
     */
    public Paging<AdVo> getList(AdPageParam adPageParam) {
        Page<Ad> page = new PageInfo<>(adPageParam);
        LambdaQueryWrapper<Ad> wrapper = new LambdaQueryWrapper<>();
        if (adPageParam.getShopSupplierId() != null && adPageParam.getShopSupplierId() > 0) {
            wrapper.eq(Ad::getShopSupplierId, adPageParam.getShopSupplierId());
        }
        IPage<Ad> iPage = this.page(page, wrapper);
        IPage<AdVo> result = iPage.convert(item -> {
            AdVo vo = new AdVo();
            BeanUtils.copyProperties(item, vo);
            if(vo.getCategoryId()!=null && vo.getCategoryId()>0) {
                vo.setCategoryName(adCategoryService.getById(vo.getCategoryId()).getName());
            }
            vo.setFilePath(uploadFileUtils.getFilePath(vo.getImageId()));
            return vo;
        });
        return new Paging<>(result);
    }

    /**
     * 获取广告详情
     * @param adId
     * @return
     */
    public AdVo detail(Integer adId) {
        AdVo vo = new AdVo();
        Ad ad = this.getById(adId);
        BeanUtils.copyProperties(ad, vo);
        if(vo.getCategoryId()!=null && vo.getCategoryId()>0) {
            vo.setCategoryName(adCategoryService.getById(vo.getCategoryId()).getName());
        }
        vo.setFilePath(uploadFileUtils.getFilePath(vo.getImageId()));
        return vo;
    }

    /**
     * 添加新记录
     * @param adParam
     * @return
     */
    public Boolean add(AdParam adParam) {
        Ad ad = new Ad();
        BeanUtils.copyProperties(adParam, ad);
        return this.save(ad);
    }

    /**
     * 编辑
     * @param adParam
     * @return
     */
    public Boolean edit(AdParam adParam) {
        Ad ad = this.getById(adParam.getAdId());
        BeanUtils.copyProperties(adParam, ad);
        return this.updateById(ad);
    }

    /**
     * 删除记录
     * @param adId
     * @return
     */
    public Boolean delById(Integer adId) {
        return this.removeById(adId);
    }

}
