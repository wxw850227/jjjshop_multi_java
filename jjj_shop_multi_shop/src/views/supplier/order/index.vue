<template>
  <div class="user clearfix">
    <!--添加商户-->
    <!-- <div class="common-level-rail fl">
      <el-button size="small" type="primary" icon="el-icon-plus" @click="addClick" v-auth="'/plus/supplier/supplier/add'">添加商户</el-button>
    </div> -->
    <div class="common-seach-wrap fr">
      <el-form
        size="small"
        :inline="true"
        :model="formInline"
        class="demo-form-inline"
      >
        <el-form-item label="支付状态">
          <el-select
            size="small"
            v-model="formInline.payStatus"
            placeholder="请选择"
          >
            <el-option label="全部" value=""></el-option>
            <el-option label="待支付" value="10"></el-option>
            <el-option label="已支付" value="20"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="支付类型">
          <el-select
            size="small"
            v-model="formInline.payType"
            placeholder="请选择"
          >
            <el-option label="全部" value=""></el-option>
            <el-option label="余额" value="10"></el-option>
            <el-option label="微信" value="20"></el-option>
          </el-select>
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
            prop="orderId"
            label="ID"
            width="90"
          ></el-table-column>
          <el-table-column
            prop="orderNo"
            label="订单号"
            width="150"
          ></el-table-column>
          <el-table-column prop="" label="会员昵称">
            <template #default="scope">
              <span>{{ scope.row.userName }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="payPrice"
            label="支付金额"
            width="90"
          ></el-table-column>
          <el-table-column
            prop="payTime"
            label="支付时间"
            width="150"
          ></el-table-column>
          <el-table-column prop="payType" label="支付方式" width="90">
            <template #default="scope">
              <span v-if="scope.row.payType == 10">余额</span>
              <span v-if="scope.row.payType == 20">微信</span>
            </template>
          </el-table-column>
          <el-table-column prop="payStatus" label="状态" width="90">
            <template #default="scope">
              <span v-if="scope.row.payStatus == 10">待支付</span>
              <span v-if="scope.row.payStatus == 20">已支付</span>
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
          <!--      <el-table-column fixed="right" label="操作" width="90">
          </el-table-column> -->
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
        payType: "",
        payStatus: "",
      },
      /*是否打开添加弹窗*/
      open_add: false,
      /*是否打开编辑弹窗*/
      open_edit: false,
      /*当前编辑的对象*/
      userModel: {},
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
      SupplierApi.supplierOrder(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.records;
          self.totalDataNumber = res.data.total;
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*搜索查询*/
    onSubmit() {
      this.curPage = 1;
      this.getTableList();
    },
  },
};
</script>

<style></style>
