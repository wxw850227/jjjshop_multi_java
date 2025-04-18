package net.jjjshop.supplier.controller.store;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.common.vo.RegionVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.store.StorePageParam;
import net.jjjshop.supplier.param.store.StoreParam;
import net.jjjshop.supplier.service.store.StoreService;
import net.jjjshop.supplier.vo.store.StoreVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/supplier/store/store")
public class StoreController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/store/store/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<StoreVo>> index(@Validated @RequestBody StorePageParam storePageParam) throws Exception {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        storePageParam.setShopSupplierId(user.getShopSupplierId());
        return ApiResult.ok(storeService.getList(storePageParam));
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    @RequiresPermissions("/store/store/add")
    @OperationLog(name = "toAdd")
    @ApiOperation(value = "toAdd", response = String.class)
    public ApiResult<List<RegionVo>> toAdd() throws Exception {
        return ApiResult.ok(storeService.toAdd());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/store/store/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@Validated @RequestBody StoreParam storeParam) throws Exception {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        storeParam.setShopSupplierId(user.getShopSupplierId());
        if(storeService.add(storeParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    @RequiresPermissions("/store/store/edit")
    @OperationLog(name = "toEdit")
    @ApiOperation(value = "toEdit", response = String.class)
    public ApiResult<Map<String, Object>> toEdit(Integer storeId) throws Exception {
        return ApiResult.ok(storeService.toEdit(storeId));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/store/store/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@Validated @RequestBody StoreParam storeParam) throws Exception {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        storeParam.setShopSupplierId(user.getShopSupplierId());
        if(storeService.edit(storeParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/store/store/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer storeId) throws Exception {
        if(storeService.setDelete(storeId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }
}
