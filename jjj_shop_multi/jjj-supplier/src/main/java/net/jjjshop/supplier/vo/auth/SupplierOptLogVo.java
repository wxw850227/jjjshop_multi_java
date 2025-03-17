package net.jjjshop.supplier.vo.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户操作日志VO")
public class SupplierOptLogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer optLogId;

    @ApiModelProperty("用户id")
    private Integer supplierUserId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("访问url")
    private String url;

    @ApiModelProperty("请求类型")
    private String requestType;

    @ApiModelProperty("浏览器")
    private String browser;

    @ApiModelProperty("浏览器信息")
    private String agent;

    @ApiModelProperty("操作内容")
    private String content;

    @ApiModelProperty("商户id")
    private Integer shopSupplierId;

    @ApiModelProperty("登录ip")
    private String ip;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("真实名称")
    private String realName;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
