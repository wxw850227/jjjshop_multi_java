package net.jjjshop.shop.controller.user;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.BalanceLogEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.user.UserBalanceLogPageParam;
import net.jjjshop.shop.service.user.UserBalanceLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "balanceLog", tags = {"balanceLog"})
@RestController
@RequestMapping("/shop/user/balance")
public class UserBalanceController {
    @Autowired
    private UserBalanceLogService userBalanceLogService;
    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/user/balance/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@Validated @RequestBody UserBalanceLogPageParam userBalanceLogPageParam) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("list", userBalanceLogService.getList(userBalanceLogPageParam));
        map.put("scene", BalanceLogEnum.getList());
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    @OperationLog(name = "setting")
    @ApiOperation(value = "setting", response = String.class)
    public ApiResult<JSONObject> setting() throws Exception{
        JSONObject vo = settingUtils.getSetting(SettingEnum.BALANCE.getKey(), null);
        return ApiResult.ok(vo);
    }

    @RequestMapping(value = "/setting", method = RequestMethod.POST)
    @OperationLog(name = "setting")
    @ApiOperation(value = "setting", response = String.class)
    public ApiResult<String> setting(@RequestBody JSONObject jsonData) throws Exception{
        settingUtils.saveSetting(SettingEnum.BALANCE.getKey(), jsonData);
        return ApiResult.ok("保存成功");
    }
}
