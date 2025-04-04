

package net.jjjshop.framework.shiro.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.shop.ShopAccess;
import net.jjjshop.common.entity.supplier.SupplierAccess;

import java.util.List;

/**
 * 登录用户对象，响应给前端
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LoginSupplierAccessVo extends SupplierAccess {

    private static final long serialVersionUID = -1758338570596088158L;

    @ApiModelProperty("子权限")
    private List<LoginSupplierAccessVo> children;

}
