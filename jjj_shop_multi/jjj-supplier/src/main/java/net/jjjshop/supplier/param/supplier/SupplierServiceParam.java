package net.jjjshop.supplier.param.supplier;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "SupplierServiceParam对象", description = "商户服务保障对象")
public class SupplierServiceParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer serviceId;

    @ApiModelProperty("供应商用户id")
    private Integer shopSupplierId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("资金流动类型 (10微信，qq等 20在线客户)")
    private Integer serviceType;

    @ApiModelProperty("微信号")
    private String wechat;

    @ApiModelProperty("qq")
    private String qq;

    @ApiModelProperty("客服电话")
    private String phone;
}
