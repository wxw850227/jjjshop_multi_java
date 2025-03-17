<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="250px">

      <div class="common-form">支付宝支付设置</div>
      <el-form-item label="是否开启" prop="customer">
        <el-checkbox v-model="form.is_open">是否开启支付宝支付</el-checkbox>
      </el-form-item>
      <el-form-item label="支付宝 appid">
        <el-input v-model="form.app_id" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="支付宝公钥">
        <el-input type="textarea" :rows="4" v-model="form.publicKey" class="max-w460"></el-input>
      </el-form-item>
      <el-form-item label="应用私钥">
        <el-input type="textarea" :rows="4" v-model="form.privateKey" class="max-w460"></el-input>
      </el-form-item>

      <!--提交-->
      <div class="common-button-wrapper">
          <el-button type="primary" @click="onSubmit">提交</el-button>
      </div>
    </el-form>

  </div>

</template>

<script>
  import AppSettingApi from '@/api/appsetting.js';
  export default {
    data() {
      return {
        /*form表单数据*/
        form: {
        },
        /*是否打开图片选择*/
        isupload:false
      };
    },
    created() {
      this.getData()
    },

    methods: {
      getData() {
        let self = this;
        AppSettingApi.h5AlipayDetail({}, true)
          .then(data => {
            if(data.data.data != null){
              self.form = data.data.data;
            }
          })
          .catch(error => {});

      },
      //提交表单
      onSubmit() {
        let self = this;
        let params = this.form;
        AppSettingApi.editH5Alipay(params, true)
          .then(data => {
            ElMessage({
              message: '恭喜你，设置成功',
              type: 'success'
            });
          })
          .catch(error => {

          });
      },
    }

  };
</script>

<style>
  .tips {
    color: #ccc;
  }
</style>
