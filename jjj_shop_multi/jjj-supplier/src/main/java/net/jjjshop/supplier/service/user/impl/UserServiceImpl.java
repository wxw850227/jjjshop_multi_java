package net.jjjshop.supplier.service.user.impl;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.*;
import net.jjjshop.common.mapper.user.UserMapper;
import net.jjjshop.common.service.user.UserBalanceLogService;
import net.jjjshop.common.service.user.UserGradeLogService;
import net.jjjshop.common.service.user.UserGradeService;
import net.jjjshop.common.service.user.UserPointsLogService;
import net.jjjshop.common.util.UserUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.user.UserPageParam;
import net.jjjshop.supplier.service.user.UserService;
import net.jjjshop.supplier.vo.user.UserVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-01
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserGradeService userGradeService;

    /**
     * 查找用户
     * @param userPageParam
     * @return
     */
    public Paging<UserVo> getList(UserPageParam userPageParam) {
        // 用户列表
        Page<User> page = new PageInfo<>(userPageParam);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getIsDelete, 0);
        if(StringUtils.isNotEmpty(userPageParam.getSearch())){
            wrapper.like(User::getNickname, userPageParam.getSearch());
        }
        if(StringUtils.isNotEmpty(userPageParam.getNickName())){
            wrapper.like(User::getNickname, userPageParam.getNickName());
        }
        if(userPageParam.getGradeId() != null && userPageParam.getGradeId() != 0){
            wrapper.eq(User::getGradeId, userPageParam.getGradeId());
        }
        if(userPageParam.getSex() != null && userPageParam.getSex() != -1){
            wrapper.eq(User::getGender,userPageParam.getSex());
        }
        if(userPageParam.getStartDate() != null){
            wrapper.ge(User::getCreateTime,  DateUtil.format(userPageParam.getStartDate(), "yyyy-MM-dd 00:00:00"));
        }
        if(userPageParam.getEndDate() != null){
            wrapper.le(User::getCreateTime,DateUtil.format(userPageParam.getEndDate(), "yyyy-MM-dd 23:59:59"));
        }else if(userPageParam.getStartDate() != null){
            wrapper.le(User::getCreateTime,  DateUtil.format(userPageParam.getStartDate(), "yyyy-MM-dd 23:59:59"));
        }
        wrapper.orderByDesc(User::getCreateTime);
        IPage<User> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<UserVo> resultPage = iPage.convert(item -> {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties(item, vo);
            vo.setGradeName(userGradeService.getById(vo.getGradeId()).getName());
            return vo;
        });
        return new Paging(resultPage);

    }

    /**
     * 获取所有用户
     * @param
     * @return
     */
    public List<UserVo> getAll(){
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getIsDelete, 0));
        List<UserVo> result = list.stream().map(e -> {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties(e, vo);
            vo.setGradeName(userGradeService.getById(e.getGradeId()).getName());
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 消减用户的消费金额
     * @param user
     * @param expendMoney
     * @return
     */
    public Boolean setDecUserExpend(User user, BigDecimal expendMoney){
        return this.update(new LambdaUpdateWrapper<User>()
                .eq(User::getUserId, user.getUserId())
                .set(User::getExpendMoney, user.getExpendMoney().subtract(expendMoney)));
    }

    /**
     * 通过等级Id获取所有用户Id
     * @param
     * @return
     */
    public List<Integer> getUserIdsByGrade(Integer gradeId){
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getIsDelete, 0).eq(User::getGradeId, gradeId).orderByAsc(User::getUserId));
        List<Integer> result = list.stream().map(e -> {
            return e.getUserId();
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 获取所有用户Id
     * @param
     * @return
     */
    public List<Integer> getUserIds(){
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getIsDelete, 0).orderByAsc(User::getUserId));
        List<Integer> result = list.stream().map(e -> {
            return e.getUserId();
        }).collect(Collectors.toList());
        return result;
    }
}
