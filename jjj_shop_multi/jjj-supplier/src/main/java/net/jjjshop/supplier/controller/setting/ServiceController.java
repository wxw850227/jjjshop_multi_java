package net.jjjshop.supplier.controller.setting;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierService;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.common.settings.vo.SysConfigVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.supplier.SupplierServiceParam;
import net.jjjshop.supplier.service.supplier.SupplierServiceService;
import net.jjjshop.supplier.vo.setting.SupplierServiceVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "printing", tags = {"printing"})
@RestController
@RequestMapping("/supplier/setting/service")
public class ServiceController {
    @Autowired
    private SupplierServiceService supplierServiceService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<SupplierServiceVo> index() throws Exception {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        SupplierService service = supplierServiceService.getOne(new LambdaQueryWrapper<SupplierService>().eq(SupplierService::getShopSupplierId, user.getShopSupplierId()));
        //获取系统设置
        JSONObject setting = settingUtils.getSetting(SettingEnum.SYS_CONFIG.getKey(), null);
        SysConfigVo configVo = setting.toJavaObject(SysConfigVo.class);
        SupplierServiceVo vo = new SupplierServiceVo();
        if(service != null) {
            BeanUtils.copyProperties(service, vo);
        }
        vo.setShopSupplierId(user.getShopSupplierId());
        vo.setUserId(user.getUserId());
        return ApiResult.ok(vo);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index(@RequestBody SupplierServiceParam serviceParam) throws Exception {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        if(supplierServiceService.saveService(user.getShopSupplierId(), serviceParam)){
            return ApiResult.ok("保存成功");
        }else {
            return ApiResult.fail("保存失败");
        }

    }
}
