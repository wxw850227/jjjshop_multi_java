

package net.jjjshop.supplier.controller.auth;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.service.supplier.SupplierService;
import net.jjjshop.supplier.service.supplier.SupplierRoleService;
import net.jjjshop.supplier.service.supplier.SupplierUserService;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.shiro.vo.LoginSupplierAccessVo;
import net.jjjshop.framework.shiro.vo.LoginSupplierUserRedisVo;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.supplier.SupplierUserPageParam;
import net.jjjshop.supplier.param.supplier.SupplierUserParam;
import net.jjjshop.supplier.service.user.UserService;
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
@RequestMapping("/supplier/auth/user")
public class SupplierUserController {
    @Autowired
    private SupplierUserService supplierUserService;
    @Autowired
    private SupplierRoleService supplierRoleService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getRoleList", method = RequestMethod.POST)
    @OperationLog(name = "getRoleList")
    @ApiOperation(value = "getRoleList", response = String.class)
    public ApiResult<List<LoginSupplierAccessVo>> getRoleList() throws Exception{
        LoginSupplierUserRedisVo loginSupplierUserRedisVo = SupplierLoginUtil.getLoginSupplierUserRedisVo();
        return ApiResult.ok(loginSupplierUserRedisVo.getMenus());
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    @OperationLog(name = "getUserInfo")
    @ApiOperation(value = "getUserInfo", response = String.class)
    public ApiResult<Map<String,Object>> getUserInfo() throws Exception{
        Map<String,Object> result = new HashMap<>();
        Integer userId = SupplierLoginUtil.getUserId();
        SupplierUser user = supplierUserService.getById(userId);
        Supplier supplier = supplierService.getById(user.getShopSupplierId());
        // 登录用户名
        result.put("user", user);
        // 版本号
        result.put("version", springBootJjjProperties.getProjectVersion());
        // 商城名称
        result.put("shop_name", supplier.getName());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/auth/user/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index(@Validated @RequestBody SupplierUserPageParam params) throws Exception{
        Map<String,Object> result = new HashMap<>();
        result.put("userList", supplierUserService.getList(params));
        result.put("roleList", supplierRoleService.getList());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/auth/user/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Boolean> add(@Validated @RequestBody SupplierUserParam supplierUserParam) {
        return ApiResult.ok(supplierUserService.add(supplierUserParam));
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    @RequiresPermissions("/auth/user/edit")
    @OperationLog(name = "toEdit")
    @ApiOperation(value = "toEdit", response = String.class)
    public ApiResult<JSONObject> toEdit(Integer supplierUserId) {
        SupplierUser supplierUser = supplierUserService.getById(supplierUserId);
        JSONObject result = new JSONObject();
        if(supplierUser.getUserId() > 0){
            User user = userService.getById(supplierUser.getUserId());
            result.put("userId", user.getUserId());
            result.put("nickname", user.getNickname());
            result.put("avatarurl", user.getAvatarurl());
        }else{
            result.put("userId", 0);
        }
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/auth/user/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Boolean> edit(@Validated @RequestBody SupplierUserParam supplierUserParam) {
        return ApiResult.ok(supplierUserService.edit(supplierUserParam));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/auth/user/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<Boolean> delete(Integer supplierUserId) {
        return ApiResult.ok(supplierUserService.setDelete(supplierUserId));
    }
}
