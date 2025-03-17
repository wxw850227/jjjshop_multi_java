<template>
    <div>
      <!--form表单-->
      <el-form size="small" ref="formData" :model="formData" label-width="150px">
        <el-form-item label="客服类型">
          <div>
            <el-radio v-model="formData.serviceType" :label="10">聊天工具</el-radio>
            <el-radio v-model="formData.serviceType" :label="20" v-if="formData.serviceOpen==1">在线客服</el-radio>
          </div>
        </el-form-item>
        <div v-if="formData.serviceType==10">
        <el-form-item label="微信号">
          <el-input v-model="formData.wechat" placeholder="请输入微信号" class="max-w460"></el-input>
        </el-form-item>
        <el-form-item label="qq">
          <el-input v-model="formData.qq" placeholder="请输入qq号" class="max-w460"></el-input>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="formData.phone" placeholder="请输入联系电话" class="max-w460"></el-input>
        </el-form-item>
        </div>
        <el-form-item label="绑定客服" v-if="formData.serviceType==20" :rules="[{required: true,message: '请输入绑定用户id'}]" prop="userId">
          <el-input type="number" v-model="formData.userId" placeholder="请输入用户id" class="max-w460" :disabled="userId>0"></el-input>
          <p class="gray">请在商城小程序个人中心查看用户ID</p>
        </el-form-item>
        <!--提交-->
        <div class="common-button-wrapper">
          <el-button size="small" type="primary" @click="onSubmit" :disabled="loading">提交</el-button>
        </div>
      </el-form>
    </div>
  </template>

  <script>
    import SettingApi from '@/api/setting.js';
    export default{
      data(){
        return {
          /*是否在提交*/
          loading:false,
          /*表单数据对象*/
          formData:{
            serviceType: 10,
            wechat:'',
            qq:'',
            phone:'',
            serviceOpen:'',
            userId:''
          },
        }
      },
      created() {
        this.getData();
      },
      methods:{
        /*获取配置数据*/
        getData() {
          let self = this;
          SettingApi.getService({}, true).then(res => {
              if(res.data != null){
                self.formData = res.data;
                self.userId = res.data.userId;
              }
            })
            .catch(error => {

            });
        },

        /*提交*/
        onSubmit() {
          let self = this;
          let params = this.formData;
          self.$refs.formData.validate((valid) => {
            if (valid) {
              self.loading = true;
              SettingApi.setService(params, true)
                .then(data => {
                  self.loading = false;
                  ElMessage({
                    message: '恭喜你，保存设置成功',
                    type: 'success'
                  });
                })
                .catch(error => {
                  self.loading = false;
                });
            }
          });
        },
      }
    }
  </script>

  <style>
  </style>
