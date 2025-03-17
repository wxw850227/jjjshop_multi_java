package net.jjjshop.common.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierApply;
import net.jjjshop.common.service.supplier.SupplierApplyService;
import net.jjjshop.common.service.supplier.SupplierService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

@Slf4j
@Component
public class SupplierDataUtils {
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierApplyService supplierApplyService;

    /**
     * 获取供应商统计数量
     */
    public Integer getSupplierData(String startDate, String endDate, String type) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(startDate)) {
            wrapper.ge(Supplier::getCreateTime, DateUtil.parse(startDate + " 00:00:00"));
        }
        if (StringUtils.isNotEmpty(endDate)) {
            wrapper.le(Supplier::getCreateTime, DateUtil.parse(endDate + " 23:59:59"));
        } else if (StringUtils.isNotEmpty(startDate)) {
            wrapper.le(Supplier::getCreateTime, DateUtil.parse(startDate + " 23:59:59"));
        }
        if("supplierTotal".equals(type) || "supplierAdd".equals(type)){
            return supplierService.count(wrapper);
        }else {
            return 0;
        }
    }

    /**
     * 获取供应商申请统计数量
     */
    public Integer getApplyData(String startDate, String endDate) {
        LambdaQueryWrapper<SupplierApply> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(startDate)) {
            wrapper.ge(SupplierApply::getCreateTime, DateUtil.parse(startDate + " 00:00:00"));
        }
        if (StringUtils.isNotEmpty(endDate)) {
            wrapper.le(SupplierApply::getCreateTime, DateUtil.parse(endDate + " 23:59:59"));
        } else if (StringUtils.isNotEmpty(startDate)) {
            wrapper.le(SupplierApply::getCreateTime, DateUtil.parse(startDate + " 23:59:59"));
        }
        return supplierApplyService.count(wrapper);
    }

    /**
     * 获取数据概况
     */
    public JSONObject getData(Integer shopSupplierId) throws ParseException {
        //获取今天时间
        String today = DateUtil.format(DateUtil.offsetDay(new Date(), 0), "yyyy-MM-dd");
        //获取昨天时间
        String yesterday = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");
        // 累积供应商数
        Integer supplierTotalT = this.getSupplierData(null, today, "supplierTotal");
        Integer supplierTotalY = this.getSupplierData(null, yesterday, "supplierTotal");
        // 新增供应商数
        Integer supplierAddT = this.getSupplierData(today, null, "supplierAdd");
        Integer supplierAddY = this.getSupplierData(yesterday, null, "userAdd");

        JSONObject result = new JSONObject();
        result.put("supplierTotalT", supplierTotalT);
        result.put("supplierTotalY", supplierTotalY);

        result.put("supplierAddT", supplierAddT);
        result.put("supplierAddY", supplierAddY);
        return result;
    }

    /**
     * 通过时间段查询访问量
     * @param startDate,endDate
     * @return
     */
    public Map<String, Object> getSupplierDateByDate(String startDate, String endDate) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Date startTime = DateUtil.parse(startDate);
        Date endTime = DateUtil.parse(endDate);
        //endTime加一天
        endTime = DateUtil.offsetDay(endTime,1);
        List<JSONObject> data = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (Date t = startTime; t.before(endTime); t = DateUtil.offsetDay(t,1)) {
            String day = DateUtil.format(t, "yyyy-MM-dd");
            Integer supplierAdd = this.getSupplierData(day, null, "supplierAdd");

            JSONObject json = new JSONObject();
            json.put("day", day);
            json.put("newNum", supplierAdd);
            days.add(day);
            data.add(json);
        }
        map.put("data", data);
        map.put("days", days);
        return map;
    }
}
