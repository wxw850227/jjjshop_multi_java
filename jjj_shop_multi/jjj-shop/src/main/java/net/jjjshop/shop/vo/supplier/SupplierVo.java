package net.jjjshop.shop.vo.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.supplier.Supplier;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户VO")
public class SupplierVo extends Supplier {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("logo图片路径")
    private String logoFilePath;

    @ApiModelProperty("logo图片路径")
    private String businessFilePath;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String avatarurl;
}
