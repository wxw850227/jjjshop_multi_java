package net.jjjshop.supplier.service.user;

import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.user.UserPageParam;
import net.jjjshop.supplier.vo.user.UserVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 用户记录表 服务类
 * @author jjjshop
 * @since 2022-07-01
 */
public interface UserService extends BaseService<User> {
    /**
     * 查找用户
     * @param userPageParam
     * @return
     */
    Paging<UserVo> getList(UserPageParam userPageParam);

    /**
     * 获取所有用户
     * @param
     * @return
     */
    List<UserVo> getAll();

    /**
     * 消减用户的消费金额
     * @param user
     * @param expendMoney
     * @return
     */
    Boolean setDecUserExpend(User user, BigDecimal expendMoney);


    /**
     * 通过等级Id获取所有用户Id
     * @param
     * @return
     */
    List<Integer> getUserIdsByGrade(Integer gradeId);

    /**
     * 获取所有用户Id
     * @param
     * @return
     */
    List<Integer> getUserIds();

}
