

package net.jjjshop.supplier.param.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProductParam对象", description = "商品查询参数")
public class ProductPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("tab栏选中的类型")
    private String type;

    @ApiModelProperty("分类id")
    private Integer categoryId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("是否参与分销0否1是")
    private Integer isAgent;

    @ApiModelProperty("是否参与满减0否1是")
    private Integer isJoin;

    @ApiModelProperty("供应商Id")
    private Integer shopSupplierId;

}
