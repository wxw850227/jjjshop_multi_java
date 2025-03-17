<template>
  <el-dialog
    title="修改管理员"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :append-to-body="true"
  >
    <!--form表单-->
    <el-form
      size="small"
      ref="form"
      :model="form"
      :rules="formRules"
      :label-width="formLabelWidth"
      v-loading="loading"
    >
      <el-form-item label="用户名" prop="userName"
        ><el-input v-model="form.userName" placeholder="请输入用户名"></el-input
      ></el-form-item>
      <el-form-item label="所属角色" prop="access_id">
        <el-select v-model="form.roleId" :multiple="true">
          <el-option
            v-for="item in roleList"
            :value="item.roleId"
            :key="item.roleId"
            :label="item.roleName"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="绑定用户">
        <el-row>
          <el-button @click="addClick" icon="Plus">选择会员</el-button>
          <div v-if="form.userId > 0" class="img">
            <img :src="userInfo.avatarUrl" width="100" height="100" />
            <span>{{ userInfo.userId }}({{ userInfo.nickName }})</span>
          </div>
        </el-row>
      </el-form-item>
      <el-form-item label="登录密码" prop="password"
        ><el-input
          v-model="form.password"
          placeholder="请输入登录密码"
          type="password"
        ></el-input
      ></el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword"
        ><el-input
          v-model="form.confirmPassword"
          placeholder="请输入确认密码"
          type="password"
        ></el-input
      ></el-form-item>
      <el-form-item label="姓名" prop="realName"
        ><el-input v-model="form.realName"></el-input
      ></el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit" :loading="loading"
          >确 定</el-button
        >
      </div>
    </template>
    <!--添加-->
    <GetUser :is_open="open_add" @close="closeDialogFunc"></GetUser>
  </el-dialog>
</template>

<script>
import AuthApi from "@/api/auth.js";
import GetUser from "@/components/user/GetUser.vue";
export default {
  data() {
    return {
      /*左边长度*/
      formLabelWidth: "120px",
      /*是否显示*/
      loading: false,
      open_add: false,
      /*是否显示*/
      dialogVisible: false,
      /*form表单对象*/
      form: {
        confirmPassword: "",
        accessId: [],
        userId: 0,
      },
      userInfo: {
        avatarUrl: "",
      },
      /*form验证*/
      formRules: {
        user_name: [
          {
            required: true,
            message: " ",
            trigger: "blur",
          },
        ],
        access_id: [
          {
            required: true,
            message: " ",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: " ",
            trigger: "blur",
          },
        ],
        confirm_password: [
          {
            required: true,
            message: " ",
            trigger: "blur",
          },
        ],
        real_name: [
          {
            required: true,
            message: " ",
            trigger: "blur",
          },
        ],
      },
    };
  },
  props: ["open", "roleList", "model"],
  watch: {
    open: function (n, o) {
      if (n != o) {
        this.dialogVisible = this.open;
        this.form.userName = this.model.userName;
        this.form.realName = this.model.realName;
        this.form.roleId = [];
        this.form.password = "";
        this.form.confirmPassword = "";
        this.model.roleList.forEach((item) => {
          this.form.roleId.push(item.roleId);
        });
        this.userInfo = {};
        this.getData();
      }
    },
  },
  components: {
    GetUser,
  },
  created() {},
  methods: {
    /*获取数据*/
    getData() {
      let self = this;
      AuthApi.userEditInfo({
        supplierUserId: this.model.supplierUserId,
      })
        .then((res) => {
          self.loading = false;
          self.userInfo.userId = res.data.userId;
          self.form.userId = res.data.userId;
          if (res.data.userId > 0) {
            self.userInfo.nickName = res.data.nickName;
            self.userInfo.avatarUrl = res.data.avatarUrl;
          }
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*修改*/
    onSubmit() {
      let self = this;
      self.loading = true;
      let params = self.form;
      AuthApi.userEdit(params, true)
        .then((data) => {
          self.loading = false;
          ElMessage({
            message: "恭喜你，修改成功",
            type: "success",
          });
          self.dialogFormVisible(true);
        })
        .catch((error) => {
          self.loading = false;
        });
    },
    addClick() {
      this.open_add = true;
    },
    /*关闭获取用户弹窗*/
    closeDialogFunc(e) {
      if (e && e.type && e.type != "error") {
        this.form.userId = e.params[0].userId;
        this.userInfo = e.params[0].user;
      }
      this.open_add = false;
    },
    /*关闭弹窗*/
    dialogFormVisible(e) {
      if (e) {
        this.$emit("close", {
          type: "success",
          openDialog: false,
        });
      } else {
        this.$emit("close", {
          type: "error",
          openDialog: false,
        });
      }
    },
  },
};
</script>

<style></style>
