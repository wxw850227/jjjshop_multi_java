package net.jjjshop.shop.controller.statistics;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.statistics.RankingParam;
import net.jjjshop.shop.service.statistics.UserRankingService;
import net.jjjshop.shop.vo.user.UserVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/shop/statistics/user")
public class UserController {
    @Autowired
    private UserRankingService userRankingService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/statistics/user/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index() throws Exception{
        HashMap<String, Object> map = new HashMap<>();
        JSONObject user = userRankingService.getData();
        List<UserVo> payRanking = userRankingService.getUserRanking("pay");
        List<UserVo> pointsRanking = userRankingService.getUserRanking("points");
        List<UserVo> inviteRanking = userRankingService.getUserRanking("invite");
        map.put("user",user);
        map.put("payRanking",payRanking);
        map.put("pointsRanking",pointsRanking);
        map.put("inviteRanking",inviteRanking);
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    @OperationLog(name = "newuser")
    @ApiOperation(value = "newuser", response = String.class)
    public ApiResult<Map<String,Object>> newuser(@Validated @RequestBody RankingParam rankingParam) throws Exception{
        return ApiResult.ok(userRankingService.getNewUserByDate(rankingParam.getStartDate(), rankingParam.getEndDate()));
    }

    @RequestMapping(value = "/scale", method = RequestMethod.POST)
    @OperationLog(name = "scale")
    @ApiOperation(value = "scale", response = String.class)
    public ApiResult<Map<String,Object>> scale(Integer day) throws Exception{
        return  ApiResult.ok(userRankingService.getPayScaleData(day));
    }

    @RequestMapping(value = "/payuser", method = RequestMethod.POST)
    @OperationLog(name = "payuser")
    @ApiOperation(value = "payuser", response = String.class)
    public ApiResult<Map<String,Object>> payuser(@Validated @RequestBody RankingParam rankingParam) throws Exception{
        return  ApiResult.ok(userRankingService.getPayUserByDate(rankingParam.getStartDate(), rankingParam.getEndDate()));
    }

}
