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
 * 文章组件
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("article")
public class Article implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public Article(String imagePath){
        this.item = new DiyItem();
        item.setName("文章组");
        item.setType("article");
        item.setGroup("media");
        // 参数
        JSONObject params = new JSONObject();
        params.put("source", "auto");
        JSONObject auto = new JSONObject();
        auto.put("category", 0);
        auto.put("showNum", 2);
        params.put("auto", auto);
        item.setParams(params);

        // 样式
        JSONObject style = new JSONObject();
        style.put("display", 10);
        style.put("background", "#FFFFFF");
        style.put("bgcolor", "#F2F2F2");
        style.put("paddingTop", 1);
        style.put("paddingBottom", 10);
        style.put("paddingLeft", 10);
        style.put("topRadio", 0);
        style.put("bottomRadio", 8);
        item.setStyle(style);

        // 默认数据
        JSONArray data = new JSONArray();
        item.setData(data);

        // 默认数据
        JSONArray defaultData = new JSONArray();
        JSONObject itemData = new JSONObject();
        itemData.put("articleTitle", "此处显示文章标题");
        itemData.put("showType", 10);
        itemData.put("image", imagePath + "image/diy/article/01.png");
        itemData.put("viewsNum", 309);
        // 加2条数据
        defaultData.add(itemData);
        defaultData.add(itemData);
        item.setDefaultData(defaultData);
    }
}
