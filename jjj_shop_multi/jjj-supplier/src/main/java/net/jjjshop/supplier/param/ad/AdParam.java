package net.jjjshop.supplier.param.ad;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "AdParam对象", description = "广告参数对象")
public class AdParam implements Serializable {
    private final static long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Integer adId;

    @ApiModelProperty("广告名称")
    private String title;

    @ApiModelProperty("图片id")
    private Integer imageId;

    @ApiModelProperty("排序(越小越靠前)")
    private Integer sort;

    @ApiModelProperty("跳转链接")
    private String linkUrl;

    @ApiModelProperty("链接名称")
    private String name;

    @ApiModelProperty("banner类型id")
    private Integer categoryId;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("商户id")
    private Integer shopSupplierId;

    @ApiModelProperty("1显示0隐藏")
    private Integer status;

}
