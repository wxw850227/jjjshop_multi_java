<template>
  <div class="user">
    <div class="common-seach-wrap">
      <el-form
        size="small"
        :inline="true"
        :model="formInline"
        class="demo-form-inline"
      >
        <el-form-item label="审核状态">
          <el-select v-model="formInline.applyStatus" placeholder="请选择状态">
            <el-option label="全部" value="-1"></el-option>
            <el-option label="待审核" value="10"></el-option>
            <el-option label="审核通过" value="20"></el-option>
            <el-option label="已打款" value="40"></el-option>
            <el-option label="驳回" value="30"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="提现方式">
          <el-select v-model="formInline.payType" placeholder="请选择提现方式">
            <el-option label="全部" value="-1"></el-option>
            <el-option label="微信" value="10"></el-option>
            <el-option label="支付宝" value="20"></el-option>
            <el-option label="银行卡" value="30"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户id">
          <el-input
            v-model="formInline.userId"
            placeholder="请输入用户ID"
          ></el-input>
        </el-form-item>
        <el-form-item label=""
          ><el-input
            v-model="formInline.search"
            placeholder="请输入昵称/姓名/手机号"
          ></el-input
        ></el-form-item>
        <el-form-item
          ><el-button type="primary" @click="onSubmit"
            >查询</el-button
          ></el-form-item
        >
        <el-form-item>
          <el-button
            size="small"
            type="success"
            @click="onExport"
            v-auth="'/user/cash/export'"
            >导出</el-button
          >
        </el-form-item>
      </el-form>
    </div>

    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table
          :data="tableData"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column
            prop="userId"
            label="用户ID"
            width="60"
          ></el-table-column>
          <el-table-column prop="nickName" label="微信头像" width="70">
            <template #default="scope">
              <img
                class="radius"
                v-img-url="scope.row.avatarUrl"
                width="30"
                height="30"
              />
            </template>
          </el-table-column>
          <el-table-column
            prop="nickName"
            label="微信昵称"
            width="100"
          ></el-table-column>
          <el-table-column prop="money" label="提现金额">
            <template #default="scope">
              <span class="orange">{{ scope.row.money }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="money" label="实际到账">
            <template #default="scope">
              <span class="orange">{{ scope.row.realMoney }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="payTypeText"
            label="提现方式"
          ></el-table-column>
          <el-table-column prop="payType" label="提现信息	">
            <template #default="scope">
              <div v-if="scope.row.payType == 20">
                <p>
                  <span>支付宝名称：{{ scope.row.alipayName }}</span>
                </p>
                <p>
                  <span>支付宝账号：{{ scope.row.alipayAccount }}</span>
                </p>
              </div>
              <div v-else-if="scope.row.payType == 30">
                <p>
                  <span>银行名称：{{ scope.row.bankName }}</span>
                </p>
                <p>
                  <span>开户名：{{ scope.row.bankAccount }}</span>
                </p>
                <p>
                  <span>银行卡号：{{ scope.row.bankCard }}</span>
                </p>
              </div>
              <div v-else>
                <p><span>--</span></p>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="applyStatusText"
            label="审核状态"
          ></el-table-column>
          <el-table-column
            prop="createTime"
            label="申请时间"
            width="135"
          ></el-table-column>
          <el-table-column
            prop="auditTime"
            label="审核时间"
            width="135"
          ></el-table-column>
          <el-table-column fixed="right" label="操作" width="180">
            <template #default="scope">
              <div
                v-if="
                  scope.row.applyStatus == 10 || scope.row.applyStatus == 20
                "
              >
                <el-button
                  @click="editClick(scope.row)"
                  type="text"
                  size="small"
                  v-auth="'/user/cash/audit'"
                  >审核</el-button
                >
                <template
                  v-if="scope.row.applyStatus == 20 && scope.row.payType != 10"
                >
                  <el-button
                    @click="makeMoney(scope.row)"
                    type="text"
                    size="small"
                    v-auth="'/user/cash/money'"
                    >确认打款</el-button
                  >
                </template>
                <template
                  v-if="scope.row.applyStatus == 20 && scope.row.payType == 10"
                >
                  <el-button
                    @click="WxPay(scope.row.id)"
                    type="text"
                    size="small"
                    v-auth="'/user/cash/wxpay'"
                    >微信付款</el-button
                  >
                </template>
              </div>
              <div v-if="scope.row.applyStatus == 30">
                <el-button
                  @click="editClick(scope.row)"
                  type="text"
                  size="small"
                  >详情</el-button
                >
              </div>
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
    <Edit
      v-if="open_edit"
      :open_edit="open_edit"
      :form="userModel"
      @closeDialog="closeDialogFunc($event, 'edit')"
    ></Edit>
  </div>
</template>

<script>
import BalanceApi from "@/api/balance.js";
import { getCookie } from "@/utils/base.js";
import qs from "qs";
import Edit from "./dialog/Edit.vue";
import { useUserStore } from "@/store";
const { token } = useUserStore();
export default {
  components: {
    /*编辑组件*/
    Edit,
  },
  data() {
    return {
      token,
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
        search: "",
        /*用户ID*/
        userId: "",
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
      if (to.query.userId != null) {
        this.formInline.userId = to.query.userId;
      } else {
        this.formInline.userId = "";
      }
      this.curPage = 1;
      this.getData();
    },
  },
  created() {
    if (this.$route.query.userId != null) {
      this.formInline.userId = this.$route.query.userId;
    }
    /*获取列表*/
    this.getData();
  },
  methods: {
    /*获取数据*/
    getData() {
      let self = this;
      let Params = self.formInline;
      Params.pageIndex = self.curPage;
      Params.pageSize = self.pageSize;
      BalanceApi.cashList(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.records;
          self.totalDataNumber = res.data.total;
        })
        .catch((error) => {});
    },

    /*搜索*/
    onSubmit() {
      this.curPage = 1;
      this.getData();
    },

    onExport: function () {
      let baseUrl = window.location.protocol + "//" + window.location.host;
      let Params = this.formInline;
      Params.tokenshop = this.token;
      window.location.href =
        baseUrl + "/api/shop/user/cash/export?" + qs.stringify(Params);
    },

    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getData();
    },

    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.curPage = val;
      self.getData();
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

    /*确认打款*/
    makeMoney(e) {
      let self = this;
      ElMessageBox.confirm("确认要打款吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          self.loading = true;
          BalanceApi.cashMoney(
            {
              id: e.id,
            },
            true
          )
            .then((data) => {
              self.loading = false;
              if (data.code == 1) {
                ElMessage({
                  message: "恭喜你，操作成功",
                  type: "success",
                });
                this.getData();
              } else {
                self.loading = false;
              }
            })
            .catch((error) => {
              self.loading = false;
            });
        })
        .catch(() => {});
    },

    /*微信打款*/
    WxPay(e) {
      let self = this;
      ElMessageBox.confirm(
        "该操作 将使用微信支付企业付款到零钱功能，确定打款吗？",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          self.loading = true;
          BalanceApi.cashWxpay(
            {
              id: e,
            },
            true
          )
            .then((data) => {
              self.loading = false;
              if (data.code == 1) {
                ElMessage({
                  message: "恭喜你，操作成功",
                  type: "success",
                });
                this.getData();
              } else {
                self.loading = false;
              }
            })
            .catch((error) => {
              self.loading = false;
            });
        })
        .catch(() => {});
    },
  },
};
</script>

<style></style>
