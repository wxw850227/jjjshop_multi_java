package net.jjjshop.common.entity.page;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * banner图
 *
 * @author jjjshop
 * @since 2022-10-25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_ad")
@ApiModel(value = "Ad对象")
public class Ad implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(value = "ad_id", type = IdType.AUTO)
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

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
