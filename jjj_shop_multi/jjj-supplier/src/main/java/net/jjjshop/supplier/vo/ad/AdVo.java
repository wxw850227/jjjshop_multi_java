package net.jjjshop.supplier.vo.ad;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.page.Ad;
import net.jjjshop.common.entity.store.Store;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("广告VO")
public class AdVo extends Ad {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片路径")
    private String filePath;

    @ApiModelProperty("分类名称")
    private String categoryName;
}
