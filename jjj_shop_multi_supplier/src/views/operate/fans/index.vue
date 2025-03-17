<template>
  <div>
    <div class="table-wrap">
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="nickname" label="昵称"></el-table-column>
        <el-table-column label="微信头像">
          <template #default="scope">
            <img :src="scope.row.avatarurl" :width="30" :height="30" />
          </template>
        </el-table-column>
        <el-table-column prop="gender" label="性别">
          <template #default="scope">
            {{ scope.row.gender | convertSex }}
          </template>
        </el-table-column>
        <el-table-column prop="country" label="国家"></el-table-column>
        <el-table-column prop="province" label="省份"></el-table-column>
        <el-table-column prop="city" label="城市"></el-table-column>
        <el-table-column prop="createTime" label="关注时间" width="140"></el-table-column>
      </el-table>

      <!--分页-->
      <div class="pagination">
        <el-pagination background :current-page="curPage" :page-size="pageSize" layout="total, prev, pager, next, jumper" :total="totalDataNumber"></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import FansApi from '@/api/fans.js';
export default {
  components: {},
  data() {
    return {
      /*表单数据*/
      tableData: [],
      loading: true,
      /*一页多少条*/
      pageSize: 20,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1
    };
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    /*获取列表*/
    getData() {
      let self = this;
      let Params = {};
      Params.page = self.curPage;
      FansApi.fansList(Params, true)
        .then(res => {
          self.loading = false;
          self.tableData = res.data.records;
          self.totalDataNumber = res.data.total;
        })
        .catch(error => {
          self.loading = false;
        });
    },
    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.curPage = val;
      self.loading = true;
      self.getData();
    },
  }
};
</script>

<style></style>
