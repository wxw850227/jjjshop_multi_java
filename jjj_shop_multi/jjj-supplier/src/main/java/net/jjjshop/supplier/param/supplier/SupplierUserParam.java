

package net.jjjshop.supplier.param.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SupplierUser对象", description = "商户用户参数对象")
public class SupplierUserParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer supplierUserId;

    @ApiModelProperty("绑定的用户id")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("登录密码")
    private String password;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("用户角色")
    private List<Integer> roleId;
}
