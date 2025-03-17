package net.jjjshop.supplier.vo.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.supplier.SupplierCash;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户提现Vo")
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

    @ApiModelProperty("申请状态 (10待审核 20审核通过 30驳回 40已打款)")
    private String applyStatusText;

}
