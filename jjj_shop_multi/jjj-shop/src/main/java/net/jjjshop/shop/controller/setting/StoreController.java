

package net.jjjshop.shop.controller.setting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.log.annotation.OperationLog;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "setting", tags = {"setting"})
@RestController
@RequestMapping("/shop/setting/store")
public class StoreController {

    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @RequiresPermissions("/setting/store/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<JSONObject> index() throws Exception{
        JSONObject vo = settingUtils.getSetting(SettingEnum.STORE.getKey(), null);
        vo.put("allType", DeliveryTypeEnum.getList());
        return ApiResult.ok(vo);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/store/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index(@RequestBody JSONObject jsonData) throws Exception{
        StoreVo storeVo = JSONObject.parseObject(jsonData.toJSONString(), StoreVo.class);
        StoreVo.KuaiDi100 kuaiDi100 = storeVo.getKuaiDi100();
        kuaiDi100.setCustomer(kuaiDi100.getCustomer().trim());
        kuaiDi100.setKey(kuaiDi100.getKey().trim());
        StoreVo.Policy policy = storeVo.getPolicy();
        policy.setPrivacy(policy.getPrivacy().trim());
        policy.setService(policy.getService().trim());
        if(CollectionUtils.isEmpty(storeVo.getDeliveryType())){
            throw new BusinessException("请至少选择一种配送方式");
        }
        //按数字从小到大排序
        storeVo.getDeliveryType().sort(Integer::compareTo);
        jsonData = (JSONObject) JSON.toJSON(storeVo);
        settingUtils.saveSetting(SettingEnum.STORE.getKey(), jsonData);
        return ApiResult.ok("保存成功");
    }
}
