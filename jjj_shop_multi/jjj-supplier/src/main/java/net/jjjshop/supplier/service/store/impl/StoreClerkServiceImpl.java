package net.jjjshop.supplier.service.store.impl;

import com.alipay.api.domain.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.store.StoreClerk;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.mapper.store.StoreClerkMapper;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.store.StoreClerkPageParam;
import net.jjjshop.supplier.param.store.StoreClerkParam;
import net.jjjshop.supplier.service.store.StoreClerkService;
import net.jjjshop.supplier.service.store.StoreService;
import net.jjjshop.supplier.vo.store.StoreClerkVo;
import net.jjjshop.supplier.vo.store.StoreVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商家门店店员表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-27
 */
@Slf4j
@Service
public class StoreClerkServiceImpl extends BaseServiceImpl<StoreClerkMapper, StoreClerk> implements StoreClerkService {

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    /**
     * 商家门店店员分页列表
     * @param storeClerkPageParam
     * @return
     */
    public Paging<StoreClerkVo> getList(StoreClerkPageParam storeClerkPageParam) {
        Page<StoreClerk> page = new PageInfo(storeClerkPageParam);
        LambdaQueryWrapper<StoreClerk> wrapper = new LambdaQueryWrapper<StoreClerk>().eq(StoreClerk::getIsDelete, 0).orderByDesc(StoreClerk::getCreateTime);
        //如果搜索框内容不为空，则对搜索框内容进行查询
        if (StringUtils.isNotEmpty(storeClerkPageParam.getSearch())) {
            wrapper.and(i -> i.or().like(StoreClerk::getRealName, storeClerkPageParam.getSearch())
                    .or().like(StoreClerk::getMobile, storeClerkPageParam.getSearch()));
        }
        if (storeClerkPageParam.getStoreId() != null) {
            wrapper.eq(StoreClerk::getStoreId, storeClerkPageParam.getStoreId());
        }
        wrapper.eq(StoreClerk::getShopSupplierId, storeClerkPageParam.getShopSupplierId());
        IPage<StoreClerk> iPage = this.page(page, wrapper);
        IPage<StoreClerkVo> result = iPage.convert(item -> {
            StoreClerkVo vo = new StoreClerkVo();
            BeanUtils.copyProperties(item, vo);
            //显示店员是否启用
            if (vo.getStatus() == 1) {
                vo.setStatusText("启用");
            } else {
                vo.setStatusText("禁用");
            }
            //如果店员是商城用户则设置店员的昵称和微信头像
            User user = userService.getById(vo.getUserId());
            if (user != null) {
                vo.setNickName(user.getNickname());
                vo.setAvatarUrl(user.getAvatarurl());
            }
            vo.setStoreName(storeService.getById(vo.getStoreId()).getStoreName());
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 获取所有商家门店店员列表
     * @param
     * @return
     */
    public List<StoreClerkVo> getAll() {
        List<StoreClerk> list = this.list(new LambdaQueryWrapper<StoreClerk>()
                .eq(StoreClerk::getIsDelete, 0)
                .orderByDesc(StoreClerk::getCreateTime));
        List<StoreClerkVo> result = list.stream().map(e -> {
            StoreClerkVo vo = new StoreClerkVo();
            BeanUtils.copyProperties(e, vo);
            User user = userService.getById(vo.getUserId());
            vo.setNickName(user.getNickname());
            vo.setAvatarUrl(user.getAvatarurl());
            vo.setStoreName(storeService.getById(vo.getStoreId()).getStoreName());
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 通过商家门店Id获取店员列表
     * @param storeId
     * @return
     */
    public List<StoreClerkVo> getClerkByStoreId(Integer storeId) {
        List<StoreClerk> list = this.list(new LambdaQueryWrapper<StoreClerk>()
                .eq(StoreClerk::getIsDelete, 0)
                .eq(StoreClerk::getStoreId, storeId)
                .orderByDesc(StoreClerk::getCreateTime));
        List<StoreClerkVo> result = list.stream().map(e -> {
            StoreClerkVo vo = new StoreClerkVo();
            BeanUtils.copyProperties(e, vo);
            User user = userService.getById(vo.getUserId());
            if(user != null) {
                vo.setNickName(user.getNickname());
                vo.setAvatarUrl(user.getAvatarurl());
                vo.setStoreName(storeService.getById(vo.getStoreId()).getStoreName());
            }
            return vo;
        }).collect(Collectors.toList());
        return result;
    }


    /**
     * 添加商家店员
     * @param storeClerkParam
     * @return
     */
    public Boolean add(Integer shopSupplierId, StoreClerkParam storeClerkParam) {
        if(storeClerkParam.getUserId()==null) {
            throw new BusinessException("请选择用户");
        }
        StoreClerk storeClerk = new StoreClerk();
        int count = this.count(new LambdaQueryWrapper<StoreClerk>()
                .eq(StoreClerk::getUserId, storeClerkParam.getUserId())
                .eq(StoreClerk::getClerkId, storeClerkParam.getClerkId())
                .eq(StoreClerk::getStoreId, storeClerkParam.getStoreId())
                .eq(StoreClerk::getShopSupplierId, storeClerk.getShopSupplierId())
                .eq(StoreClerk::getIsDelete, 0));
        if (count > 0) {
            throw new BusinessException("该用户已经是门店店员了！");
        }
        BeanUtils.copyProperties(storeClerkParam, storeClerk);
        storeClerk.setShopSupplierId(shopSupplierId);
        return this.save(storeClerk);
    }

    /**
     * 获取编辑商家店员需要的数据
     * @param clerkId
     * @return
     */
    public StoreClerkVo toEdit(Integer clerkId) {
        StoreClerk clerk = this.getById(clerkId);
        StoreClerkVo vo = new StoreClerkVo();
        BeanUtils.copyProperties(clerk, vo);
        return vo;
    }

    /**
     * 编辑商家店员
     * @param storeClerkParam
     * @return
     */
    public Boolean edit(StoreClerkParam storeClerkParam) {
        StoreClerk storeClerk = new StoreClerk();
        BeanUtils.copyProperties(storeClerkParam, storeClerk);
        return this.updateById(storeClerk);
    }

    /**
     * 软删除商家店员
     * @param clerkId
     * @return
     */
    public Boolean setDelete(Integer clerkId) {
        return this.update(new LambdaUpdateWrapper<StoreClerk>().eq(StoreClerk::getClerkId, clerkId).set(StoreClerk::getIsDelete, 1));
    }


}
