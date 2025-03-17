<template>
  <div class="pt30">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <!--<el-form-item label="获取手机号">
        <el-checkbox-group v-model="form.checkedCities">
          <el-checkbox v-for="(item,index) in allType" :label="item.value" :key="index">{{item.name}}</el-checkbox>
         </el-checkbox-group>
      </el-form-item>-->

      <el-form-item label="拒绝后" prop="sendDay">
        <div style="width: 500px">
          <el-input placeholder="请输入" v-model="form.sendDay" type="number">
            <template #append> 天不再提醒 </template>
          </el-input>
          <p class="gray">拒绝获取后多久再提示，设置为0则每次都要提醒</p>
        </div>
      </el-form-item>

      <!--提交-->
      <div class="common-button-wrapper">
        <el-button
          size="small"
          type="primary"
          @click="onSubmit"
          :loading="loading"
          >提交</el-button
        >
      </div>
    </el-form>
  </div>
</template>

<script>
import SettingApi from "@/api/setting.js";
export default {
  data() {
    return {
      /*是否正在加载*/
      loading: false,
      /*订单*/
      form: {
        checkedCities: [],
        sendDay: 7,
      },
      allType: [],
    };
  },
  created() {
    this.getParams();
  },

  methods: {
    /*获取配置数据*/
    getParams() {
      let self = this;
      SettingApi.getPhoneDetail({}, true)
        .then((res) => {
          let vars = res.data;
          self.form.checkedCities = vars.checkedCities;
          self.form.sendDay = vars.sendDay;
          // 转成整数，兼容组件
          for (let i = 0; i < self.form.checkedCities.length; i++) {
            self.$set(
              self.form.checkedCities,
              i,
              parseInt(self.form.checkedCities[i])
            );
          }
          self.form.sendDay = vars.sendDay;
          self.allType = res.data.allType;
          self.loading = false;
        })
        .catch((error) => {});
    },

    /*提交*/
    onSubmit() {
      let self = this;
      let params = this.form;
      console.log(params);
      self.loading = true;
      SettingApi.editGetPhone(params, true)
        .then((data) => {
          self.loading = false;
          ElMessage({
            message: "恭喜你，设置成功",
            type: "success",
          });
        })
        .catch((error) => {
          self.loading = false;
        });
    },
  },
};
</script>

<style></style>
