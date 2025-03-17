

package net.jjjshop.front.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.common.service.user.UserGradeService;
import net.jjjshop.common.settings.vo.BalanceVo;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.page.CenterMenuService;
import net.jjjshop.front.vo.supplier.SupplierUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "user", tags = {"用户首页"})
@RestController
@RequestMapping("/front/user/index")
public class IndexController extends BaseController {

    @Autowired
    private CenterMenuService centerMenuService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserGradeService userGradeService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(Integer appId){
        Map<String, Object> result = new HashMap<>();
        User user = this.getUser(true);
        if(appId != null && !appId.equals(user.getAppId())){
            throw new BusinessException("当前应用不匹配");
        }
        result.put("user", user);
        //供应商用户信息
        SupplierUser supplierUser = supplierUserService.getOne(new LambdaQueryWrapper<SupplierUser>().eq(SupplierUser::getUserId, user.getUserId()).last("LIMIT 1"));
        SupplierUserVo userVo = new SupplierUserVo();
        if(supplierUser != null) {
            BeanUtils.copyProperties(supplierUser, userVo);
        }
        result.put("supplierUser", userVo);
        result.put("userGrade", userGradeService.getById(user.getGradeId())==null?"":userGradeService.getById(user.getGradeId()).getName());
        result.put("menus", centerMenuService.getMenus());
        // 订单数量
        JSONObject orderCount = new JSONObject();
        orderCount.put("payment", orderService.getCount(user.getUserId(), "payment"));
        orderCount.put("delivery", orderService.getCount(user.getUserId(), "delivery"));
        orderCount.put("received", orderService.getCount(user.getUserId(), "received"));
        orderCount.put("comment", orderService.getCount(user.getUserId(), "comment"));
        result.put("orderCount", orderCount);
        // 获取手机号
        result.put("getPhone", false);
        //商城设置
        JSONObject store = settingUtils.getSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = store.toJavaObject(StoreVo.class);
        //供应商背景图
        String supplierImage = StringUtils.isNotBlank(storeVo.getSupplierImage()) ? storeVo.getSupplierImage() : "";
        //充值功能是否开启
        JSONObject balance = settingUtils.getSetting(SettingEnum.BALANCE.getKey(), null);
        BalanceVo balanceVo = balance.toJavaObject(BalanceVo.class);
        Integer balanceOpen = balanceVo.getIsOpen();
        // 设置项
        JSONObject setting = new JSONObject();
        setting.put("pointsName", "积分");
        setting.put("supplierImage", supplierImage);
        setting.put("balanceOpen", balanceOpen);
        result.put("setting", setting);
        //todo 获取消息条数
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    @OperationLog(name = "setting")
    @ApiOperation(value = "setting", response = String.class)
    public ApiResult<Map<String, Object>> setting(){
        Map<String, Object> result = new HashMap<>();
        User user = this.getUser(true);
        result.put("userInfo", user);
        return ApiResult.ok(result);
    }
}
