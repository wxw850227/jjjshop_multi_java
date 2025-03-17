package net.jjjshop.supplier.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.Region;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserFavorite;
import net.jjjshop.common.mapper.user.UserFavoriteMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.user.UserFavoritePageParam;
import net.jjjshop.supplier.service.user.UserFavoriteService;
import net.jjjshop.supplier.service.user.UserService;
import net.jjjshop.supplier.vo.user.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 我的收藏 服务实现类
 * @author jjjshop
 * @since 2022-08-02
 */
@Slf4j
@Service
public class UserFavoriteServiceImpl extends BaseServiceImpl<UserFavoriteMapper, UserFavorite> implements UserFavoriteService {

    @Autowired
    private UserService userService;

    /**
     * 获取关注店铺的用户
     * @param shopSupplierId
     * @param userFavoritePageParam
     * @return
     */
    public Paging<UserVo> getUserList(Integer shopSupplierId, UserFavoritePageParam userFavoritePageParam) {
        Page<UserFavorite> page = new PageInfo<>(userFavoritePageParam);
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(userFavoritePageParam.getSearch())) {
            List<Integer> userIds = userService.list(new LambdaQueryWrapper<User>()
                    .and(i -> i.or().like(User::getNickname, userFavoritePageParam.getSearch())
                            .or().like(User::getMobile, userFavoritePageParam.getSearch())))
                    .stream().map(User::getUserId).collect(Collectors.toList());
            wrapper.in(UserFavorite::getUserId, userIds);
        }
        wrapper.eq(UserFavorite::getShopSupplierId, shopSupplierId);
        wrapper.eq(UserFavorite::getType, 10);
        IPage<UserFavorite> iPage = this.page(page, wrapper);
        IPage<UserVo> result = iPage.convert(item -> {
            UserVo vo = new UserVo();
            User user = userService.getById(item.getUserId());
            BeanUtils.copyProperties(user, vo);
            vo.setCreateTime(item.getCreateTime());
            return vo;
        });
        return new Paging<>(result);
    }

    /**
     * 获取某天的关注用户数
     * @param day
     * @param shopSupplierId
     * @return
     */
    public Integer getUserTotal(String day, Integer shopSupplierId) {
        return this.count(new LambdaQueryWrapper<UserFavorite>()
                .eq(UserFavorite::getPid, shopSupplierId)
                .eq(UserFavorite::getType, 10)
                .ge(UserFavorite::getCreateTime, day+" 00:00:00")
                .lt(UserFavorite::getCreateTime, day+" 23:59:59"));
    }

}
