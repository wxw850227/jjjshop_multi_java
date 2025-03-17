package net.jjjshop.shop.param.supplier;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel(value = "SupplierCategoryParam对象", description = "商户分类参数对象")
public class SupplierCategoryParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类型id")
    private Integer categoryId;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("保证金")
    private BigDecimal depositMoney;
}
