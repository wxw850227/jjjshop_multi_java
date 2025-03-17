package net.jjjshop.supplier.param.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "SupplierAccountParam对象", description = "商户账户信息对象")
public class SupplierAccountParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
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
}
