package net.jjjshop.supplier.param.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "SupplierSecurityParam对象", description = "商户服务保障参数对象")
public class SupplierSecurityParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer serviceSecurityId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("logo")
    private String logo;

    @ApiModelProperty("状态1开启0关闭")
    private Boolean status;
}
