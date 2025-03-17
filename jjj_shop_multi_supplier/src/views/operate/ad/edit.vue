<template>
  <div class="product-add pb50" v-loading="loading">
    <!--编辑广告-->
    <el-form
      size="small"
      :model="form"
      ref="form"
      :rules="rules"
      label-width="100px"
    >
      <div class="common-form">编辑广告</div>
      <el-form-item label="广告标题" prop="title">
        <el-input
          v-model="form.title"
          placeholder="请输入广告标题"
          class="max-w460"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="广告图">
        <div>
          <el-button type="primary" @click="openUpload">上传图片</el-button>
          <img
            :src="path"
            class="mt10"
            :width="120"
            v-if="isImg"
            :isImg="isImg"
          />
          <div class="gray9">建议尺寸710x200,大小不超过2M</div>
          <!--上传图片组件-->
          <Upload
            v-if="isupload"
            :isupload="isupload"
            @returnImgs="returnImgsFunc"
            >上传图片</Upload
          >
        </div>
      </el-form-item>
      <el-form-item label="广告状态">
        <el-radio-group v-model="form.status">
          <el-radio :label="1">显示</el-radio>
          <el-radio :label="0">隐藏</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序"
        ><el-input
          type="number"
          v-model="form.sort"
          placeholder="请输入数字"
          class="max-w460"
        ></el-input
      ></el-form-item>
      <el-form-item label="链接" prop="image_id">
        <el-row>
          <div class="url-box flex-1">
            <!--  <span class="key-name">链接地址：</span> -->
            <el-input
              v-model="form.linkUrl"
              type="text"
              class="max-w460 mr10"
              style="width: 380px"
            ></el-input>
            <el-button type="primary" @click="changeLink()">选择链接</el-button>
            <div class="tips" id="tips" style="color: #000">{{ tips_Id }}</div>
            <el-input
              v-model="form.name"
              type="hidden"
              class="max-w460"
            ></el-input>
          </div>
          <div class="url-box ml10" style="float: right"></div>
        </el-row>
      </el-form-item>
      <Setlink
        v-if="is_linkset"
        :is_linkset="is_linkset"
        @closeDialog="closeLinkset"
        >选择链接</Setlink
      >
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button
          size="small"
          type="info"
          @click="cancelFunc"
          :loading="loading"
          >取消</el-button
        >
        <el-button
          size="small"
          type="primary"
          @click="onSubmit"
          :loading="loading"
          >提交</el-button
        >
      </div>
    </el-form>
  </div>
</template>

<script>
import AdApi from "@/api/ad.js";
import Upload from "@/components/file/Upload.vue";
import Setlink from "@/components/setlink/Setlink.vue";
export default {
  components: {
    /*图片上传*/
    Upload: Upload,
    Setlink,
  },
  data() {
    return {
      /*是否加载完成*/
      loading: true,
      /*是否上传图片*/
      isupload: false,
      /*form表单数据*/
      is_linkset: false,
      tips_Id: "",
      isImg: true,
      path: "",
      form: {
        adId: "",
        title: "",
        categoryId: 0,
        imageId: "",
        image: {},
        sort: 1,
        status: "0",
      },
      /*广告类别*/
      category: [],
      /*验证规则*/
      rules: {
        title: [{ required: true, message: "请输入广告标题", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getDetail();
  },

  methods: {
    changeLink() {
      this.is_linkset = true;
    },

    /*获取链接并关闭弹窗*/
    closeLinkset(e) {
      this.is_linkset = false;
      if (e) {
        this.form.linkUrl = e.url;
        this.form.name = "链接到" + " " + e.type + " " + e.name;
        this.tips_Id = "链接到" + " " + e.type + " " + e.name;
      }
    },
    /*上传*/
    openUpload() {
      this.isupload = true;
    },

    /*获取图片*/
    returnImgsFunc(e) {
      let self = this;
      if (e != null) {
        this.form.imageId = e[0].fileId;
        this.path = e[0].filePath;
        this.isImg = true;
      }
      this.isupload = false;
    },

    getDetail() {
      let self = this;
      // 取到路由带过来的参数
      const params = self.$route.query.adId;
      AdApi.adDetail({ adId: params }, true)
        .then((res) => {
          self.form = res.data;
          self.tips_Id = res.data.name;
          if (!self.form.image) {
            self.form.image = {};
          }
          self.path = res.data.filePath;
          self.category = res.data.catgory;
          self.loading = false;
        })
        .catch((error) => {});
    },

    /*修改文章*/
    onSubmit() {
      let self = this;
      let params = this.form;
      // 取到路由带过来的参数
      AdApi.editAd(params, true)
        .then((data) => {
          ElMessage({
            message: data.msg,
            type: "success",
          });
          self.$router.push("/operate/ad/index");
        })
        .catch((error) => {});
    },

    /*取消添加，返回文章列表*/
    cancelFunc() {
      this.$router.push({
        path: "/operate/ad/index",
      });
    },
  },
};
</script>

<style>
.edit_container {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.ql-editor {
  height: 400px;
}
</style>
