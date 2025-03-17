<template>
  <div>
    <!--form表单-->
    <el-form size="small" ref="formData" :model="formData" label-width="150px">
      <div class="common-form">支付宝</div>
      <el-form-item label="支付宝姓名" :rules="[{ required: true, message: '请输入支付宝姓名' }]" prop="alipayName">
        <el-input v-model="formData.alipayName" placeholder="请输入支付宝姓名" class="max-w460"></el-input>
      </el-form-item>

      <el-form-item label="支付宝账号" :rules="[{ required: true, message: '请输入支付宝账号' }]">
        <el-input v-model="formData.alipayAccount" placeholder="请输入支付宝账号" class="max-w460"></el-input>
      </el-form-item>
      <div class="common-form">银行</div>
      <el-form-item label="开户行名称" :rules="[{ required: true, message: '请输入开户行名称' }]">
        <el-input v-model="formData.bankName" placeholder="请输入开户行名称" class="max-w460"></el-input>
      </el-form-item>

      <el-form-item label="银行开户名" :rules="[{ required: true, message: '请输入银行开户名' }]">
        <el-input v-model="formData.bankAccount" placeholder="请输入银行开户名" class="max-w460"></el-input>
      </el-form-item>

      <el-form-item label="银行卡号" :rules="[{ required: true, message: '请输入银行卡号' }]">
        <el-input v-model="formData.bankCard" placeholder="请输入银行卡号" class="max-w460"></el-input>
      </el-form-item>
      <!-- <div class="common-form">微信</div>
      <el-form-item label="微信姓名" :rules="[{ required: true, message: '请输入微信姓名' }]">
        <el-input v-model="formData.weixin_name" placeholder="请输入微信姓名" class="max-w460"></el-input>
      </el-form-item>

      <el-form-item label="微信账号" :rules="[{ required: true, message: '请输入微信账号' }]">
        <el-input v-model="formData.weixin_account" placeholder="请输入微信账号" class="max-w460"></el-input>
      </el-form-item> -->
      <!--提交-->
      <div class="common-button-wrapper"><el-button size="small" type="primary" @click="onSubmit" :disabled="loasing_submit">提交</el-button></div>
    </el-form>

    <!--上传图片-->
    <Upload v-if="isupload" :isupload="isupload" :config="{ total: 3 }" @returnImgs="returnImgsFunc"></Upload>
  </div>
</template>

<script>
import cashApi from '@/api/cash.js';
import Upload from '@/components/file/Upload.vue';
export default {
  components: {
    Upload
  },
  data() {
    return {
      /*是否正在加载*/
      loading: true,
      /*是否在提交*/
      loasing_submit: false,
      /*表单数据对象*/
      formData: {
        alipayName:'',
        alipayAccount:'',
        bankName:'',
        bankAccount:'',
        bankCard:'',
        weixinName:'',
        weixinAccount:''
      },
      app_id:10001,
      /*是否打开图片选择*/
      isupload: false,
      /*当前图片类别*/
      img_type: null
    };
  },
  created() {
    this.getParams();
  },
  methods: {
    /*获取配置数据*/
    getParams() {
      let self = this;
      cashApi
        .getAccount({
          app_id:10001
        }, true)
        .then(res => {
          if (res.code == 1) {
            self.formData = res.data;
            self.loading = false;
          } else {
            console.log();
          }
        })
        .catch(error => {});
    },

    /*提交*/
    onSubmit() {
      let self = this;
      let params = this.formData;
      params.app_id = self.app_id;
      self.$refs.formData.validate(valid => {
        if (valid) {
          self.loasing_submit = true;
          cashApi
            .setAccount(params, true)
            .then(data => {
              self.loasing_submit = false;
              ElMessage({
                message: '恭喜你，保存设置成功',
                type: 'success'
              });
            })
            .catch(error => {
              self.loasing_submit = false;
            });
        }
      });
    },

    /*选择图片*/
    chooseImg(e) {
      this.img_type = e;
      this.isupload = true;
    },

    /*关闭选择图片*/
    returnImgsFunc(e) {
      this.isupload = false;
      if (this.img_type != null) {
        this.formData[this.img_type + '_image'] = e[0].file_path;
        this.formData[this.img_type + '_id'] = e[0].file_id;
        this.img_type = null;
      }
    }
  }
};
</script>

<style></style>
