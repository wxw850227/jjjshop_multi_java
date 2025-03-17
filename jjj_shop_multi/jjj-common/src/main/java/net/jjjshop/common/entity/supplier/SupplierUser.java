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
 * 商家用户记录表
 *
 * @author jjjshop
 * @since 2022-10-17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_supplier_user")
@ApiModel(value = "SupplierUser对象")
public class SupplierUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "supplier_user_id", type = IdType.AUTO)
    private Integer supplierUserId;

    @ApiModelProperty("绑定的用户id")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("盐值")
    private String salt;

    @ApiModelProperty("登录密码")
    private String password;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("是否为超级管理员0不是,1是")
    private Integer isSuper;

    @ApiModelProperty("关联供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("0=显示1=伪删除")
    private Integer isDelete;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
