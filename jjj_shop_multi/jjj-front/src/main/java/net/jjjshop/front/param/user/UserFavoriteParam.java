package net.jjjshop.front.param.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
@ApiModel(value = "CategoryParam对象", description = "Category新增修改参数")
public class UserFavoriteParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer pid;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("用户Id")
    private Integer userId;

}
