<template>
  <el-dialog title="编辑用户信息" v-model="dialogVisible" @close='dialogFormVisible' :close-on-click-modal="false" width="400px"
    :close-on-press-escape="false">
    <el-form size="small" :label-width="formLabelWidth">
      <el-form-item label="姓名">
        <el-input v-model="form.nickname" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="生日">
        <el-date-picker v-model="form.createTime" type="date" value-format="YYYY-MM-DD" style="width:100%"></el-date-picker>
      </el-form-item>
    </el-form>
    <template #footer>
    <div class="dialog-footer">
      <el-button @click="dialogFormVisible">取 消</el-button>
      <el-button type="primary" @click="editUser" :loading="loading">确 定</el-button>
    </div>
    </template>
  </el-dialog>
</template>

<script>
import UserApi from '@/api/user.js';
  export default {
    data () {
      return {
        /*左边长度*/
        formLabelWidth: '40px',
        /*是否显示*/
        loading: false,
        dialogVisible: false,
      };
    },
    props: ['open_basic', 'form', 'gradeList'],
    created () {
      this.dialogVisible = this.open_basic;
    },
    methods: {

      /*修改用户*/
      editUser () {
        let self = this;
        self.loading = true;
        let params = self.form;
        UserApi.edituser(params, true)
          .then(data => {
            self.loading = false;
            if (data.code == 1) {
              ElMessage({
                message: '恭喜你，用户修改成功',
                type: 'success'
              });
              self.dialogFormVisible(true);
            }
          })
          .catch(error => {
            self.loading = false;
          });
      },

      /*关闭弹窗*/
      dialogFormVisible (e) {
        if (e) {
          this.$emit('closeDialog', {
            type: 'success',
            openDialog: false
          })
        } else {
          this.$emit('closeDialog', {
            type: 'error',
            openDialog: false
          })
        }
      }

    }
  };
</script>

<style></style>
