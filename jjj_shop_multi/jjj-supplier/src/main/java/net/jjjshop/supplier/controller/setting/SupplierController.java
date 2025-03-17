

package net.jjjshop.supplier.controller.setting;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierService;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.setting.SupplierSettingParam;
import net.jjjshop.supplier.vo.setting.SupplierSettingVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "delivery", tags = {"delivery"})
@RestController
@RequestMapping("/supplier/setting/supplier")
public class SupplierController {
    @Autowired
    private SupplierUserService supplierUserService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private UploadFileUtils uploadFileUtils;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<SupplierSettingVo> index() throws Exception{
        Integer userId = SupplierLoginUtil.getUserId();
        SupplierUser user = supplierUserService.getById(userId);
        Supplier supplier = supplierService.getById(user.getShopSupplierId());
        SupplierSettingVo vo = new SupplierSettingVo();
        BeanUtils.copyProperties(supplier, vo);
        vo.setLogoFilePath(uploadFileUtils.getFilePath(vo.getLogoId()));
        vo.setBusinessFilePath(uploadFileUtils.getFilePath(vo.getBusinessId()));
        return ApiResult.ok(vo);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index(SupplierSettingParam supplierSettingParam) throws Exception{
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierSettingParam, supplier);
        supplierService.updateById(supplier);
        return ApiResult.ok("保存成功");
    }
}
