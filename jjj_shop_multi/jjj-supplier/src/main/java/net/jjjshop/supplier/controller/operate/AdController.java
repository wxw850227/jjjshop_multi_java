package net.jjjshop.supplier.controller.operate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.ad.AdPageParam;
import net.jjjshop.supplier.param.ad.AdParam;
import net.jjjshop.supplier.service.ad.AdService;
import net.jjjshop.supplier.vo.ad.AdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "查询用户接口", tags = {"查询用户接口"})
@RestController
@RequestMapping("/supplier/operate/ad")
public class AdController {
    @Autowired
    private AdService adService;
    @Autowired
    private SupplierUserService supplierUserService;


    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<AdVo>> index(@Validated @RequestBody AdPageParam adPageParam) throws Exception{
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        adPageParam.setShopSupplierId(user.getShopSupplierId());
        return ApiResult.ok(adService.getList(adPageParam));
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    @OperationLog(name = "toAdd")
    @ApiOperation(value = "toAdd", response = String.class)
    public ApiResult<String> toAdd() {
        return ApiResult.ok("");
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated AdParam adParam) {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        adParam.setShopSupplierId(user.getShopSupplierId());
        if (adService.add(adParam)) {
            return ApiResult.ok(null, "添加成功");
        } else {
            return ApiResult.fail("添加失败");
        }
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<AdVo> detail(Integer adId) {
        return ApiResult.ok(adService.detail(adId));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated AdParam adParam) {
        if (adService.edit(adParam)) {
            return ApiResult.ok(null, "编辑成功");
        } else {
            return ApiResult.fail("编辑失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer adId) {
        if (adService.delById(adId)) {
            return ApiResult.ok(null, "删除成功");
        } else {
            return ApiResult.fail("删除失败");
        }
    }
}
