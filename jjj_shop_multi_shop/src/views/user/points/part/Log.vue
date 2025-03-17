<template>
  <div class="user">
    <!--搜索表单-->
    <div class="common-seach-wrap">
      <el-form
        size="small"
        :inline="true"
        :model="formInline"
        class="demo-form-inline"
      >
        <el-form-item label="昵称">
          <el-input
            v-model="formInline.nickName"
            placeholder="请输入昵称"
          ></el-input>
        </el-form-item>
        <el-form-item label="注册时间">
          <div class="block">
            <span class="demonstration"></span>
            <el-date-picker
              v-model="formInline.startDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择开始日期"
            >
            </el-date-picker>
            <span>-</span>
            <el-date-picker
              v-model="formInline.endDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择结束日期"
            >
            </el-date-picker>
          </div>
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
          <el-table-column prop="logId" label="ID" width="60"></el-table-column>
          <el-table-column label="微信头像">
            <template #default="scope">
              <img :src="scope.row.avatarUrl" :width="30" :height="30" />
            </template>
          </el-table-column>
          <el-table-column prop="nickName" label="昵称"></el-table-column>
          <el-table-column prop="value" label="变动数量"></el-table-column>
          <el-table-column
            prop="description"
            label="描述/说明"
          ></el-table-column>
          <el-table-column prop="remark" label="管理员备注"></el-table-column>
          <el-table-column prop="createTime" label="创建时间"></el-table-column>
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
import PointsApi from "@/api/points.js";
export default {
  components: {},
  data() {
    return {
      /*是否加载完成*/
      loading: true,
      /*列表数据*/
      tableData: [],
      /*一页多少条*/
      pageSize: 10,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
      /*横向表单数据模型*/
      formInline: {
        nickName: "",
      },

      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
        },
      },
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
      let Params = self.formInline;
      Params.pageIndex = self.curPage;
      Params.pageSize = self.pageSize;
      PointsApi.GetUserList(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.records;
          self.totalDataNumber = res.data.total;
        })
        .catch((error) => {});
    },

    /*搜索查询*/
    onSubmit() {
      let self = this;
      self.loading = true;
      self.getTableList();
    },
  },
};
</script>
<style></style>
