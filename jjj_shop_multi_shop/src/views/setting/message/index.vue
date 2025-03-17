<template>
  <div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="会员通知" name="first"> </el-tab-pane>
      <el-tab-pane label="商家通知" name="second"> </el-tab-pane>
      <el-tab-pane label="获取手机号" name="third"> </el-tab-pane>
    </el-tabs>

    <!--会员通知-->
    <Message v-if="activeName == 'first'" :message_to="10"></Message>
    <!--商家通知-->
    <Message v-if="activeName == 'second'" :message_to="20"></Message>
    <!--获取手机号-->
    <GetPhone v-if="activeName == 'third'"></GetPhone>
  </div>
</template>
<script>
import Message from "./Message.vue";
import GetPhone from "./getphone.vue";
export default {
  components: {
    /*编辑组件*/
    Message,
    /*获取手机*/
    GetPhone,
  },
  data() {
    return {
      /*参数*/
      message_to: 10,
      /*当前选中*/
      activeName: "first",
    };
  },
  created() {
    if (this.$route.query.type != null) {
      this.activeName = this.$route.query.type;
    }
  },
  methods: {},
};
</script>
