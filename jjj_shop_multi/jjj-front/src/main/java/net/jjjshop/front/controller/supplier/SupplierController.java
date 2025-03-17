package net.jjjshop.front.controller.supplier;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderSettled;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.common.util.OrderDataUtils;
import net.jjjshop.common.util.UserDataUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.order.OrderSettledPageParam;
import net.jjjshop.front.param.supplier.SupplierPageParam;
import net.jjjshop.front.param.supplier.SupplierParam;
import net.jjjshop.front.service.app.AppService;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.order.OrderSettledService;
import net.jjjshop.front.service.supplier.SupplierCategoryService;
import net.jjjshop.front.service.supplier.SupplierService;
import net.jjjshop.front.service.user.UserVisitService;
import net.jjjshop.front.vo.order.OrderDetailVo;
import net.jjjshop.front.vo.order.OrderSettledListVo;
import net.jjjshop.front.vo.page.AdVo;
import net.jjjshop.front.vo.supplier.SupplierCategoryListVo;
import net.jjjshop.front.vo.supplier.SupplierDetailVo;
import net.jjjshop.front.vo.supplier.SupplierListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "store", tags = {"订单核销"})
@RestController
@RequestMapping("/front/supplier/index")
public class SupplierController extends BaseController {
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierUserService supplierUserService;
    @Autowired
    private UserVisitService userVisitService;
    @Autowired
    private UserDataUtils userDataUtils;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDataUtils orderDataUtils;
    @Autowired
    private OrderSettledService orderSettledService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @OperationLog(name = "list")
    @ApiOperation(value = "list", response = String.class)
    public ApiResult<Paging<SupplierListVo>> detail(@RequestBody SupplierPageParam supplierPageParam) throws Exception{
        return ApiResult.ok(supplierService.supplierList(supplierPageParam));
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody SupplierParam supplierParam) throws Exception{
        User user = this.getUser(false);
        Map<String, Object> map = new HashMap<>();
        //获取店铺信息
        SupplierDetailVo detail = supplierService.getDetail(supplierParam.getShopSupplierId(), user);
        if(detail == null) {
            throw new BusinessException("店铺不存在");
        }
        //广告图
        List<AdVo> adList = supplierService.getAdList(supplierParam.getShopSupplierId());
        //添加访问记录
        userVisitService.addVisit(user,supplierParam.getShopSupplierId(),supplierParam.getVisitcode(), null);
        map.put("detail", detail);
        map.put("adList", adList);
        return ApiResult.ok(map);
    }

    //成交数据
    @RequestMapping(value = "/tradeData", method = RequestMethod.POST)
    @OperationLog(name = "tradeData")
    @ApiOperation(value = "tradeData", response = String.class)
    public ApiResult<Map<String, Object>> tradeData() throws Exception{
        User user = this.getUser(false);
        Map<String, Object> map = new HashMap<>();
        //获取店铺信息
        SupplierUser supplierUser = supplierUserService.getOne(new LambdaQueryWrapper<SupplierUser>().eq(SupplierUser::getUserId, user.getUserId()).last("LIMIT 1"));
        if(supplierUser == null || !(supplierUser.getShopSupplierId()>0)) {
            throw new BusinessException("您还未开通店铺");
        }
        //获取店铺信息
        SupplierDetailVo detail = supplierService.getDetail(supplierUser.getShopSupplierId(), user);
        //累计成交数
        Integer totalPayOrder = orderService.getTotalPayOrder(supplierUser.getShopSupplierId());
        //今日成交笔数
        Integer todayPayOrder = orderService.getTodayPayOrder(supplierUser.getShopSupplierId());

        map.put("totalCount", totalPayOrder);
        map.put("todayCount", todayPayOrder);
        map.put("supplier", detail);
        map.put("isOpen", false);
        return ApiResult.ok(map);
    }

    //成交数据
    @RequestMapping(value = "/storedata", method = RequestMethod.POST)
    @OperationLog(name = "storedata")
    @ApiOperation(value = "storedata", response = String.class)
    public ApiResult<Map<String, Object>> storedata(@RequestBody OrderSettledPageParam orderSettledPageParam) throws Exception{
        Map<String, Object> map = new HashMap<>();
        User user = this.getUser(false);
        //获取店铺信息
        SupplierUser supplierUser = supplierUserService.getOne(new LambdaQueryWrapper<SupplierUser>().eq(SupplierUser::getUserId, user.getUserId()).last("LIMIT 1"));
        JSONObject order = orderDataUtils.getData(supplierUser.getShopSupplierId());
        JSONObject visit = userDataUtils.getData(supplierUser.getShopSupplierId());
        Paging<OrderSettledListVo> ordersettle = orderSettledService.getList(supplierUser.getShopSupplierId(), orderSettledPageParam);
        map.put("order", order);
        map.put("visit", visit);
        map.put("ordersettle", ordersettle);
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/settledetail", method = RequestMethod.POST)
    @OperationLog(name = "settledetail")
    @ApiOperation(value = "settledetail", response = String.class)
    public ApiResult<Map<String, Object>> settledetail(Integer settledId) {
        Map<String, Object> map = new HashMap<>();
        OrderSettled settled = orderSettledService.getById(settledId);
        OrderSettledListVo vo = new OrderSettledListVo();
        BeanUtils.copyProperties(settled, vo);
        OrderDetailVo detail = orderService.detail(vo.getOrderId());
        map.put("model", vo);
        map.put("order", detail);
        return ApiResult.ok(map);
    }

}
