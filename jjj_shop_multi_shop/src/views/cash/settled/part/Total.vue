<template>
  <div class="d-a-c lh30 ww100 pt16">
    <div class="pt30 tc" style="width: 80px;">
      <p class="gray9">今日</p>
      <p class="gray9">昨日</p>
    </div>
    <div class="flex-1 tc">
      <p>商户结算金额（元）</p>
      <p class="f20 fb gray3">{{dataModel.realSupplierMoneyT}}</p>
      <p class="gray">{{dataModel.realSupplierMoneyY}}</p>
    </div>
    <div class="flex-1 tc">
      <p>平台抽成（元）</p>
      <p class="f20 fb gray3">{{dataModel.realSysMoneyT}}</p>
      <p class="gray">{{dataModel.realSysMoneyY}}</p>
    </div>
    <div class="flex-1 tc">
      <p>退款金额（元）</p>
      <p class="f20 fb gray3">{{dataModel.refundMoneyT}}</p>
      <p class="gray">{{dataModel.refundMoneyY}}</p>
    </div>
  </div>
</template>
<script>

export default {
  components:{
  },
  data() {
    return {
      /*是否正在加载*/
      loading: true,

    };
  },
  inject: ['dataModel'],
  created() {

  },
  methods: {


  }
};
</script>

<style>
</style>
