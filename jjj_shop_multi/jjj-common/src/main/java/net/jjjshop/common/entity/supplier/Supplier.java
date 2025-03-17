package net.jjjshop.common.entity.supplier;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 供应商表
 *
 * @author jjjshop
 * @since 2022-10-17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_supplier")
@ApiModel(value = "Supplier对象")
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "shop_supplier_id", type = IdType.AUTO)
    private Integer shopSupplierId;

    @ApiModelProperty("供应商姓名")
    private String name;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("联系人")
    private String linkName;

    @ApiModelProperty("联系电话")
    private String linkPhone;

    @ApiModelProperty("logo")
    private Integer logoId;

    @ApiModelProperty("联系地址")
    private String address;

    @ApiModelProperty("营业执照")
    private Integer businessId;

    @ApiModelProperty("商家介绍")
    private String description;

    @ApiModelProperty("在线客服开关0，不开启1，开启")
    private Integer openService;

    @ApiModelProperty("总货款")
    private BigDecimal totalMoney;

    @ApiModelProperty("当前可提现金额")
    private BigDecimal money;

    @ApiModelProperty("已冻结金额")
    private BigDecimal freezeMoney;

    @ApiModelProperty("累积提现佣金")
    private BigDecimal cashMoney;

    @ApiModelProperty("保证金")
    private BigDecimal depositMoney;

    @ApiModelProperty("是否删除0，否1是")
    private Integer isDelete;

    @ApiModelProperty("会员id")
    private Integer userId;

    @ApiModelProperty("主营分类id")
    private Integer categoryId;

    @ApiModelProperty("评分")
    private String score;

    @ApiModelProperty("物流评分")
    private BigDecimal expressScore;

    @ApiModelProperty("服务评分")
    private BigDecimal serverScore;

    @ApiModelProperty("描述评分")
    private BigDecimal describeScore;

    @ApiModelProperty("资料是否齐全")
    private Integer isFull;

    @ApiModelProperty("关注人数")
    private Integer favCount;

    @ApiModelProperty("店铺状态0正常10退押金中20未交保证金")
    private Integer status;

    @ApiModelProperty("店铺类型10普通20自营")
    private Integer storeType;

    @ApiModelProperty("收到的礼物币总数")
    private Integer totalGift;

    @ApiModelProperty("账户礼物币")
    private Integer giftMoney;

    @ApiModelProperty("商品总销量")
    private Integer productSales;

    @ApiModelProperty("是否回收0否1是")
    private Integer isRecycle;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
