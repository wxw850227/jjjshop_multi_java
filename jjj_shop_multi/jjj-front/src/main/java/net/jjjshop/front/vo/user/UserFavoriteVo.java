package net.jjjshop.front.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.user.UserFavorite;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("用户收藏")
public class UserFavoriteVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer favoriteId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("商品/店铺id")
    private Integer pid;

    @ApiModelProperty("10店铺20商品")
    private Integer type;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("商品")
    private Integer productId;

    @ApiModelProperty("图片")
    private String productImage;

    @ApiModelProperty("商品名字")
    private String productName;

    @ApiModelProperty("商品销量")
    private Integer productSales;

    @ApiModelProperty("商品价格")
    private BigDecimal productPrice;

    @ApiModelProperty("商品价格")
    private BigDecimal linePrice;
}
