package net.jjjshop.supplier.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierService;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品分类VO")
public class SupplierServiceVo extends SupplierService {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("logo图片路径")
    private Boolean serviceOpen;

    @ApiModelProperty("logo图片路径")
    private Integer userId;

}
