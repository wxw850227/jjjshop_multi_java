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
@ApiModel("供应商分类VO")
public class SupplierCategoryListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类型id")
    private Integer categoryId;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("保证金")
    private BigDecimal depositMoney;
}
