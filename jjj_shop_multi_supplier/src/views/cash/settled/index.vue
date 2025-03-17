<template>
  <div class="user clearfix">
    <div class="common-seach-wrap fr">
      <el-form size="small" :inline="true" :model="formInline" class="demo-form-inline">
        <div class="date_section">
          <div class="block mr10">
            <el-date-picker v-model="value1" type="date" value-format="YYYY-MM-DD" placeholder="开始时间">
            </el-date-picker>
          </div>
          <div class="block mr10">
            <el-date-picker v-model="value2" type="date" value-format="YYYY-MM-DD" placeholder="结束时间">
            </el-date-picker>
          </div>
        </div>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table size="small" :data="tableData" border style="width: 100%" v-loading="loading">
          <el-table-column prop="orderId" label="ID" width="120"></el-table-column>
          <el-table-column prop="orderMaster.orderNo" label="订单号" width="150"></el-table-column>
          <el-table-column prop="payMoney" label="支付金额" width="120"></el-table-column>
          <el-table-column prop="orderMoney" label="结算金额" width="120"></el-table-column>
          <el-table-column prop="supplierMoney" label="店铺结算金额"></el-table-column>
          <el-table-column prop="sysMoney" label="平台抽成金额"></el-table-column>
          <el-table-column prop="refundMoney" label="退款金额"></el-table-column>
          <el-table-column prop="createTime" label="添加时间" width="150"></el-table-column>
          <el-table-column prop='settledId' fixed="right" label="操作" width="120" >
            <template #default="scope">
                <el-button  @click="addClick(scope.row)" type="text" size="small" v-auth="'/cash/settled/detail'">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!--分页-->
      <div class="pagination">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background :current-page="curPage"
          :page-size="pageSize" layout="total, prev, pager, next, jumper"
          :total="totalDataNumber">
        </el-pagination>
      </div>
    </div>

  </div>
</template>

<script>
  import SupplierApi from '@/api/cash.js';
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
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now();
          },
        },
        value1: '',
        value2: '',
        formInline: {
          payType: '',
          payStatus: ''
        },
        is_settled:'全部',

        /*是否打开添加弹窗*/
        open_add: false,
        /*是否打开编辑弹窗*/
        open_edit: false,
        /*当前编辑的对象*/
        userModel: {}
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
        self.loading = true;
        SupplierApi.settled(Params, true)
          .then(res => {
            self.loading = false;
            self.tableData = res.data.records;
            self.totalDataNumber = res.data.total;
            console.log(self.tableData)
          })
          .catch(error => {
            self.loading = false;
          });
      },
      getIssettled(val){
        this.formInline.is_settled=val;
        console.log(this.formInline.is_settled)
      },
      /*搜索查询*/
      onSubmit() {
        this.curPage = 1;
        this.formInline.startDay=this.value1;
        this.formInline.endDay=this.value2;
        this.getTableList();
      },
    /*打开添加*/
    addClick(row) {
      let self = this;
      let params = row.settledId;
      self.$router.push({
        path: '/cash/settled/detail',
        query: {
          settledId: params
        }
      });
    },
    }
  };
</script>

<style>
  .date_section {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-bottom: 18px;
  }
  .demo-form-inline{
    display: flex;
    align-items: center;
  }
</style>
