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
 * 商家用户角色表
 *
 * @author jjjshop
 * @since 2022-10-28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_supplier_role")
@ApiModel(value = "SupplierRole对象")
public class SupplierRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色id")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("排序(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("商户id")
    private Integer shopSupplierId;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
