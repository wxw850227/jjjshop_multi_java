package net.jjjshop.shop.vo.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.supplier.SupplierApply;
import net.jjjshop.common.entity.supplier.SupplierCash;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户申请VO")
public class SupplierApplyVo extends SupplierApply {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("营业执照")
    private String businessImage;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("用户昵称")
    private String nickName;

}
