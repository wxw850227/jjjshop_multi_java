package net.jjjshop.common.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.UserFavorite;
import net.jjjshop.common.entity.user.UserVisit;
import net.jjjshop.common.service.user.UserFavoriteService;
import net.jjjshop.common.service.user.UserVisitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

@Slf4j
@Component
public class UserDataUtils {

    @Autowired
    private UserFavoriteService userFavoriteService;
    @Autowired
    private UserVisitService userVisitService;

    /**
     * 获取某天的店铺关注数
     * $endDate不传则默认当天
     */
    public Integer getFavData(String startDate, String endDate, Integer type, Integer shopSupplierId) {
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(startDate)) {
            wrapper.ge(UserFavorite::getCreateTime, DateUtil.parse(startDate + " 00:00:00"));
        }
        if (StringUtils.isNotEmpty(endDate)) {
            wrapper.le(UserFavorite::getCreateTime, DateUtil.parse(endDate + " 23:59:59"));
        } else if (StringUtils.isNotEmpty(startDate)) {
            wrapper.le(UserFavorite::getCreateTime, DateUtil.parse(startDate + " 23:59:59"));
        }
        if(shopSupplierId!=null && shopSupplierId>0) {
            wrapper.eq(UserFavorite::getShopSupplierId, shopSupplierId);
        }
        wrapper.eq(UserFavorite::getType, type);
        return userFavoriteService.count(wrapper);
    }

    /**
     * 获取某天的访问数
     * $endDate不传则默认当天
     */
    public Integer getVisitData(String startDate, String endDate, Integer shopSupplierId) {
        LambdaQueryWrapper<UserVisit> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(startDate)) {
            wrapper.ge(UserVisit::getCreateTime, DateUtil.parse(startDate + " 00:00:00"));
        }
        if (StringUtils.isNotEmpty(endDate)) {
            wrapper.le(UserVisit::getCreateTime, DateUtil.parse(endDate + " 23:59:59"));
        } else if (StringUtils.isNotEmpty(startDate)) {
            wrapper.le(UserVisit::getCreateTime, DateUtil.parse(startDate + " 23:59:59"));
        }
        if(shopSupplierId!=null && shopSupplierId>0) {
            wrapper.eq(UserVisit::getShopSupplierId, shopSupplierId);
        }
        return userVisitService.count(wrapper);
    }

    /**
     * 获取某天的访客数
     * $endDate不传则默认当天
     */
    public Integer getVisitUserData(String startDate, String endDate, Integer shopSupplierId) {
        QueryWrapper<UserVisit> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(startDate)) {
            wrapper.lambda().ge(UserVisit::getCreateTime, DateUtil.parse(startDate + " 00:00:00"));
        }
        if (StringUtils.isNotEmpty(endDate)) {
            wrapper.lambda().le(UserVisit::getCreateTime, DateUtil.parse(endDate + " 23:59:59"));
        } else if (StringUtils.isNotEmpty(startDate)) {
            wrapper.lambda().le(UserVisit::getCreateTime, DateUtil.parse(startDate + " 23:59:59"));
        }
        if(shopSupplierId!=null && shopSupplierId>0) {
            wrapper.lambda().eq(UserVisit::getShopSupplierId, shopSupplierId);
        }
        wrapper.select("DISTINCT visitcode");
        return userVisitService.count(wrapper);
    }

    /**
     * 获取用户数据
     */
    public JSONObject getData(Integer shopSupplierId) throws ParseException {
        //获取今天时间
        String today = DateUtil.format(DateUtil.offsetDay(new Date(), 0), "yyyy-MM-dd");
        //获取昨天时间
        String yesterday = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");

        //店铺收藏
        Integer favStoreT = this.getFavData(today, null, 10, shopSupplierId);
        Integer favStoreY = this.getFavData(yesterday, null, 10, shopSupplierId);
        //商品收藏
        Integer favProductT = this.getFavData(today, null, 20, shopSupplierId);
        Integer favProductY = this.getFavData(yesterday, null, 20, shopSupplierId);
        //访客数
        Integer visitUserT = this.getVisitUserData(today, null, shopSupplierId);
        Integer visitUserY = this.getVisitUserData(yesterday, null, shopSupplierId);
        //访问量
        Integer visitTotalT = this.getVisitData(today, null, shopSupplierId);
        Integer visitTotalY = this.getVisitData(yesterday, null, shopSupplierId);

        JSONObject result = new JSONObject();
        result.put("favStoreT",favStoreT);
        result.put("favStoreY",favStoreY);

        result.put("favProductT",favProductT);
        result.put("favProductY",favProductY);

        result.put("visitUserT",visitUserT);
        result.put("visitUserY",visitUserY);

        result.put("visitTotalT",visitTotalT);
        result.put("visitTotalY",visitTotalY);
        return result;
    }

    /**
     * 通过时间段查询访问量
     * @param startDate,endDate
     * @return
     */
    public Map<String, Object> getVisitByDate(String startDate, String endDate, Integer shopSupplierId) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Date startTime = DateUtil.parse(startDate);
        Date endTime = DateUtil.parse(endDate);
        //endTime加一天
        endTime = DateUtil.offsetDay(endTime,1);
        List<JSONObject> data = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (Date t = startTime; t.before(endTime); t = DateUtil.offsetDay(t,1)) {
            String day = DateUtil.format(t, "yyyy-MM-dd");
            Integer favStore = this.getFavData(day, null, 10, shopSupplierId);
            Integer favProduct = this.getFavData(day, null, 20, shopSupplierId);
            Integer visitUser = this.getVisitUserData(day, null, shopSupplierId);
            Integer visitTotal = this.getVisitData(day, null, shopSupplierId);

            JSONObject json = new JSONObject();
            json.put("day", day);
            json.put("favStore", favStore);
            json.put("favProduct", favProduct);
            json.put("visitUser", visitUser);
            json.put("visitTotal", visitTotal);
            days.add(day);
            data.add(json);
        }
        map.put("data", data);
        map.put("days", days);
        return map;
    }

    /**
     * 通过时间段查询用户数据
     */
    public Map<String, Object> getFavDataByDate(String startDate, String endDate, Integer shopSupplierId) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Date startTime = DateUtil.parse(startDate);
        Date endTime = DateUtil.parse(endDate);
        //endTime加一天
        endTime = DateUtil.offsetDay(endTime,1);
        List<JSONObject> data = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (Date t = startTime; t.before(endTime); t = DateUtil.offsetDay(t,1)) {
            String day = DateUtil.format(t, "yyyy-MM-dd");
            Integer favStore = this.getFavData(day, null, 10, shopSupplierId);
            Integer favProduct = this.getFavData(day, null, 20, shopSupplierId);

            JSONObject json = new JSONObject();
            json.put("day", day);
            json.put("supplierCount", favStore);
            json.put("productCount", favProduct);
            days.add(day);
            data.add(json);
        }
        map.put("data", data);
        map.put("days", days);
        return map;
    }
}
