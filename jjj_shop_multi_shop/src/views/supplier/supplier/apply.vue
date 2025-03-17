<template>
  <div class="user clearfix">
    <div class="common-seach-wrap fr">
      <el-form
        size="small"
        :inline="true"
        :model="formInline"
        class="demo-form-inline"
      >
        <el-form-item label="状态">
          <el-select
            size="small"
            v-model="formInline.status"
            placeholder="请选择"
          >
            <el-option label="全部" value=""></el-option>
            <el-option label="待审核" value="0"></el-option>
            <el-option label="待付款" value="1"></el-option>
            <el-option label="成功" value="3"></el-option>
            <el-option label="未通过" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-input
            v-model="formInline.storeName"
            placeholder="请输入店铺名称"
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="Search" @click="onSubmit"
            >查询</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table
          size="small"
          :data="tableData"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column
            prop="supplierApplyId"
            label="申请ID"
            width="90"
          ></el-table-column>
          <el-table-column prop="" label="会员昵称"
            ><template #default="scope">
              <span>{{ scope.row.nickName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="storeName" label="店铺名称"></el-table-column>
          <el-table-column label="营业执照" width="80">
            <template #default="scope">
              <el-image
                v-if="scope.row.businessImage"
                :src="scope.row.businessImage"
                :width="50"
                :height="50"
                :preview-src-list="getSrcList(scope.$index)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="mobile" label="手机号码"></el-table-column>
          <el-table-column prop="userName" label="姓名"></el-table-column>
          <el-table-column prop="" label="主营类别"
            ><template #default="scope">
              <span v-if="scope.row.categoryId">{{
                scope.row.categoryName
              }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="90">
            <template #default="scope">
              <span v-if="scope.row.status == 0">待审核</span>
              <span v-if="scope.row.status == 1" class="green">审核通过</span>
              <span v-if="scope.row.status == 2" class="gray">未通过</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="添加时间"
            width="150"
          ></el-table-column>
          <el-table-column
            prop="updateTime"
            label="编辑时间"
            width="150"
          ></el-table-column>
          <el-table-column fixed="right" label="操作" width="90">
            <template #default="scope">
              <el-button
                v-if="scope.row.status == 0"
                @click="editClick(scope.row)"
                type="text"
                size="small"
                v-auth="'/supplier/supplier/audit'"
                >审核</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!--分页-->
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
          :current-page="curPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="totalDataNumber"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import SupplierApi from "@/api/supplier.js";
export default {
  components: {},
  data() {
    return {
      /*是否加载完成*/
      loading: true,
      /*列表数据*/
      tableData: [],
      /*门店列表数据*/
      storeList: [],
      /*一页多少条*/
      pageSize: 20,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
      /*横向表单数据模型*/
      formInline: {
        status: "",
        storeName: "",
      },
      /*是否打开添加弹窗*/
      open_add: false,
      /*是否打开编辑弹窗*/
      open_edit: false,
      /*当前编辑的对象*/
      userModel: {},
      srcList: [],
    };
  },
  created() {
    /*获取列表*/
    this.getTableList();
  },
  methods: {
    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.curPage = val;
      self.loading = true;
      self.getTableList();
    },

    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getTableList();
    },

    /*获取列表*/
    getTableList() {
      let self = this;
      let Params = this.formInline;
      Params.pageIndex = self.curPage;
      Params.pageSize = self.pageSize;
      SupplierApi.supplierPendList(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.records;
          self.totalDataNumber = res.data.total;
          self.tableData.forEach(function (item) {
            if (item.businessImage) {
              self.srcList.push(item.businessImage);
            }
          });
        })
        .catch((error) => {
          self.loading = false;
        });
    },
    getSrcList(index) {
      return this.srcList.slice(index).concat(this.srcList.slice(0, index));
    },
    /*搜索查询*/
    onSubmit() {
      this.curPage = 1;
      this.getTableList();
    },

    /*打开编辑*/
    editClick(row) {
      let self = this;
      let params = row.supplierApplyId;
      this.$router.push({
        path: "/supplier/supplier/audit",
        query: {
          supplierApplyId: params,
        },
      });
    },

    /*删除*/
    deleteClick(row) {
      let self = this;
      ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          self.loading = true;
          SupplierApi.deleteSupplier(
            {
              shop_supplier_id: row,
            },
            true
          )
            .then((data) => {
              self.loading = false;
              if (data.code == 1) {
                ElMessage({
                  message: "恭喜你，商户删除成功",
                  type: "success",
                });
                self.getTableList();
              }
            })
            .catch((error) => {
              self.loading = false;
            });
        })
        .catch(() => {
          self.loading = false;
        });
    },
  },
};
</script>

<style></style>
