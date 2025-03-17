package net.jjjshop.shop.vo.link;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("超链接Vo")
public class LinkVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("跳转链接")
    private String url;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("种类")
    private String type;

}
