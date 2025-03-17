package net.jjjshop.supplier.controller.setting;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "mpService", tags = {"mpService"})
@RestController
@RequestMapping("/supplier/setting/fullfree")
public class FullFreeController {
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<JSONObject> index() throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        JSONObject vo = settingUtils.getSupplierSetting(SettingEnum.FULL_FREE.getKey(), null, user.getShopSupplierId());
        return ApiResult.ok(vo);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index(@RequestBody JSONObject jsonData) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        settingUtils.saveSupplierSetting(SettingEnum.FULL_FREE.getKey(), jsonData, user.getShopSupplierId());
        return ApiResult.ok("保存成功");
    }
}
