<template>
  <div class="user">
    <div class="common-form">物流公司列表</div>
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
            prop="expressId"
            label="物流公司ID"
          ></el-table-column>
          <el-table-column
            prop="expressName"
            label="物流公司名称"
          ></el-table-column>
          <el-table-column
            prop="expressCode"
            label="物流公司代码"
          ></el-table-column>
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
import SettingApi from "@/api/setting.js";
export default {
  data() {
    return {
      /*是否加载完成*/
      loading: true,
      /*列表数据*/
      tableData: [],
      /*一页多少条*/
      pageSize: 20,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
    };
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.curPage = val;
      self.loading = true;
      self.getData();
    },

    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getData();
    },

    /*获取列表*/
    getData() {
      let self = this;
      let Params = {};
      Params.pageIndex = self.curPage;
      Params.pageSize = self.pageSize;
      SettingApi.expressList(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.records;
          self.totalDataNumber = res.data.total;
        })
        .catch((error) => {});
    },
  },
};
</script>

<style></style>
