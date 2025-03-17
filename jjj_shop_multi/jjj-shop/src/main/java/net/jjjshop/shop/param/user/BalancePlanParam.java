

package net.jjjshop.shop.param.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

import java.util.Date;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BalancePlanParam对象", description = "余额充值查询参数")
public class BalancePlanParam extends BasePageOrderParam {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer planId;

    @ApiModelProperty("套餐名称")
    private String planName;

    @ApiModelProperty("充值金额")
    private Integer money;

    @ApiModelProperty("赠送金额")
    private Integer giveMoney;

    @ApiModelProperty("到账金额")
    private Integer realMoney;

    @ApiModelProperty("排序(数字越小越靠前)")
    private Integer sort;

}
