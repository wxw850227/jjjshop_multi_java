package net.jjjshop.front.vo.page;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel("广告图VO")
public class AdVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Integer adId;

    @ApiModelProperty("广告名称")
    private String title;

    @ApiModelProperty("图片路径")
    private String filePath;

    @ApiModelProperty("跳转链接")
    private String linkUrl;

    @ApiModelProperty("商户id")
    private Integer shopSupplierId;

    @ApiModelProperty("1显示0隐藏")
    private Boolean status;


}
