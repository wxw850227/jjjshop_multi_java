<template>

  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="150px">
      <!--添加门店-->
      <div class="common-form">短信通知（阿里云短信）</div>
      <el-form-item label="AccessKey">
        <el-input v-model="form.aliyunSms.accessKeyId" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="AccessKeySecret">
        <el-input v-model="form.aliyunSms.accessKeySecret" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="短信签名">
        <el-input v-model="form.aliyunSms.sign" class="max-w460"></el-input>
      </el-form-item>
      <div class="common-form">商家手机号设置（用于接收消息提醒）</div>
      <el-form-item label="接收手机号">
        <el-input v-model="form.acceptPhone" class="max-w460"></el-input>
      </el-form-item>
      <div class="common-form">注册短信验证码模板（不填则表示不支持h5登录）</div>
      <el-form-item label="短信模板">
        <el-input v-model="form.templateCode" class="max-w460"></el-input>
      </el-form-item>

      <!--提交-->
      <div class="common-button-wrapper">
          <el-button type="primary" @click="onSubmit" :loading="loading">提交</el-button>
      </div>
    </el-form>
  </div>

</template>

<script>
  import SettingApi from '@/api/setting.js';

  export default {
    data() {
      return {
        /*form表单数据*/
        form: {
          aliyunSms:{}
        },
        loading: false,
      };
    },
    created() {
      this.getData()
    },

    methods: {
      getData() {
        let self = this;
        SettingApi.smsDetail({}, true)
          .then(res => {
            self.form = res.data;
            self.form.aliyunSms=res.data.engine.aliyun;
          })
          .catch(error => {});

      },
      //提交表单
      onSubmit() {
        let self = this;
        self.loading = true;
        let params = this.form;
        SettingApi.editSms(params, true)
          .then(data => {
            self.loading = false;
            ElMessage({
              message: '恭喜你，短信通知设置成功',
              type: 'success'
            });

          })
          .catch(error => {
            self.loading = false;
          });
      },
      //发送短信
      sendOut() {
        let self = this;
        let params = this.form;
        SettingApi.sendSms(params, true)
          .then(data => {
            ElMessage({
              message: '恭喜你，短信发送成功',
              type: 'success'
            });
            self.$router.push('/setting/Sms');

          })
          .catch(error => {

          });
      }



    }

  };
</script>

<style>
  .tips {
    color: #ccc;
  }
</style>
