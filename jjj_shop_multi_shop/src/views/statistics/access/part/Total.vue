<template>
  <div class="d-a-c lh30 ww100 pt16">
    <div class="pt30 tc" style="width: 80px">
      <p class="gray9">今日</p>
      <p class="gray9">昨日</p>
    </div>
    <div class="flex-1 tc">
      <p>访客数</p>
      <p class="f20 fb gray3">{{ dataModel.visitUserT }}</p>
      <p class="gray">{{ dataModel.visitUserY }}</p>
    </div>
    <div class="flex-1 tc">
      <p>访问量</p>
      <p class="f20 fb gray3">{{ dataModel.visitTotalT }}</p>
      <p class="gray">{{ dataModel.visitTotalY }}</p>
    </div>
    <div class="flex-1 tc">
      <p>店铺收藏数</p>
      <p class="f20 fb gray3">{{ dataModel.favStoreT }}</p>
      <p class="gray">{{ dataModel.favStoreY }}</p>
    </div>
    <div class="flex-1 tc">
      <p>商品收藏数</p>
      <p class="f20 fb gray3">{{ dataModel.favProductT }}</p>
      <p class="gray">{{ dataModel.favProductY }}</p>
    </div>
  </div>
</template>
<script>
export default {
  components: {},
  data() {
    return {
      /*是否正在加载*/
      loading: true,
    };
  },
  inject: ["dataModel"],
  created() {},
  methods: {},
};
</script>

<style></style>
