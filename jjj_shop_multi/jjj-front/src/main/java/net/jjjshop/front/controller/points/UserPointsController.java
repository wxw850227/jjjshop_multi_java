package net.jjjshop.front.controller.points;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.points.UserPointsLogPageParam;
import net.jjjshop.front.service.points.UserPointsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Api(value = "pointsLog", tags = {"积分记录"})
@RestController
@RequestMapping("/front/points/log")
public class UserPointsController extends BaseController {

    @Autowired
    private UserPointsLogService userPointsLogService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@Validated @RequestBody UserPointsLogPageParam userPointsLogPageParam) throws Exception {
        return ApiResult.ok(userPointsLogService.getList(userPointsLogPageParam,this.getUser(true)));
    }
}
