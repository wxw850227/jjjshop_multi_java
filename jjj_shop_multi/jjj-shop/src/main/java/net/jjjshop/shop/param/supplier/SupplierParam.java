package net.jjjshop.shop.param.supplier;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel(value = "SupplierParam对象", description = "商户参数对象")
public class SupplierParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer shopSupplierId;

    @ApiModelProperty("联系人")
    private String linkName;

    @ApiModelProperty("联系电话")
    private String linkPhone;

    @ApiModelProperty("logo")
    private Integer logoId;

    @ApiModelProperty("联系地址")
    private String address;

    @ApiModelProperty("营业执照")
    private Integer businessId;

    @ApiModelProperty("商家介绍")
    private String description;

    @ApiModelProperty("供应商姓名")
    private String name;

    @ApiModelProperty("在线客服开关0，不开启1，开启")
    private Integer openService;

    @ApiModelProperty("主营分类id")
    private Integer categoryId;

    @ApiModelProperty("绑定的用户id")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("登录密码")
    private String password;

    @ApiModelProperty("登录密码")
    private String confirmPassword;
}
