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
 * 管理员登录记录表
 *
 * @author jjjshop
 * @since 2022-10-31
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_supplier_login_log")
@ApiModel(value = "SupplierLoginLog对象")
public class SupplierLoginLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "login_log_id", type = IdType.AUTO)
    private Integer loginLogId;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("登录ip")
    private String ip;

    @ApiModelProperty("登录结果")
    private String result;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
