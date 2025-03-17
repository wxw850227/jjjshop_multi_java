

package net.jjjshop.front.param.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SupplierParam对象", description = "供应商参数对象")
public class SupplierParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("访客id")
    private String visitcode;

    @ApiModelProperty("供应商Id")
    private Integer shopSupplierId;

}
