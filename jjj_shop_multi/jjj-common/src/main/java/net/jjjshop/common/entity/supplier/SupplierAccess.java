package net.jjjshop.common.entity.supplier;

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
 * 商家用户权限表
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_supplier_access")
@ApiModel(value = "SupplierAccess对象")
public class SupplierAccess implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "access_id", type = IdType.INPUT)
    private Integer accessId;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("路由地址")
    private String path;

    @ApiModelProperty("父级id")
    private Integer parentId;

    @ApiModelProperty("排序(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("重定向名称")
    private String redirectName;

    @ApiModelProperty("是否是路由 0=不是1=是")
    private Integer isRoute;

    @ApiModelProperty("是否是菜单 0不是 1是")
    private Integer isMenu;

    @ApiModelProperty("别名(废弃)")
    private String alias;

    @ApiModelProperty("是否显示1=显示0=不显示")
    private Integer isShow;

    @ApiModelProperty("插件分类id")
    private Integer plusCategoryId;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("app_id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
