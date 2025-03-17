package net.jjjshop.supplier.controller.store;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.store.StoreClerkPageParam;
import net.jjjshop.supplier.param.store.StoreClerkParam;
import net.jjjshop.supplier.service.store.StoreClerkService;
import net.jjjshop.supplier.service.store.StoreService;
import net.jjjshop.supplier.vo.store.StoreClerkVo;
import net.jjjshop.supplier.vo.store.StoreVo;
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
@RequestMapping("/supplier/store/clerk")
public class StoreClerkController {
    @Autowired
    private StoreClerkService storeClerkService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/store/clerk/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<StoreClerkVo>> index(@Validated @RequestBody StoreClerkPageParam storeClerkPageParam) throws Exception {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        storeClerkPageParam.setShopSupplierId(user.getShopSupplierId());
        return ApiResult.ok(storeClerkService.getList(storeClerkPageParam));
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    @RequiresPermissions("/store/clerk/add")
    @OperationLog(name = "toAdd")
    @ApiOperation(value = "toAdd", response = String.class)
    public ApiResult<List<StoreVo>> toAdd() throws Exception {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        return ApiResult.ok(storeService.getAll(user.getShopSupplierId()));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/store/clerk/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@Validated @RequestBody StoreClerkParam storeClerkParam) throws Exception {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        if(storeClerkService.add(user.getShopSupplierId(), storeClerkParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    @RequiresPermissions("/store/clerk/edit")
    @OperationLog(name = "toEdit")
    @ApiOperation(value = "toEdit", response = String.class)
    public ApiResult<Map<String, Object>> toEdit(Integer clerkId) throws Exception {
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("storeList", storeService.getAll(user.getShopSupplierId()));
        map.put("detail", storeClerkService.toEdit(clerkId));
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/store/clerk/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@Validated @RequestBody StoreClerkParam storeClerkParam) throws Exception {
        if(storeClerkService.edit(storeClerkParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/store/clerk/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer clerkId) throws Exception {
        if(storeClerkService.setDelete(clerkId)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }
}
