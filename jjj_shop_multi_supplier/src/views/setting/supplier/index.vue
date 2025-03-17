<template>
  <div>
    <!--form表单-->
    <el-form size="small" ref="formData" :model="formData" label-width="150px">
      <el-form-item
        label="店铺名称"
        :rules="[{ required: true, message: '请输入店铺名称' }]"
      >
        <el-input
          v-model="formData.name"
          placeholder="请输入店铺名称"
          class="max-w460"
        ></el-input>
      </el-form-item>
      <!-- <el-form-item label="联系人" :rules="[{required: true,message: '请输入联系人'}]">
          <el-input v-model="formData.link_name" placeholder="请输入联系人" class="max-w460"></el-input>
        </el-form-item> -->

      <el-form-item
        label="联系电话"
        :rules="[{ required: true, message: '请输入联系电话' }]"
      >
        <el-input
          v-model="formData.linkPhone"
          placeholder="请输入联系电话"
          class="max-w460"
          v-bind:readonly="true"
        ></el-input>
      </el-form-item>

      <el-form-item
        label="联系地址"
        :rules="[{ required: true, message: '请输入联系地址' }]"
      >
        <el-input
          v-model="formData.address"
          placeholder="请输入联系地址"
          class="max-w460"
        ></el-input>
      </el-form-item>

      <el-form-item
        label="商家介绍"
        :rules="[{ required: true, message: '请输入商家介绍' }]"
      >
        <el-input
          type="textarea"
          v-model="formData.description"
          placeholder="请输入商家介绍"
          class="max-w460"
        ></el-input>
      </el-form-item>

      <el-form-item
        label="logo"
        :rules="[{ required: true, message: '选择Logo' }]"
      >
        <el-button @click="chooseImg('logo')">选择Logo</el-button>
        <div v-if="formData.logoId != ''" class="img">
          <img :src="logoFilePath" width="100" height="100" />
        </div>
        <div class="gray9">建议图片尺寸为150x150</div>
      </el-form-item>

      <!--  <el-form-item label="营业执照" :rules="[{required: true,message: '选择营业执照'}]">
          <el-button @click="chooseImg('business')">选择营业执照</el-button>
          <div v-if="formData.business_id!=''" class="img">
            <img :src="business_file_path" width="100" height="100" />
          </div>
        </el-form-item> -->

      <!--提交-->
      <div class="common-button-wrapper">
        <el-button
          size="small"
          type="primary"
          @click="onSubmit"
          :disabled="loading"
          >提交</el-button
        >
      </div>
    </el-form>

    <!--上传图片-->
    <Upload
      v-if="isupload"
      :isupload="isupload"
      @returnImgs="returnImgsFunc"
    ></Upload>
  </div>
</template>

<script>
import SettingApi from "@/api/setting.js";
import Upload from "@/components/file/Upload.vue";
export default {
  components: {
    Upload,
  },
  data() {
    return {
      /*是否在提交*/
      loading: false,
      /*表单数据对象*/
      formData: {
        name: "",
        linkName: "",
        linkPhone: "",
        address: "",
        description: "",
        logoId: 0,
        //business_id:0,
      },
      /*是否打开图片选择*/
      isupload: false,
      /*当前图片类别*/
      type: null,
      logoFilePath: "",
      businessFilePath: "",
    };
  },
  created() {
    this.getParams();
  },
  methods: {
    /*获取配置数据*/
    getParams() {
      let self = this;
      SettingApi.getSupplier({}, true)
        .then((res) => {
          self.formData = res.data;
          self.logoFilePath = res.data.logoFilePath;
          self.businessFilePath = res.data.businessFilePath;
        })
        .catch((error) => {});
    },

    /*提交*/
    onSubmit() {
      let self = this;
      let params = this.formData;
      self.$refs.formData.validate((valid) => {
        if (valid) {
          self.loading = true;
          SettingApi.setSupplier(params, true)
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "恭喜你，保存设置成功",
                type: "success",
              });
            })
            .catch((error) => {
              self.loading = false;
            });
        }
      });
    },

    /*选择图片*/
    chooseImg(e) {
      this.type = e;
      this.isupload = true;
    },

    /*关闭选择图片*/
    returnImgsFunc(e) {
      if (e != null && e.length > 0) {
        if (this.type == "logo") {
          this.logoFilePath = e[0].filePath;
          this.formData.logoId = e[0].fileId;
        } else if (this.type == "business") {
          this.businessFilePath = e[0].filePath;
          this.formData.businessId = e[0].fileId;
        }
      }
      this.isupload = false;
    },
  },
};
</script>

<style></style>
