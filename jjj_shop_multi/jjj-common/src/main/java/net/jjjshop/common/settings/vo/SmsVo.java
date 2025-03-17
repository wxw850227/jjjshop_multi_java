package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("短信设置VO")
public class SmsVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String acceptPhone;
    private String templateCode;
    private AliyunSms aliyunSms;


    @Data
    @Accessors(chain = true)
    @ApiModel("阿里云短信VO")
    public static class AliyunSms implements Serializable {
        private static final long serialVersionUID = 1L;
        private String accessKeyId;
        private String accessKeySecret;
        private String sign;

        public AliyunSms() {
            this.accessKeyId = "";
            this.accessKeySecret = "";
            this.sign = "";
        }
    }

    public SmsVo() {
        this.acceptPhone = "";
        this.templateCode = "";
        this.aliyunSms = new AliyunSms();
    }
}
