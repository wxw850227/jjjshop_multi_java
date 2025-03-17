package net.jjjshop.shop.controller.user;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.user.UserPointsLogPageParam;
import net.jjjshop.shop.service.user.UserPointsLogService;
import net.jjjshop.shop.vo.user.UserPointsLogVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "pointsLog", tags = {"pointsLog"})
@RestController
@RequestMapping("/shop/user/points")
public class UserPointsLogController {

    @Autowired
    private UserPointsLogService userPointsLogService;

    @RequestMapping(value = "/log", method = RequestMethod.POST)
    @RequiresPermissions("/user/points/index")
    @OperationLog(name = "log")
    @ApiOperation(value = "log", response = String.class)
    public ApiResult<Paging<UserPointsLogVo>> log(@Validated @RequestBody UserPointsLogPageParam userPointsLogPageParam) throws Exception {
        return ApiResult.ok(userPointsLogService.getList(userPointsLogPageParam));
    }
}
