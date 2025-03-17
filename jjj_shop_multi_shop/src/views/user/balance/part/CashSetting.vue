<template>
  <div class="pb50">
    <el-form ref="form" size="small" :model="form" label-width="200px">
      <div class="common-form">提现设置</div>
      <el-form-item label="是否开启提现">
        <el-radio-group v-model="form.isOpen">
          <el-radio label="1">开启</el-radio>
          <el-radio label="0">关闭</el-radio>
        </el-radio-group>
        <div class="lh18 mt10 gray9">
          <p>注：如开启则移动端显示余额提现</p>
        </div>
      </el-form-item>
      <el-form-item
        label="提现比例 "
        prop="cashRatio"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          placeholder="请输入内容"
          v-model="form.cashRatio"
          class="max-w460"
        >
          <template #append>%</template>
        </el-input>
        <div class="lh18 mt10 gray9">
          <p>注：提现比例请填写数字0~100</p>
          <p>例：提现金额(100.00元) * 提现比例(100%) = 实际到账(100元)</p>
        </div>
      </el-form-item>
      <el-form-item
        label="最低提现金额"
        prop="minMoney"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          placeholder="请输入内容"
          v-model="form.minMoney"
          class="max-w460"
        >
          <template #append>元</template>
        </el-input>
        <div class="lh18 mt10 gray9">
          <p>注：最低提现金额多少元</p>
        </div>
      </el-form-item>
      <!--提交-->
      <div class="common-button-wrapper">
        <el-button
          type="primary"
          size="small"
          @click="onSubmit"
          :loading="loading"
          >提交</el-button
        >
      </div>
    </el-form>
  </div>
</template>
<script>
import BalanceApi from "@/api/balance.js";
export default {
  data() {
    return {
      form: {},
      loading: false,
    };
  },
  created() {
    /*获取数据*/
    this.getData();
  },
  methods: {
    /*获取数据*/
    getData() {
      let self = this;
      let Params = {};
      BalanceApi.getCashSetting(Params, true)
        .then((res) => {
          self.form = res.data;
        })
        .catch((error) => {});
    },

    /*保存*/
    onSubmit() {
      let self = this;
      let form = self.form;
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          BalanceApi.cashSetting(form, true)
            .then((data) => {
              self.loading = false;
              if (data.code == 1) {
                ElMessage({
                  message: "恭喜你，保存成功",
                  type: "success",
                });
              } else {
                self.loading = false;
              }
            })
            .catch((error) => {
              self.loading = false;
            });
        }
      });
    },
  },
};
</script>

<style></style>
