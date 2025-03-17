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
 * 服务保障申请
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_supplier_service_apply")
@ApiModel(value = "SupplierServiceApply对象")
public class SupplierServiceApply implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "service_apply_id", type = IdType.AUTO)
    private Integer serviceApplyId;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("服务id")
    private Integer serviceSecurityId;

    @ApiModelProperty("0待审核1通过2拒绝")
    private Integer status;

    @ApiModelProperty("备注")
    private String content;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
