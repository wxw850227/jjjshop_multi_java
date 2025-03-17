<template>
  <div class="user">
    <div class="common-seach-wrap">
      <el-form
        size="small"
        :inline="true"
        :model="formInline"
        class="demo-form-inline"
      >
        <el-form-item label="佣金结算">
          <el-select v-model="formInline.is_settled" placeholder="是否结算佣金">
            <el-option label="全部" value="-1"></el-option>
            <el-option label="已结算" value="1"></el-option>
            <el-option label="未结算" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户id"
          ><el-input
            v-model="formInline.user_id"
            placeholder="请输入用户ID"
          ></el-input
        ></el-form-item>
        <el-form-item
          ><el-button type="primary" @click="onSubmit"
            >查询</el-button
          ></el-form-item
        >
      </el-form>
    </div>

    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table
          :data="tableData"
          size="small"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column prop="order_master.create_time" label="商品信息">
            <template #default="scope">
              <div
                class="product-info p-10-0"
                v-for="(item, index) in scope.row.orderMaster.productList"
                :key="index"
              >
                <div class="pic"><img v-img-url="item.imagePath" alt="" /></div>
                <div class="info">
                  <div class="name gray3">{{ item.productName }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="referee.value" label="分销商" width="400">
            <template #default="scope">
              <div class="d-s-s d-c">
                <div
                  class="d-s-c ww100 border-b-d"
                  v-if="scope.row.firstUserId > 0"
                >
                  <p class="referee-name text-ellipsis">
                    <span class="gray9">一级分销商：</span>
                    <span class="blue">{{
                      scope.row.agentFirst.nickName
                    }}</span>
                  </p>
                  <p class="referee-name text-ellipsis">
                    <span class="gray9">用户ID：</span>
                    <span class="gray6">{{ scope.row.agentFirst.userId }}</span>
                  </p>
                  <p class="referee-name text-ellipsis">
                    <span class="gray9">分销佣金：</span>
                    <span class="orange">￥{{ scope.row.firstMoney }}</span>
                  </p>
                </div>
                <div
                  class="d-s-c ww100 border-b-d"
                  v-if="scope.row.secondUserId > 0"
                >
                  <p class="referee-name text-ellipsis">
                    <span class="gray9">二级分销商：</span>
                    <span class="blue">{{
                      scope.row.agentSecond.nickName
                    }}</span>
                  </p>
                  <p class="referee-name text-ellipsis">
                    <span class="gray9">用户ID：</span>
                    <span class="gray6">{{
                      scope.row.agentSecond.userId
                    }}</span>
                  </p>
                  <p class="referee-name text-ellipsis">
                    <span class="gray9">分销佣金：</span>
                    <span class="orange">￥{{ scope.row.secondMoney }}</span>
                  </p>
                </div>
                <div
                  class="d-s-c ww100 border-b-d"
                  v-if="scope.row.thirdUserId > 0"
                >
                  <p class="referee-name text-ellipsis">
                    <span class="gray9">三级分销商：</span>
                    <span class="blue">{{
                      scope.row.agentThird.nickName
                    }}</span>
                  </p>
                  <p class="referee-name text-ellipsis">
                    <span class="gray9">用户ID：</span>
                    <span class="gray6">{{ scope.row.agentThird.userId }}</span>
                  </p>
                  <p class="referee-name text-ellipsis">
                    <span class="gray9">分销佣金：</span>
                    <span class="orange">￥{{ scope.row.thirdMoney }}</span>
                  </p>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="nickName" label="单价/数量" width="150">
            <template #default="scope">
              <div
                v-for="(item, index) in scope.row.orderMaster.productList"
                :key="index"
              >
                <span class="orange">￥{{ item.productPrice }}</span>
                ×{{ item.totalNum }}
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="orderMaster.payPrice"
            label="实付款"
            width="100"
          >
            <template #default="scope">
              <span class="fb orange">{{
                scope.row.orderMaster.payPrice
              }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="nickName"
            label="买家"
            width="100"
          ></el-table-column>
          <el-table-column prop="mobile" label="交易状态" width="130">
            <template #default="scope">
              <p>
                <span class="gray9">付款状态：</span>
                {{ scope.row.orderMaster.payStatusText }}
              </p>
              <p>
                <span class="gray9">发货状态：</span>
                {{ scope.row.orderMaster.deliveryStatusText }}
              </p>
              <p>
                <span class="gray9">收货状态：</span>
                {{ scope.row.orderMaster.receiptStatusText }}
              </p>
            </template>
          </el-table-column>
          <el-table-column prop="referee.value" label="佣金结算" width="70">
            <template #default="scope">
              <span class="green" v-if="scope.row.isSettled == 1">已结算</span>
              <span class="red" v-if="scope.row.isSettled == 0">未结算</span>
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
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import OrderApi from "@/api/order.js";
export default {
  components: {
    /*编辑组件*/
  },
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
      formInline: {
        is_settled: "-1",
        /*用户ID*/
        user_id: "",
      },
      /*是否打开编辑弹窗*/
      open_edit: false,
      /*当前编辑的对象*/
      userModel: {},
    };
  },
  props: {},
  watch: {
    $route(to, from) {
      if (to.query.user_id != null) {
        this.formInline.user_id = to.query.user_id;
      } else {
        this.formInline.user_id = "";
      }
      this.curPage = 1;
      this.getData();
    },
  },
  created() {
    if (this.$route.query.user_id != null) {
      this.formInline.user_id = this.$route.query.user_id;
    }
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

    /*获取数据*/
    getData(user_id) {
      let self = this;
      let Params = {
        userId: self.formInline.user_id,
        pageIndex: self.curPage,
        pageSize: self.pageSize,
        isSettled: self.is_settled,
      };

      OrderApi.agentOrder(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.records;
          self.totalDataNumber = res.data.total;
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    //搜索
    onSubmit() {
      let self = this;
      self.loading = true;
      self.is_settled = self.formInline.is_settled;
      self.getData();
    },

    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getData();
    },

    /*打开弹出层编辑*/
    editClick(item) {
      this.userModel = item;
      this.open_edit = true;
    },

    /*关闭弹窗*/
    closeDialogFunc(e, f) {
      if (f == "add") {
        this.open_add = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
      if (f == "edit") {
        this.open_edit = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
    },
  },
};
</script>

<style scoped="">
.referee-name {
  width: 33.333333%;
}
</style>
