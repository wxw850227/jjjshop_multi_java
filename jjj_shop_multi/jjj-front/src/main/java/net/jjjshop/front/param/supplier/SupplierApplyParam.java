package net.jjjshop.front.param.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SupplierApplyParam对象", description = "供应商申请参数")
public class SupplierApplyParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("营业执照")
    private Integer businessId;

    @ApiModelProperty("会员id")
    private Integer userId;

    @ApiModelProperty("主营分类id")
    private Integer categoryId;

    @ApiModelProperty("程序id")
    private Integer appId;

}
