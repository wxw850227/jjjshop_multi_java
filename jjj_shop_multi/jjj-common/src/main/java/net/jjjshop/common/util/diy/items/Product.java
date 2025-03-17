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
 * 商品组
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("product")
public class Product implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public Product(String imagePath){
        this.item = new DiyItem();
        item.setName("商品组");
        item.setType("product");
        item.setGroup("shop");
        // 参数
        JSONObject params = new JSONObject();
        params.put("source", "auto"); // choice; auto
        JSONObject auto = new JSONObject();
        auto.put("category", 0);
        auto.put("productSort", "all"); // all; sales; price
        auto.put("showNum", 6);
        params.put("auto", auto);
        item.setParams(params);

        // 样式
        JSONObject style = new JSONObject();
        style.put("paddingTop", 1);
        style.put("paddingBottom", 10);
        style.put("paddingLeft", 10);
        style.put("topRadio", 5);
        style.put("bottomRadio", 8);
        style.put("bgcolor", "#F2f2f2");
        style.put("background", "#Ffffff");
        style.put("display", "list"); // list; slide
        style.put("column", 2);
        style.put("productNameColor", "#333333");
        style.put("productPriceColor", "#333333");
        style.put("productLineColor", "#999999");
        style.put("productLineprice", 1);
        style.put("productPrice", 1);
        style.put("productName", 1);
        JSONObject show = new JSONObject();
        show.put("productName", 1);
        show.put("productPrice", 1);
        show.put("linePrice", 1);
        show.put("sellingPoint", 0);
        show.put("productSales", 0);
        style.put("show", show);
        item.setStyle(style);


        JSONObject itemData = new JSONObject();
        itemData.put("productName", "此处显示商品名称");
        itemData.put("image", imagePath + "image/diy/product/01.png");
        itemData.put("productPrice", "99.00");
        itemData.put("linePrice", "139.00");
        itemData.put("sellingPoint", "此款商品美观大方 不容错过");
        itemData.put("productSales", "100");

        // 默认数据
        JSONArray defaultData = new JSONArray();
        // 4条数据
        defaultData.add(itemData);
        defaultData.add(itemData);
        defaultData.add(itemData);
        defaultData.add(itemData);
        item.setDefaultData(defaultData);

        // 默认数据
        JSONArray data = new JSONArray();
        // 2条数据
        data.add(itemData);
        data.add(itemData);
        item.setData(data);

    }
}
