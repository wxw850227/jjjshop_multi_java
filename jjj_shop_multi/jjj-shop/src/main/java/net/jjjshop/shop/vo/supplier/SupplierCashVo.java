package net.jjjshop.shop.vo.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.supplier.SupplierCash;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户提现VO")
public class SupplierCashVo extends SupplierCash {
    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty("供应商姓名")
    private String supplierName;

    @ApiModelProperty("支付类型文本")
    private String payTypeText;

}
