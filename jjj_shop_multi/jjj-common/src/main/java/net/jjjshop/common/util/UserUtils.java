package net.jjjshop.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.common.entity.user.UserGradeLog;
import net.jjjshop.common.entity.user.UserPointsLog;
import net.jjjshop.common.service.user.UserGradeLogService;
import net.jjjshop.common.service.user.UserGradeService;
import net.jjjshop.common.service.user.UserPointsLogService;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
public class UserUtils {

    @Autowired
    private UserService userService;
    @Autowired
    private UserGradeService userGradeService;
    @Autowired
    private UserPointsLogService userPointsLogService;
    @Autowired
    private UserGradeLogService userGradeLogService;

    /**
     * 累积用户的可用积分
     */
    public void setIncPoints(Integer userId, Integer points, String description, Boolean upgrade)
    {
        User user = userService.getById(userId);
        // 新增积分变动明细
        UserPointsLog log = new UserPointsLog();
        log.setUserId(userId);
        log.setValue(points);
        log.setDescription(description);
        log.setAppId(user.getAppId());
        userPointsLogService.save(log);
        // 更新用户可用积分
        Integer userPoints = 0;
        if(user.getPoints() + points > 0){
            userPoints = user.getPoints() + points;
        }
        userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, userId)
                .set(User::getPoints, userPoints));
        // 用户总积分
        if(points > 0){
            userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, userId)
                    .setSql("`total_points` = `total_points` + " + points));
        }
    }

    /**
     * 可用等级
     * @return
     */
    public List<UserGrade> getUsableGradeList(Long appId){
        if(appId == null){
            appId = RequestDetailThreadLocal.getRequestDetail().getAppId();
        }
        return userGradeService.list(new LambdaQueryWrapper<UserGrade>()
                .eq(UserGrade::getIsDelete, false).eq(UserGrade::getAppId, appId)
                .orderByAsc(UserGrade::getWeight).orderByAsc(UserGrade::getCreateTime));
    }

    /**
     * 用户升级处理
     */
    @Async
    public void userGradeUpgrade(Integer userId){
        User user = userService.getById(userId);
        // 获取所有等级
        List<UserGrade> list = this.getUsableGradeList(Long.valueOf(user.getAppId()));
        if (list.size() == 0) {
            return;
        }
        // 遍历等级，根据升级条件 查询满足消费金额的用户列表，并且他的等级小于该等级
        UserGrade upgradeGrade = null;
        for(UserGrade grade:list){
            if(grade.getIsDefault() == 1){
                continue;
            }
            boolean isUpgrade = this.checkCanUpdate(user, grade);
            if(isUpgrade){
                upgradeGrade = grade;
            }else{
                break;
            }
        }
        if(upgradeGrade != null && upgradeGrade.getGradeId().intValue() != user.getGradeId().intValue()){
            log.info(String.format("setUserGrade userId:%s gradeId:%s", user.getUserId(), upgradeGrade.getGradeId()));
            // 修改会员的等级
            this.upgradeGrade(user, upgradeGrade);
            // 赠送积分
            if(upgradeGrade.getGivePoints() > 0){
                this.setIncPoints(userId, upgradeGrade.getGivePoints(), "用户升级赠送积分", false);
            }
        }
    }

    /**
     * 查询满足会员等级升级条件的用户列表
     */
    public boolean checkCanUpdate(User user, UserGrade grade)
    {
        // 按消费升级
        if(grade.getOpenMoney() == 1 && user.getExpendMoney().compareTo(new BigDecimal(grade.getUpgradeMoney())) >= 0){
            return true;
        }
        // 按积分升级
        if(grade.getOpenPoints() == 1 && user.getTotalPoints() >= grade.getUpgradePoints()){
            return true;
        }
        // 按邀请人数升级
        if(grade.getOpenInvite() == 1 && user.getTotalInvite() >= grade.getUpgradeInvite()){
            return true;
        }
        return false;
    }

    /**
     * 批量设置会员等级
     */
    private void upgradeGrade(User user, UserGrade upgradeGrade){
        // 更新会员等级的数据
        userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, user.getUserId())
                .set(User::getGradeId, upgradeGrade.getGradeId()));
        UserGradeLog log = new UserGradeLog();
        log.setOldGradeId(user.getGradeId());
        log.setNewGradeId(upgradeGrade.getGradeId());
        //自动升级
        log.setChangeType(20);
        log.setUserId(user.getUserId());
        log.setAppId(user.getAppId());
        userGradeLogService.save(log);
    }

    /**
     * 累积用户总消费金额
     */
    public Boolean setIncPayMoney(User user, BigDecimal money) {
        user.setPayMoney(user.getPayMoney().add(money));
        return userService.updateById(user);
    }

    /**
     * 提现驳回：冻结用户资金
     */
    public Boolean freezeMoney(User user, BigDecimal money) {
        user.setBalance(user.getBalance().subtract(money));
        user.setFreezeMoney(user.getFreezeMoney().add(money));
        return userService.updateById(user);
    }

    /**
     * 提现打款成功：累积提现余额
     */
    public Boolean totalMoney(Integer userId, BigDecimal money) {
        User user = userService.getById(userId);
        user.setFreezeMoney(user.getFreezeMoney().subtract(money));
        user.setCashMoney(user.getCashMoney().add(money));
        return userService.updateById(user);
    }

    /**
     * 提现驳回：解冻用户资金
     */
    public Boolean backFreezeMoney(User user, BigDecimal money) {
        user.setBalance(user.getBalance().add(money));
        user.setFreezeMoney(user.getFreezeMoney().subtract(money));
        return userService.updateById(user);
    }

    /**
     * 获取所有用户等级
     * @param
     * @return
     */
    public List<UserGrade> getAllGrade() {
        return userGradeService.list(new LambdaQueryWrapper<UserGrade>()
                .select(UserGrade::getGradeId, UserGrade::getName)
                .eq(UserGrade::getIsDelete, 0)
                .orderByAsc(UserGrade::getWeight, UserGrade::getCreateTime)
        );
    }
}
