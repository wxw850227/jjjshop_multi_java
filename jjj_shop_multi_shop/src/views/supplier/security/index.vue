<template>
  <div>
    <div class="common-level-rail">
      <el-button size="small" type="primary" @click="addSecurity"
        >添加服务</el-button
      >
    </div>
    <div class="table-wrap">
      <el-table :data="categoryData" style="width: 100%" v-loading="loading">
        <el-table-column prop="name" label="服务名称"></el-table-column>
        <el-table-column prop="" label="图片">
          <template #default="scope">
            <img v-img-url="scope.row.logo" alt="" :width="50" />
          </template>
        </el-table-column>
        <el-table-column prop="description" label="服务描述"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <span v-if="scope.row.status == 0" class="gray">隐藏</span>
            <span v-if="scope.row.status == 1" class="red">显示</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="添加时间"
          width="180"
        ></el-table-column>
        <el-table-column prop="name" label="操作" width="150">
          <template #default="scope">
            <el-button @click="editSecurity(scope.row)" type="text" size="small"
              >编辑</el-button
            >
            <el-button
              @click="deleteSecurity(scope.row)"
              type="text"
              size="small"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!--添加-->
      <Add
        v-if="open_add"
        :open_add="open_add"
        @closeDialog="closeDialogFunc($event, 'add')"
      ></Add>

      <!--编辑-->
      <Edit
        v-if="open_edit"
        :open_edit="open_edit"
        :editform="userModel"
        @closeDialog="closeDialogFunc($event, 'edit')"
      ></Edit>
    </div>
  </div>
</template>

<script>
import SupplierApi from "@/api/supplier.js";
import Edit from "./part/Edit.vue";
import Add from "./part/Add.vue";
import { deepClone } from "@/utils/base.js";
export default {
  components: {
    /*编辑组件*/
    Edit,
    Add,
  },
  data() {
    return {
      /*分类*/
      categoryData: [],
      /*是否打开添加弹窗*/
      open_add: false,
      /*是否打开编辑弹窗*/
      open_edit: false,
      /*当前编辑的对象*/
      userModel: {},
      commentData: [],
      /*是否加载完成*/
      loading: true,
    };
  },
  created() {
    /*获取文章列表*/
    this.getTableList();
  },
  methods: {
    /*获取文章列表*/
    getTableList() {
      let self = this;
      let Params = {};
      SupplierApi.SecurityList(Params, true)
        .then((res) => {
          self.loading = false;
          self.categoryData = res.data;
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*删除*/
    deleteSecurity(row) {
      let self = this;
      ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          self.loading = true;
          SupplierApi.deleteSecurity(
            {
              serviceSecurityId: row.serviceSecurityId,
            },
            true
          )
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: data.msg,
                type: "success",
              });
              self.getTableList();
            })
            .catch((error) => {
              self.loading = false;
            });
        })
        .catch(() => {});
    },
    handleClick(tab, event) {},

    /*打开添加*/
    addSecurity() {
      this.open_add = true;
    },

    /*打开编辑*/
    editSecurity(item) {
      this.userModel = deepClone(item);
      this.open_edit = true;
    },

    /*关闭弹窗*/
    closeDialogFunc(e, f) {
      if (f == "add") {
        this.open_add = e.openDialog;
        if (e.type == "success") {
          this.getTableList();
        }
      }
      if (f == "edit") {
        this.open_edit = e.openDialog;
        if (e.type == "success") {
          this.getTableList();
        }
      }
    },
  },
};
</script>

<style></style>
