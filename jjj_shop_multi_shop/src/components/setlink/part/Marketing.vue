<template>
  <div class="marketing-box">
    <el-tabs v-model="activeTab"> </el-tabs>
    <el-select
      v-model="activePage"
      placeholder="请选择"
      class="percent-w100"
      @change="changeFunc"
      value-key="id"
    >
      <el-option
        v-for="(item, index) in pages"
        :key="index"
        :label="item.name"
        :value="item"
      ></el-option>
    </el-select>
  </div>
</template>

<script>
import LinkApi from "@/api/link.js";
export default {
  data() {
    return {
      /*tab切换选择中值*/
      activeTab: "",
      /*页面数据*/
      pages: [],
      /*选中的值*/
      activePage: null,
    };
  },
  watch: {
    activeTab: function (n, o) {
      let self = this;
      self.pages = [];
      if (n != o) {
        if (n == "signin") {
          self.pages = self.signinList;
        } else if (n == "points") {
          self.pages = self.pointsList;
        } else if (n == "presale") {
          self.pages = self.presaleList;
        } else if (n == "seckill") {
          self.pages = self.seckillList;
        } else if (n == "assemble") {
          self.pages = self.assembleList;
        } else if (n == "bargain") {
          self.pages = self.bargainList;
        } else if (n == "package") {
          self.pages = self.packageList;
        } else if (n == "invitation") {
          self.pages = self.invitationList;
        } else if (n == "coupon") {
          self.pages = self.couponList;
        } else if (n == "lottery") {
          self.pages = self.lotteryList;
        } else if (n == "table") {
          self.pages = self.tableList;
        } else if (n == "task") {
          self.pages = self.taskList;
        } else if (n == "agent") {
          self.pages = self.agentList;
        }
        self.autoSend();
      }
    },
  },
  created() {
    this.pages = this.couponList;
    this.autoSend();
    this.getData();
  },
  methods: {
    /*获取数据*/
    getData() {
      let self = this;
      LinkApi.getList({}, true)
        .then((res) => {
          self.packageList = res.data.packageList;
          self.invitationList = res.data.invitationList;
          self.tableList = res.data.tableList;
        })
        .catch((error) => {});
    },
    /*自动发送*/
    autoSend() {
      if (this.pages.length > 0) {
        this.activePage = this.pages[0];
        this.changeFunc();
      }
    },
    /*选中的值*/
    changeFunc(e) {
      this.$emit("changeData", this.activePage);
    },
  },
};
</script>

<style></style>
