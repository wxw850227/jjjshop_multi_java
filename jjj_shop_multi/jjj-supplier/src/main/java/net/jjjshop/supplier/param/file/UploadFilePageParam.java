package net.jjjshop.supplier.param.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

/**
 * 文件分页查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UploadFilePageParam对象", description = "文件分页查询参数")
public class UploadFilePageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分组id")
    private Integer groupId;

    @ApiModelProperty("文件类型")
    private String fileType;
}
