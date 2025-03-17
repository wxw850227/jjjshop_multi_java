package net.jjjshop.front.service.user.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserFavorite;
import net.jjjshop.common.entity.user.UserVisit;
import net.jjjshop.common.mapper.user.UserVisitMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.user.UserVisitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户访问记录 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-25
 */
@Slf4j
@Service
public class UserVisitServiceImpl extends BaseServiceImpl<UserVisitMapper, UserVisit> implements UserVisitService {

    /**
     * 访问记录
     */
    public Boolean addVisit(User user, Integer shopSupplierId, String visitcode, Product product) {
        UserVisit visit = new UserVisit();
        visit.setUserId(user==null?0:user.getUserId());
        visit.setShopSupplierId(shopSupplierId);
        visit.setVisitcode(visitcode);
        if(product == null) {
            visit.setContent("访问店铺首页");
        }else {
            visit.setContent("访问商品详情页"+product.getProductName());
        }
        return this.save(visit);
    }

}
