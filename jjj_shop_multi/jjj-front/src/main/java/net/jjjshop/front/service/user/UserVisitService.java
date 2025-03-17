package net.jjjshop.front.service.user;

import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserVisit;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 用户访问记录 服务类
 *
 * @author jjjshop
 * @since 2022-10-25
 */
public interface UserVisitService extends BaseService<UserVisit> {

    /**
     * 访问记录
     */
    Boolean addVisit(User user, Integer shopSupplierId, String visitcode, Product product);

}
