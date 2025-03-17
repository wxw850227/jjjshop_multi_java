<template>
	<view :data-theme="theme()" :class="theme() || ''" class="article-detail" v-if="loadding">
		<view class="title fb">{{ article.articleTitle }}</view>
		<view class="info d-b-c f24">
			<view>
				<text class="red">{{ article.categoryName }}</text>
				<text class="ml30">{{ article.createTime }}</text>
			</view>
			<button class="share-box" open-type="share" @click="shareFunc"><image class="share_img" src="/static/icon/fx.png" mode=""></image></button>
		</view>
		<view class="article-content" v-html="article.articleContent"></view>
		<tabBar></tabBar>
	</view>
</template>

<script>
import utils from '@/common/utils.js';

export default {
	components: {

	},
	data() {
		return {
			/*是否加载完成*/
			loadding: false,
			indicatorDots: true,
			autoplay: true,
			interval: 2000,
			duration: 500,
			/*文章id*/
			articleId: 0,
			/*文章详情*/
			article: {
				image: {}
			}
		};
	},
	onLoad(e) {
		/*分类id*/
		this.articleId = e.articleId;
		//#ifdef H5
		this.url = window.location.href;
		//#endif
	},
	mounted() {
		/*获取产品详情*/
		this.getData();
	},
	/*分享*/
	onShareAppMessage() {
		let self = this;
		// 构建页面参数
		let params = self.getShareUrlParams({
			articleId: self.articleId
		});
		self.taskFunc();
		return {
			title: self.article.articleTitle,
			path: '/pages/article/detail/detail?' + params
		};
	},
	methods: {
		taskFunc() {
			let self = this;
		},
		shareFunc() {
			let self = this;
			//this.taskFunc();
			//#ifdef H5
			self.copyUrl();
			//#endif
			//#ifdef APP-PLUS
			self.appParams.title = self.detail.productName;
			self.appParams.summary = self.detail.productName;
			// 构建页面参数
			let params = self.getShareUrlParams({
				articleId: self.articleId
			});
			self.appParams.path = '/pages/article/detail/detail?' + params;
			self.appParams.image = self.detail.image[0].filePath;
			self.isAppShare = true;
			//#endif
		},
		/*复制*/
		copyUrl() {
			var input = document.createElement('input');
			let message = window.location.href;
			input.value = message;
			document.body.appendChild(input);
			input.select();
			input.setSelectionRange(0, input.value.length), document.execCommand('Copy');
			document.body.removeChild(input);
			uni.showToast({
				title: '复制成功',
				icon: 'success',
				mask: true,
				duration: 2000
			});
		},
		//关闭分享
		closeAppShare(data) {
			this.isAppShare = false;
		},
		/*获取文章详情*/
		getData() {
			let self = this;
			this.taskFunc();
			uni.showLoading({
				title: '加载中'
			});
			self.loading = true;
			let articleId = self.articleId;
			self._get(
				'plus/article/article/detail',
				{
					articleId: articleId
				},
				function(res) {
					/*详情内容格式化*/
					res.data.articleContent = utils.format_content(res.data.articleContent);
					console.log(res.data.articleContent);
					self.article = res.data;
					self.loadding = true;
					uni.hideLoading();
				}
			);
		}
	}
};
</script>

<style>
.article-detail {
	padding: 30rpx;
	background: #ffffff;
}

.article-detail .title {
	font-size: 44rpx;
}

.article-detail .info {
	padding: 40rpx 0;
	color: #999999;
}

.article-detail .article-content {
	width: 100%;
	box-sizing: border-box;
	line-height: 60rpx;
	font-size: 34 rpx;
	overflow: hidden;
}
.article-detail .article-content image,
.article-detail .article-content img {
	display: block;
	max-width: 100%;
}
.share-box {
	background: none;
}
.share_img {
	width: 32rpx;
	height: 32rpx;
}
</style>
