<template>
	<view class="index">
		<view class="head">
			<view class="h1">当前申请状态</view>
			<view v-if="detail.status==0" class="h1 orange">审核中</view>
			<view v-if="detail.status==1" class="h1 green">审核通过</view>
			<view v-if="detail.status==2" class="h1 red">审核失败</view>
		</view>
		<view class="body">
			<view class="body_hint h3">客服会尽快进行审核</view>
			<view class="progress_bar">
				<progress v-if="detail.status==0" activeColor='#24DB5A' backgroundColor="#A2A2A2" percent="50" stroke-width="6" />
				<progress v-if="detail.status==1" activeColor='#24DB5A' backgroundColor="#A2A2A2" percent="100" stroke-width="6" />
				<progress v-if="detail.status==2" activeColor='#24DB5A' backgroundColor="#A2A2A2" percent="100" stroke-width="6" />
			</view>
			<view class="progress_bar_image">
				<view class="progress_bar_image_items">
					<view class="icon iconfont icon-edit progress_bar_image_item bg_green"></view>
					<view class="h3">已提交</view>
				</view>
				<view class="progress_bar_image_items">
					<view class="icon iconfont icon-gengduo progress_bar_image_item bg_green"></view>
					<view v-if="detail.status==0" class="h3 orange">待审核</view>
					<view v-if="detail.status==1" class="h3">审核中</view>
					<view v-if="detail.status==2" class="h3">审核中</view>
				</view>
				<view class="progress_bar_image_items">
					<view v-if="detail.status==0" class="icon iconfont icon-ic_check progress_bar_image_item bg_gray"></view>
					<view v-if="detail.status==1" class="icon iconfont icon-ic_check progress_bar_image_item bg_green"></view>
					<view v-if="detail.status==2" class="icon iconfont icon-guanbi progress_bar_image_item bg_red"></view>
					<view v-if="detail.status==0" class="h3">待审核</view>
					<view v-if="detail.status==1" class="h3 green">审核通过</view>
					<view v-if="detail.status==2" class="h3 red">审核失败</view>
				</view>
			</view>
			<view class="return">
				<view v-if="detail.status==1" class="h1">恭喜，您的入驻审核已通过！</view>
				<view v-if="detail.status==2" class="h1">抱歉，您的入驻审核未通过！</view>
				<view v-if="detail.status==2" class="h3">未通过原因：{{detail.content}}</view>
				<button v-if="detail.status==2" @click="gotoReg()" class="btn1" type="default">重新申请</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				// 审核详情
				detail: {}
			}
		},
		mounted() {
			/*获取个人中心数据*/
			this.getData();
		},
		methods: {
			/*获取数据*/
			getData() {
				let self = this;
				self._post('supplier/apply/detail', {}, function(res) {
					self.detail = res.data;
				});
			},
			//重新注册
			gotoReg() {
				this.$emit("reset-form")
				// this.gotoPage('/pages/shop/register');
			},
		}
	}
</script>

<style lang="scss" scoped>
	.index {
		width: 750rpx;
	}

	.h1 {
		font-size: 32rpx;
		color: #585858;
		font-weight: bold;
	}

	.h2 {
		font-size: 28rpx;
		color: #585858;
	}

	.h3 {
		font-size: 24rpx;
		color: #585858;
	}

	.h4 {
		font-size: 20rpx;
		color: #585858;
	}

	.iconfont {
		color: white;
	}

	.red {
		color: #E22319;
	}

	.gray {
		color: #585858;
	}

	.green {
		color: #24DB5A;
	}

	.orange {
		color: #FF7E00;
	}

	.bg_gray {
		background-color: #A2A2A2;
	}

	.bg_red {
		// background-color: #E22319;
		background-color: rgb(243,106,36);
	}

	.bg_green {
		background-color: #24DB5A;
	}

	.bg_orange {
		background-color: #FF7E00;
	}

	.head {
		width: 750rpx;
		height: 100rpx;
		background-color: #ECECEC;
		display: flex;
		justify-content: space-between;
		padding: 20rpx 40rpx;
		box-sizing: border-box;
		align-items: center;
		box-shadow: 0 0 10rpx #585858;
	}

	.body_hint {
		width: 500rpx;
		height: 50rpx;
		line-height: 50rpx;
		margin: 0 auto;
		background-color: #FFF7E2;
		text-align: center;
		margin-top: 50rpx;
	}

	.progress_bar {
		width: 450rpx;
		margin: 0 auto;
		margin-top: 50rpx;
	}

	.progress_bar_image {
		width: 500rpx;
		margin: 0 auto;
		display: flex;
		justify-content: space-between;
		position: relative;
		top: -40rpx;
	}

	.progress_bar_image_items {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}

	.progress_bar_image_item {
		width: 72rpx;
		height: 72rpx;
		border-radius: 50%;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-bottom: 15rpx;
	}


	.return {
		width: 750rpx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		margin-top: 100rpx;
	}

	.return view {
		margin-bottom: 20rpx;
	}

	.return_title {}

	.return_title2 {}

	.return .btn1 {
		width: 300rpx;
		height: 65rpx;
		line-height: 65rpx;
		border-radius: 50rpx;
		color: white;
		background-color: #FF7E00;
		margin-top: 50rpx;
		font-size: 30rpx;
	}

	.return .btn2 {
		width: 300rpx;
		height: 65rpx;
		line-height: 65rpx;
		border-radius: 50rpx;
		color: white;
		background-color: #24DB5A;
		margin-top: 50rpx;
		font-size: 30rpx;
	}

	.pay_window {
		width: 100%;
		height: 100%;
		position: absolute;
		top: 0;
		left: 0;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 999;
	}

	.pay_window_box {
		width: 70%;
		height: 500rpx;
		margin: 0 auto;
		background-color: white;
		position: relative;
		top: 200rpx;
		padding: 50rpx;
		border-radius: 30rpx;
	}

	.buy-checkout {
		width: 100%;
	}

	.buy-checkout .item {
		min-height: 50rpx;
		line-height: 50rpx;
		padding: 20rpx;
		display: flex;
		justify-content: space-between;
		font-size: 28rpx;
	}

	.buy-checkout .iconfont.icon-weixin {
		color: #04be01;
		font-size: 50rpx;
	}

	.buy-checkout .iconfont.icon-yue {
		color: #f0de7c;
		font-size: 50rpx;
	}

	.buy-checkout .item.active .iconfont.icon-xuanze {
		color: #04be01;
	}

	.pay-bt {
		padding-top: 130rpx;
		padding-bottom: 60rpx;
		display: flex;
		justify-content: center;
		width: 100%;
	}

	.pay-bts {
		width: 225rpx;
		height: 80rpx;
		line-height: 80rpx;
		text-align: center;
		padding: 0;
		font-size: 32rpx;
		border-radius: 40rpx;
		color: #FFFFFF;
		background-color: #04be01;
	}

	.icon-tijiaochenggong {
		color: #04be01;
	}
</style>
