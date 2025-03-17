<template>
  <div class="pb50" v-loading="loading">
    <!--订单进度-->
    <!--内容-->
    <div class="product-content">
      <div class="tips tips-border">
        <div class="tips_tit pb">操作提示</div>
        <div class="tips_txt">订单结算金额 = 店铺结算总金额(含运费) + 平台结算总抽成</div>
        <div class="tips_txt">店铺收入金额 = 店铺结算总金额 - 店铺退款金额 - 分销佣金</div>
        <div class="tips_txt">平台抽成金额 = 平台结算总抽成 - 平台退款抽成</div>
      </div>
      <!--店铺结算-->
      <div class="common-form">店铺结算<span class="common-form-data">订单日期:{{ detail.createTime }}</span></div>
      <div class="table-wrap">
        <el-row>
          <el-col :span="6">
            <div class="pb16">
              <div>订单结算金额（元）</div>
              <div class="detail_prici">{{ detail.orderMoney }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16">
              <div>支付金额（元）</div>
              <div class="detail_prici">{{ detail.payMoney }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16">
              <div>运费金额（元）</div>
              <div class="detail_prici">{{ detail.expressMoney }}</div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <div class="pb16">
              <div >店铺收入金额（元）</div>
              <div class="detail_prici">{{ detail.realSupplierMoney }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16">
              <div >店铺结算总金额（元）</div>
              <div class="detail_prici">{{ detail.supplierMoney }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16">
               <div >店铺退款金额（元）</div>
               <div class="detail_prici">{{ detail.refundMoney }}</div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <div class="pb16">
               <div >平台抽成金额（元）</div>
               <div class="detail_prici">{{ detail.sysMoney }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16">
               <div >平台结算总抽成（元）</div>
               <div class="detail_prici">{{ detail.realSysMoney }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="pb16">
              <div >平台退款抽成（元）</div>
              <div class="detail_prici">{{ detail.refundSysMoney }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
      <!--商品信息-->
      <div class="common-form mt16">商品信息</div>
     <div class="table-wrap">
        <el-table size="small" :data="order.productList" border style="width: 100%">
          <el-table-column prop="productName" label="商品" width="400">
            <template #default="scope">
              <div class="product-info">
                <div class="pic"><img v-img-url="scope.row.imagePath" /></div>
                <div class="info">
                  <div class="name">{{ scope.row.productName }}</div>
                  <div class="gray9" v-if="scope.row.productAttr!=''">{{scope.row.productAttr}}</div>
                  <div class="orange" v-if="scope.row.refund">{{ scope.row.refundTypeText }}-{{ scope.row.refundStatusText }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="productNo" label="商品编码"></el-table-column>
          <el-table-column prop="productWeight" label="重量 (Kg)"></el-table-column>
          <el-table-column prop="totalNum" label="购买数量">
            <template #default="scope">
              <p>x {{ scope.row.totalNum }}</p>
            </template>
          </el-table-column>
          <el-table-column prop="totalPayPrice" label="支付金额(元)">
            <template #default="scope">
              <p>￥ {{ scope.row.totalPayPrice }}</p>
            </template>
          </el-table-column>
          <el-table-column prop="total_pay_price" label="退款金额(元)">
            <template #default="scope">
              <p v-if="scope.row.refund">{{scope.row.refund.refundMoney}}</p>
              <p v-else>0</p>
            </template>
          </el-table-column>
          <!--<el-table-column prop="first_money" label="一级分销佣金(元)">
            <template #default="scope">
              <p>￥ {{ scope.row.first_money }}</p>
            </template>
          </el-table-column>
          <el-table-column prop="second_money" label="二级分销佣金(元)">
            <template #default="scope">
              <p>￥ {{ scope.row.second_money }}</p>
            </template>
          </el-table-column>
          <el-table-column prop="third_money" label="三级分销佣金(元)">
            <template #default="scope">
              <p>￥ {{ scope.row.third_money }}</p>
            </template>
          </el-table-column>-->
        </el-table>
      </div>

    </div>
    <div class="common-button-wrapper">
      <el-button size="small" type="info" @click="cancelFunc">返回上一页</el-button>
    </div>
  </div>
</template>

<script>
  import SupplierApi from '@/api/cash.js';
  import {
    deepClone
  } from '@/utils/base.js';
  export default {
    data() {
      return {
        active: 0,
        /*是否加载完成*/
        loading: true,
        /*订单数据*/
        detail: {
        },
        order: {},
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
      this.getParams();
    },
    methods: {
      /*获取参数*/
      getParams() {
        let self = this;
        // 取到路由带过来的参数
        const params = this.$route.query.settledId;
        SupplierApi.detail({
              settledId: params
            },
            true
          )
          .then(res => {
            self.loading = false;
            self.detail = res.data;
            self.order = res.data.orderMaster;
          })
          .catch(error => {
            self.loading = false;
          });
      },

      /*取消*/
      cancelFunc() {
        this.$router.back(-1);
      }

    }
  };
</script>
<style>
  .table-wrap{
    padding: 20px;
    padding-top: 0;
  }
  .common-form-data{
    margin-left: 15px;
    font-weight: 500;
  }
  .tips-border{
    border: 1px solid #CACACA;
    padding: 15px;
    margin-bottom: 20px;
  }
  .tips_tit{
    font-size: 22px;
    margin-bottom: 10px;
  }
  .tips_txt{
    line-height: 25px;
    color: #666;
    font-size: 14px;
  }
  .detail_prici{
       font-size: 20px;
       color: #000;
       font-weight: bold;
       margin-top: 10px;
       max-width: 250px;
  }
</style>
