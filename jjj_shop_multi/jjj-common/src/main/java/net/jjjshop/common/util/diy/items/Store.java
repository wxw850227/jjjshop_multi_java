package net.jjjshop.common.util.diy.items;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.util.diy.DiyItem;

/**
 * 线下门店
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("store")
public class Store implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public Store(String imagePath){
        this.item = new DiyItem();
        item.setName("线下门店");
        item.setType("store");
        item.setGroup("shop");

        // 参数
        JSONObject params = new JSONObject();
        params.put("source", "auto"); // choice; auto
        JSONObject auto = new JSONObject();
        auto.put("showNum", 6);
        params.put("auto", auto);
        item.setParams(params);

        // 样式
        JSONObject style = new JSONObject();
        style.put("background", "#FFFFFF");
        style.put("bgcolor", "#f2f2f2");
        style.put("paddingTop", 0);
        style.put("paddingBottom", 10);
        style.put("paddingLeft", 10);
        style.put("topRadio", 5);
        style.put("bottomRadio", 5);
        item.setStyle(style);

        JSONObject itemData = new JSONObject();
        itemData.put("shopName", "此处显示门店名称");
        itemData.put("logoImage", imagePath + "image/diy/circular.png");
        itemData.put("phone", "010-6666666");
        itemData.put("province", "xx省");
        itemData.put("city", "xx市");
        itemData.put("region", "xx区");
        itemData.put("address", "xx街道");

        // 默认数据
        JSONArray defaultData = new JSONArray();
        // 2条数据
        defaultData.add(itemData);
        defaultData.add(itemData);
        item.setDefaultData(defaultData);

        // 默认数据
        JSONArray data = new JSONArray();
        // 1条数据
        data.add(itemData);
        item.setData(data);



    }
}
