<template>
  <div>
    <div class="common-form">
      <span>{{ curItem.name }}</span>
    </div>
    <el-form size="small" :model="curItem" label-width="100px">
      <!--显示数量-->
      <el-form-item label="显示数量：">
        <el-input v-model="curItem.params.showNum" class="w-auto"></el-input>
      </el-form-item>
      <!-- 文字颜色 -->
      <el-form-item label="时间颜色：">
        <div class="d-s-c">
          <el-color-picker v-model="curItem.style.color"></el-color-picker>
          <el-button
            type="button"
            style="margin-left: 10px"
            @click.stop="
              $parent.onEditorResetColor(curItem.style, 'color', '#FF4C01')
            "
            >重置
          </el-button>
        </div>
      </el-form-item>
      <el-form-item label="日期文字：">
        <div class="d-s-c">
          <el-color-picker
            v-model="curItem.style.countdownColor"
          ></el-color-picker>
          <el-button
            type="button"
            style="margin-left: 10px"
            @click.stop="
              $parent.onEditorResetColor(
                curItem.style,
                'countdown_color',
                '#ffffff'
              )
            "
            >重置
          </el-button>
        </div>
      </el-form-item>
      <el-form-item label="日期背景：">
        <div class="d-s-c">
          <el-color-picker
            v-model="curItem.style.countdownBackColor"
          ></el-color-picker>
          <el-button
            type="button"
            style="margin-left: 10px"
            @click.stop="
              $parent.onEditorResetColor(
                curItem.style,
                'countdownBackColor',
                '#FF4C01'
              )
            "
            >重置
          </el-button>
        </div>
      </el-form-item>
      <el-form-item label="背景图片：">
        <div class="diy-notice-icon">
          <img
            v-img-url="curItem.style.backgroundImage"
            width="350"
            height="45"
            alt=""
            @click="
              $parent.onEditorSelectImage(curItem.style, 'backgroundImage')
            "
          />
        </div>
        <div>建议尺寸700×90</div>
      </el-form-item>
      <!--组件样式-->
      <div class="p-10-0 mb16 f14 border-b">
        <span class="gray6">组件样式</span>
      </div>
      <!-- 商品排序 -->
      <el-form-item label="显示内容：">
        <el-checkbox v-model="curItem.style.show.seckillPrice"
          >秒杀价格</el-checkbox
        >
        <el-checkbox v-model="curItem.style.show.linePrice"
          >划线价格</el-checkbox
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
  props: ["curItem", "selectedIndex"],
  created() {},
  methods: {},
};
</script>

<style scoped></style>
