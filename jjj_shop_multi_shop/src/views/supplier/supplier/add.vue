<template>
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <!--添加店员-->
      <div class="common-form">添加商户</div>
      <el-form-item
        label="商户名称"
        prop="supplier.name"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          class="max-w460"
          v-model="form.supplier.name"
          placeholder="请输入商户名称"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="账号"
        prop="supplier.userName"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          class="max-w460"
          v-model="form.supplier.userName"
          placeholder="请输入账号"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="登录密码"
        prop="supplier.password"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          v-model="form.supplier.password"
          placeholder="请输入登录密码"
          type="password"
          class="max-w460"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="确认密码"
        prop="supplier.confirmPassword"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          v-model="form.supplier.confirmPassword"
          placeholder="请输入确认密码"
          type="password"
          class="max-w460"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="联系人"
        prop="supplier.linkName"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          class="max-w460"
          v-model="form.supplier.linkName"
          placeholder="请输入联系人"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="联系电话"
        prop="supplier.linkPhone"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          class="max-w460"
          v-model="form.supplier.linkPhone"
          placeholder="请输入联系电话"
        ></el-input>
      </el-form-item>
      <el-form-item label="门店logo">
        <el-row>
          <el-button icon="Picture" @click="openUpload('logo')"
            >选择图片</el-button
          >
          <div v-if="form.supplier.logoId != ''" class="img">
            <img :src="logoFilePath" width="100" height="100" />
          </div>
        </el-row>
        <div class="gray9">建议图片尺寸为150x150</div>
      </el-form-item>
      <el-form-item
        label="主营类别"
        prop="supplier.categoryId"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-select v-model="form.supplier.categoryId" placeholder="请选择">
          <el-option
            v-for="(item, index) in category"
            :key="index"
            :label="item.name"
            :value="item.categoryId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店铺类型" :label-width="formLabelWidth">
        <el-radio-group v-model="form.supplier.storeType">
          <el-radio :label="10">普通</el-radio>
          <el-radio :label="20">自营</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        label="地址"
        prop="supplier.address"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          class="max-w460"
          v-model="form.supplier.address"
          placeholder="请输入地址"
        ></el-input>
      </el-form-item>
      <el-form-item label="营业执照">
        <el-row>
          <el-button icon="Picture" @click="openUpload('business')"
            >选择图片</el-button
          >
          <div v-if="form.supplier.businessId != ''" class="img">
            <img :src="businessFilePath" width="100" height="100" />
          </div>
        </el-row>
      </el-form-item>
      <el-form-item label="绑定用户">
        <el-row>
          <el-button icon="picture" @click="userClick()">选择用户</el-button>
          <div v-if="form.supplier.userId != 0" class="img">
            <img :src="userInfo.avatarUrl" width="100" height="100" />
          </div>
        </el-row>
      </el-form-item>
      <el-form-item label="商家介绍" prop="supplier.description">
        <el-input
          type="textarea"
          v-model="form.supplier.description"
          class="max-w460"
        ></el-input>
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button size="small" type="info" @click="cancelFunc">取消</el-button>
        <el-button
          size="small"
          type="primary"
          @click="onSubmit"
          :loading="loading"
          >提交</el-button
        >
      </div>
    </el-form>

    <!--上传图片组件-->
    <Upload
      v-if="isupload"
      :isupload="isupload"
      :type="type"
      @returnImgs="returnImgsFunc"
      >上传图片</Upload
    >
    <!--管理用户-->
    <GetUser
      :is_open="open_user"
      @close="closeDialogFunc($event, 'add')"
    ></GetUser>
  </div>
</template>

<script>
import SupplierApi from "@/api/supplier.js";
import Upload from "@/components/file/Upload.vue";
import GetUser from "@/components/user/GetUser.vue";
export default {
  components: {
    /*上传组件*/
    Upload,
    /*选择用户*/
    GetUser,
  },
  data() {
    return {
      /*form表单数据*/
      form: {
        supplier: {
          logoId: 0,
          businessId: 0,
          openService: 0,
          categoryId: "",
          userId: 0,
          storeType: 10,
        },
      },
      logoFilePath: "",
      businessFilePath: "",
      /*是否打开添加弹窗*/
      open_add: false,
      storeList: [],
      category: [],
      /*是否上传图片*/
      isupload: false,
      loading: false,
      type: "logo",
      open_user: false,
      userInfo: {},
    };
  },
  created() {
    /*获取列表*/
    this.getCategoryList();
  },
  methods: {
    /*获取广告类别*/
    getCategoryList() {
      let self = this;
      let Params = {};

      SupplierApi.toaddSupplier(Params, true)
        .then((res) => {
          self.category = res.data;
          if (self.category.length > 0) {
            self.form.supplier.categoryId = self.category[0].categoryId;
          }
        })
        .catch((error) => {});
    },
    onSubmit() {
      let self = this;
      let form = self.form;
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          SupplierApi.addSupplier(form.supplier, true)
            .then((data) => {
              self.loading = false;
              if (data.code == 1) {
                ElMessage({
                  message: "恭喜你，商户添加成功",
                  type: "success",
                });
                self.$router.push("/supplier/supplier/index");
              } else {
                self.loading = false;
              }
            })
            .catch((error) => {
              self.loading = false;
            });
        }
      });
    },

    /*上传*/
    openUpload(e) {
      this.type = e;
      this.isupload = true;
    },

    /*获取图片*/
    returnImgsFunc(e) {
      if (e != null && e.length > 0) {
        if (this.type == "logo") {
          this.logoFilePath = e[0].filePath;
          this.form.supplier.logoId = e[0].fileId;
        } else if (this.type == "business") {
          this.businessFilePath = e[0].filePath;
          this.form.supplier.businessId = e[0].fileId;
        }
      }
      this.isupload = false;
    },

    /*取消*/
    cancelFunc() {
      this.$router.back(-1);
    },
    /*打开弹出层*/
    userClick() {
      this.open_user = true;
    },

    /*关闭获取用户弹窗*/
    closeDialogFunc(e) {
      if (e.type != "error") {
        this.userInfo = e.params[0];
        this.form.supplier.userId = e.params[0].userId;
      }
      this.open_user = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.basic-setting-content {
}

.product-add {
  padding-bottom: 50px;
}

.img {
  margin-top: 10px;
}
</style>
