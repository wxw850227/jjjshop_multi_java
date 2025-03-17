package net.jjjshop.front.vo.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("商品页面供应商VO")
public class ProductSupplierVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer shopSupplierId;

    @ApiModelProperty("供应商姓名")
    private String name;

    @ApiModelProperty("关注人数")
    private Integer favCount;

    @ApiModelProperty("logo")
    private Integer logoId;

    @ApiModelProperty("主营分类id")
    private Integer categoryId;

    @ApiModelProperty("服务评分")
    private BigDecimal serverScore;

    @ApiModelProperty("商品总销量")
    private Integer productSales;

    @ApiModelProperty("供应商姓名")
    private String logoFilePath;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("店铺类型10普通20自营")
    private Integer storeType;

}
