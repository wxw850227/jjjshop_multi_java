package net.jjjshop.common.entity.supplier;

import java.math.BigDecimal;
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
 * 商户申请表
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_supplier_apply")
@ApiModel(value = "SupplierApply对象")
public class SupplierApply implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "supplier_apply_id", type = IdType.AUTO)
    private Integer supplierApplyId;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("营业执照")
    private Integer businessId;

    @ApiModelProperty("0待审核1审核通过2未通过")
    private Integer status;

    @ApiModelProperty("会员id")
    private Integer userId;

    @ApiModelProperty("主营分类id")
    private Integer categoryId;

    @ApiModelProperty("保证金")
    private BigDecimal depositMoney;

    @ApiModelProperty("审核备注")
    private String content;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
