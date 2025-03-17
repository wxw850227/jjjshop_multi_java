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
 * 供应商提现账号表
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_supplier_account")
@ApiModel(value = "SupplierAccount对象")
public class SupplierAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "account_id", type = IdType.AUTO)
    private Integer accountId;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("支付宝姓名")
    private String alipayName;

    @ApiModelProperty("支付宝账号")
    private String alipayAccount;

    @ApiModelProperty("开户行名称")
    private String bankName;

    @ApiModelProperty("银行开户名")
    private String bankAccount;

    @ApiModelProperty("银行卡号")
    private String bankCard;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
