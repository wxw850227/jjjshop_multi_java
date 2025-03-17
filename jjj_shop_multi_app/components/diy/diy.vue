<template>
	<view>
		<block v-for="(item,index) in diyItems" :key="index">
			<!--轮播图-->
			<template v-if="item.type==='banner'&&item.data!=null">
				<banner :itemData="item"></banner>
			</template>

			<!--单组图-->
			<template v-if="item.type==='imageSingle'&&item.data!=null">
				<imagesingle :itemData="item"></imagesingle>
			</template>

			<!-- 橱窗-->
			<template v-if="item.type == 'window'&&item.data!=null">
				<windows :itemData="item"></windows>
			</template>

			<!-- 视频组-->
			<template v-if="item.type == 'video'">
				<videos :itemData="item"></videos>
			</template>

			<!--文章-->
			<template v-if="item.type == 'article'&&item.data!=null">
				<articles :itemData="item"></articles>
			</template>

			<!--头条快报-->
			<template v-if="item.type == 'special'&&item.data!=null">
				<special :itemData="item"></special>
			</template>

			<!--公告组-->
			<template v-if="item.type == 'notice'">
				<notice :itemData="item"></notice>
			</template>
			<!--标题组-->
			<template v-if="item.type == 'title'">
				<titles :itemData="item"></titles>
			</template>
			<!--导航组-->
			<template v-if="item.type==='navBar'&&item.data!=null">
				<navBar :itemData="item"></navBar>
			</template>

			<!--商品组-->
			<template v-if="item.type==='product'&&item.data!=null">
				<product :itemData="item"></product>
			</template>

			<!--门店-->
			<template v-if="item.type == 'store'&&item.data!=null">
				<store :itemData="item"></store>
			</template>

			<!--客服-->
			<template v-if="item.type == 'service'">
				<service :itemData="item"></service>
			</template>

			<!--富文本-->
			<template v-if="item.type==='richText'">
				<richText :itemData="item"></richText>
			</template>

			<!--辅助空白-->
			<template v-if="item.type == 'blank'">
				<blank :itemData="item"></blank>
			</template>

			<!--辅助线-->
			<template v-if="item.type == 'guide'">
				<guide :itemData="item"></guide>
			</template>

		</block>
	</view>
</template>

<script>
	import banner from './banner/banner';
	import imagesingle from './imagesingle/imagesingle';
	import windows from './window/window';
	import videos from './videos/videos';
	import articles from './article/article';
	import special from './special/special';
	import notice from './notice/notice';
	import titles from './title/title';
	import richText from './richText/richText';
	import navBar from './navBar/navBar';
	import store from './store/store';
	import service from './service/service';
	import blank from './blank/blank';
	import guide from './guide/guide';
	import product from './product/product';

	export default {
		components: {
			banner,
			imagesingle,
			windows,
			videos,
			articles,
			special,
			notice,
			titles,
			richText,
			navBar,
			store,
			service,
			blank,
			guide,
			product
		},
		data() {
			return {

			}
		},
		props: ['diyItems'],
		created() {}
	}
</script>

<style>
</style>
