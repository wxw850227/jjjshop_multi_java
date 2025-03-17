

package net.jjjshop.framework.shiro.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Add;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 登录用户对象，响应给前端
 **/
@Data
@Accessors(chain = true)
public class LoginSupplierUserVo implements Serializable {

    private static final long serialVersionUID = -1758338570596088158L;

    @ApiModelProperty("主键")
    private Integer supplierUserId;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("是否为超级管理员0不是,1是")
    private Boolean isSuper;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("权限列表-后端")
    private Set<String> permissions;

    @ApiModelProperty("权限列表-前端菜单")
    private List<LoginSupplierAccessVo> menus;

}
