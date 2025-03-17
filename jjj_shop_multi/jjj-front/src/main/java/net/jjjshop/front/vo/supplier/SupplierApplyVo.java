package net.jjjshop.front.vo.supplier;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.front.vo.product.ProductListVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("供应商申请VO")
public class SupplierApplyVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
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

    @ApiModelProperty("审核备注")
    private Integer supplierIsDelete;

}
