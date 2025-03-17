package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.OperateTypeEnum;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("系统设置VO")
public class StoreVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private List<Integer> deliveryType;
    private Boolean isGetLog;
    private KuaiDi100 kuaiDi100;
    private Policy policy;
    //押金
    private Integer supplierCash;
    //抽成比例
    private Integer commissionRate;
    //模式类型，B2B B2B2C
    private Integer operateType;
    //新商品是否需要审核
    private Integer addAudit;
    // 审核通过后再修改是否需要审核
    private Integer editAudit;
    // 供应商入驻图片
    private String supplierImage;
    // 是否开启短信
    private Integer smsOpen;
    private Integer storeOpen;


    @Data
    @Accessors(chain = true)
    @ApiModel("快递100VO")
    public static class KuaiDi100 implements Serializable{
        private static final long serialVersionUID = 1L;
        private String customer;
        private String key;
        public KuaiDi100(){
            this.customer = "";
            this.key = "";
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("隐私政策")
    public static class Policy implements Serializable{
        private static final long serialVersionUID = 1L;
        private String service;
        private String privacy;
        public Policy(){
            this.service = "";
            this.privacy = "";
        }
    }

    public StoreVo(){
        this.name = "玖玖珈商城";
        this.deliveryType = DeliveryTypeEnum.getValues();
        this.isGetLog = false;
        this.kuaiDi100 = new KuaiDi100();
        this.policy = new Policy();
        this.supplierCash = 0;
        this.commissionRate = 5;
        this.operateType = OperateTypeEnum.B2B.getValue();
        this.addAudit = 1;
        this.supplierImage = "";
        this.smsOpen = 1;
        this.storeOpen = 1;
        this.editAudit = 1;
    }
}
