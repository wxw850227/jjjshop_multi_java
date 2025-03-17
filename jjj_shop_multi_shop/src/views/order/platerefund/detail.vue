<template>
  <div class="user pb50" v-loading="loading">
    <div class="product-content">
      <!--售后单信息-->
      <div class="common-form">售后单信息</div>
      <div class="refund-detail-content">
        <el-row>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">订单号：</span>
              {{ detail.order.orderNo }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">买家：</span>
              {{ detail.user.nickname }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">售后类型：</span>
              {{ detail.typeText }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">处理状态：</span>
              {{ detail.statusText }}
            </div>
          </el-col>
          <el-col :span="4">
            <div class="pb16">
              <el-button
                size="mini"
                href="/"
                target="_blank"
                @click="gotoPage(detail)"
                >订单详情</el-button
              >
            </div>
          </el-col>
        </el-row>
      </div>
      <!--售后单信息-->
      <div class="common-form">商户信息</div>
      <div class="refund-detail-content">
        <el-row>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">商户名称：</span>
              {{ detail.supplierName }}
            </div>
          </el-col>
        </el-row>
      </div>
      <!--售后商品信息-->
      <div class="common-form">售后商品信息</div>
      <div class="refund-detail-content">
        <el-row>
          <el-col :span="5">
            <div class="pb16"><span class="gray9">商品编码：</span></div>
          </el-col>
          <el-col :span="19">
            <div class="pb16">
              <span class="gray9">商品名称：</span>
              {{ detail.orderProduct.productName }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">重量(Kg)：</span>
              {{ detail.orderProduct.productWeight }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">单价：</span>
              {{ detail.orderProduct.linePrice }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">购买数量：</span>
              {{ detail.orderProduct.totalNum }}
            </div>
          </el-col>
          <el-col :span="5" v-if="detail.order.orderSource == 80">
            <div class="pb16">
              <span class="gray9">付款价：</span>
              {{ (detail.order.payPrice * 1).toFixed(2) }}
            </div>
          </el-col>
          <el-col :span="5" v-else>
            <div class="pb16">
              <span class="gray9">付款价：</span>
              {{ detail.orderProduct.totalPayPrice }}
            </div>
          </el-col>
        </el-row>
      </div>

      <!--用户申请原因-->
      <div class="apply-reason-box">
        <div class="common-form">用户申请原因</div>
        <div class="apply-reason">{{ detail.applyDesc }}</div>
        <div
          class="d-s-s mt10"
          v-if="detail.images && detail.images.length > 0"
        >
          <div class="pic" v-for="(item, index) in detail.images" :key="index">
            <img v-img-url="item.filePath" alt="" width="60" />
          </div>
        </div>
      </div>

      <!-- 退货地址 -->
      <div v-if="detail.isAgree == 10 && detail.type != 30">
        <div class="common-form mt16">退货地址</div>
        <el-row>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">收货人：</span>
              {{ detail.address.name }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">收货电话：</span>
              {{ detail.address.phone }}
            </div>
          </el-col>
          <el-col :span="14">
            <div class="pb16">
              <span class="gray9">收货地址：</span>
              {{ detail.address.detail }}
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 商家拒绝原因 -->
      <div v-if="detail.isAgree == 20">
        <div class="common-form">商家拒绝原因</div>
        <div class="apply-reason">{{ detail.refuseDesc }}</div>
      </div>
      <!-- 平台拒绝原因 -->
      <div v-if="detail.plateStatus == 30">
        <div class="common-form">平台拒绝原因</div>
        <div class="apply-reason">{{ detail.plateDesc }}</div>
      </div>
      <!-- 用户发货信息 -->
      <div
        v-if="
          detail.type != 30 && detail.isAgree == 10 && detail.isUserSend == 1
        "
      >
        <div class="common-form mt16">用户发货信息</div>
        <el-row>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">物流公司：</span>
              {{ detail.express.expressName }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">物流单号：</span>
              {{ detail.expressNo }}
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">用户发货状态：</span>
              已发货
            </div>
          </el-col>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">发货时间：</span>
              {{ detail.sendTime }}
            </div>
          </el-col>
          <el-col :span="4">
            <div class="pb16">
              <span class="gray9">商家收货状态：</span>
              <template v-if="detail.isReceipt == 1"> 已收货 </template>
              <template v-else=""> 待收货 </template>
            </div>
          </el-col>
        </el-row>
      </div>
      <!--售后单信息-->
      <div class="common-form">商家审核</div>
      <div class="refund-detail-content">
        <el-row>
          <el-col :span="5">
            <div class="pb16">
              <span class="gray9">审核状态：</span>
              {{ detail.statusText }}
            </div>
          </el-col>
          <el-col
            :span="5"
            v-if="detail.isAgree == 10 && detail.refundMoney > 0"
          >
            <div class="pb16">
              <span class="gray9">退款金额：</span>
              {{ detail.refundMoney }}
            </div>
          </el-col>
        </el-row>
      </div>
      <!--商家审核-->
      <div v-if="detail.plateStatus == 10">
        <div class="common-form mt16">平台审核</div>
        <el-form size="small" ref="form" :model="form" label-width="80px">
          <el-form-item label="售后类型">
            <span class="orange">{{ detail.typeText }}</span>
          </el-form-item>
          <el-form-item label="审核状态">
            <el-radio-group v-model="form.isAgree">
              <el-radio :label="10">直接退款</el-radio>
              <el-radio :label="20">拒绝售后</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="退款金额">
            <el-input v-model="form.refundMoney"></el-input>
            <p>
              请输入退款金额，最多{{
                detail.orderProduct.totalPayPrice
              }}
              元。注：该操作将执行订单原路退款
              并关闭当前售后单，请确认并填写退款的金额（不能大于订单实付款）
            </p>
          </el-form-item>
          <el-form-item label="拒绝原因" v-if="form.isAgree == 20"
            ><el-input
              type="textarea"
              v-model="form.plateDesc"
              class="max-w460"
            ></el-input
          ></el-form-item>
        </el-form>
      </div>
      <div
        v-if="
          detail.type == 20 &&
          detail.isAgree.value == 10 &&
          detail.isUserSend == 1
        "
      >
        <div v-if="detail.isPlateSend == 1">
          <div class="common-form mt16">平台发货信息</div>
          <el-row>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">物流公司：</span>
                {{ detail.sendexpress.expressName }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">物流单号：</span>
                {{ detail.sendExpressNo }}
              </div>
            </el-col>
            <el-col :span="5">
              <div class="pb16">
                <span class="gray9">发货时间：</span>
                {{ detail.deliverTime }}
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <div class="common-button-wrapper">
      <el-button size="small" type="info" @click="cancelFunc"
        >取消 / 返回上一页</el-button
      >
      <!--商家审核-->
      <template v-if="detail.plateStatus == 10">
        <el-button
          size="small"
          type="primary"
          @click="onSubmit(detail.orderRefundId)"
          >确认审核</el-button
        >
      </template>
    </div>
  </div>
</template>
<script>
import OrderApi from "@/api/order.js";
export default {
  data() {
    return {
      tableData: {},
      /*退货详情*/
      detail: {
        isAgree: {},
        type: {},
        status: {},
        order: {},
        user: {},
        orderProduct: {},
        express: {},
        address: {},
        image: [],
        is_plate_send: 0,
        sendexpress: {},
      },
      /*订单详情*/
      order: {},
      /*退货地址列表*/
      address: {},
      form: {
        isAgree: 10,
        addressId: 0,
        plateDesc: "",
        refundMoney: 0,
      },
      money: {
        refundMoney: 0,
      },
      orderRefundId: 0,
      loading: true,
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
      let orderRefundId = this.$route.query.orderRefundId;
      OrderApi.refundplateDetail(
        {
          orderRefundId: orderRefundId,
        },
        true
      )
        .then((res) => {
          self.detail = res.data;
          self.order = res.data.order;
          self.addressList = res.data.addressList;
          self.expressList = res.data.expressList;
          self.loading = false;
        })
        .catch((error) => {});
    },

    /*跳转订单详情*/
    gotoPage(row) {
      let self = this;
      this.$router.push({
        path: "/order/order/detail",
        query: {
          orderId: row.orderId,
        },
      });
    },

    /*审核*/
    onSubmit(e) {
      let self = this;
      let form = self.form;
      let orderRefundId = e;
      OrderApi.plateApproval(
        {
          isAgree: form.isAgree,
          addressId: form.addressId,
          plateDesc: form.plateDesc,
          orderRefundId: orderRefundId,
          refundMoney: form.refundMoney,
        },
        true
      )
        .then((data) => {
          ElMessage({
            message: "恭喜你，操作成功",
            type: "success",
          });
          this.getData();
        })
        .catch((error) => {});
    },

    /*确认收货退款*/
    refundMoney(e) {
      let self = this;
      let form = self.money;
      OrderApi.receipt(
        {
          refundMoney: form.refundMoney,
          orderRefundId: e,
        },
        true
      )
        .then((data) => {
          ElMessage({
            message: "恭喜你，操作成功",
            type: "success",
          });
          this.getData();
        })
        .catch((error) => {});
    },

    /*取消*/
    cancelFunc() {
      this.$router.back(-1);
    },
  },
};
</script>
<style>
.apply-reason {
  padding: 16px;
  background: #fff4f4;
  border: 1px solid #f6e6e6;
}

.apply-reason-box .pic {
  width: 100px;
  height: 100px;
  margin-right: 4px;
  border: 1px solid #d1d5dd;
}

.apply-reason-box .pic img {
  width: 98px;
  height: 98px;
  object-fit: contain;
}
</style>
