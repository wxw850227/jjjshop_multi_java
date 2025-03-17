<template>
	<view class="weidian">
		<view class="weidian_head">
			<!-- #ifdef MP-WEIXIN || APP-PLUS -->
			<view class="ww100" :style="'height:'+topBarTop()+'px;'"></view>
			<view class="tc  head_top" :style="topBarHeight() == 0 ? '': 'height:'+topBarHeight()+'px;'">
				<view class="reg180" @click="goback"><text class="icon iconfont icon-jiantou"></text></view>
				<view class="fb">我的店铺</view>
			</view>
			<!-- #endif -->
			<view>
				<view class="weidian_head_body_bottom">
					<view class="weidian_head_body_bottom_item">
						<view class="f24 gray3 mb20">累计提现(元)</view>
						<text class="f36 gray3 fb">{{shop_data.supplier.cashMoney}}</text>
					</view>
					<view class="weidian_head_body_bottom_item">
						<view class="f24 gray3 mb20">累计成交(笔)</view>
						<text class="f36 gray3 fb">{{shop_data.totalCount}}</text>
					</view>
					<view class="weidian_head_body_bottom_item">
						<view class="f24 gray3 mb20">今日成交(笔)</view>
						<text class="f36 gray3 fb">{{shop_data.todayCount}}</text>
					</view>
					<view class="weidian_head_body_bottom_item">
						<view class="f24 gray3 mb20">可提现</view>
						<text class="f36 gray3 fb">{{shop_data.supplier.money}}</text>
					</view>
				</view>
			</view>
		</view>
		<view class="weidian_body">
			<view class="seller_center_body_order">
				<view class="seller_center_body_order_head">
					<text class="f32 gray3 fb">我的订单</text>
					<text class="seller_center_body_order_head_more" @click="jumpPage('/pages/user/my_shop/my_shop_order?fill=1')">全部订单<text
						 class="icon iconfont icon-jiantou f24"></text></text>
				</view>
				<view class="seller_center_body_order_body">
					<view class="seller_center_body_order_body_item" v-for="(item,index) in order_menu_list" :key="index">
						<view @click="jumpPage(item.url)">
							<image :src="item.image" mode=""></image>
							<view class="f26 gray3">{{item.title}}</view>
						</view>
					</view>
				</view>
			</view>
			<view class="weidian_body_menu_list">
				<view class="weidian_body_menu_list_item" v-for="(item,index) in menu_list" :key="index" @click="jumpPage(item.url)">
					<view class="weidian_body_menu_list_left_icon">
						<image class="left_iconimg" :src="item.left_icon" mode=""></image>
					</view>
					<view class="weidian_body_menu_list_title_left">{{item.title_left}}</view>
					<view class="weidian_body_menu_list_right_icon icon iconfont icon-jiantou"></view>
					<view class="weidian_body_menu_list_title_right"></view>
				</view>
				<view class="weidian_body_menu_list_item">
					<view class="weidian_body_menu_list_left_icon">
						<image class="left_iconimg" src="../../../static/icon/store_deposit.png" mode=""></image>
					</view>
					<view class="weidian_body_menu_list_title_left">店铺保证金</view>
					<view class="weidian_body_menu_list_right_icon icon iconfont icon-jiantou"></view>
					<view class="weidian_body_menu_list_title_right" v-if="shop_data.supplier.status==0">已缴纳</view>
					<view class="weidian_body_menu_list_title_right" v-if="shop_data.supplier.status==10">申请退出</view>
					<view class="weidian_body_menu_list_title_right" v-if="shop_data.supplier.status==20" @click="gotoPay">立即交纳</view>
				</view>
				<view v-if="is_open==1" class="weidian_body_menu_list_item" @click="toLive()">
					<view class="weidian_body_menu_list_left_icon">
						<image class="left_iconimg" src="../../../static/icon/store_live.png" mode=""></image>
					</view>
					<view class="weidian_body_menu_list_title_left">我的直播</view>
					<view class="weidian_body_menu_list_right_icon icon iconfont icon-jiantou"></view>
				</view>
<!--				<view class="weidian_body_menu_list_item" @click="gotoPage('/pages/plus/chat/supplier_chat_list')">-->
<!--					<view class="weidian_body_menu_list_left_icon">-->
<!--						<image class="left_iconimg" src="../../../static/icon/store_live.png" mode=""></image>-->
<!--					</view>-->
<!--					<view class="weidian_body_menu_list_title_left">客服消息</view>-->
<!--					<view class="weidian_body_menu_list_right_icon icon iconfont icon-jiantou"></view>-->
<!--					<view class="weidian_body_menu_list_title_right">{{msg_count}}</view>-->
<!--				</view>-->
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				order_menu_list: [{
						image: '../../../static/icon/pay.png',
						title: '待支付',
						url: '/pages/user/my_shop/my_shop_order?dataType=payment',
					},
					{
						image: '../../../static/icon/daifahuo.png',
						title: '待发货',
						url: '/pages/user/my_shop/my_shop_order?dataType=delivery',
					},
					{
						image: '../../../static/icon/daishouhuo.png',
						title: '待收货',
						url: '/pages/user/my_shop/my_shop_order?dataType=received',
					},
					{
						image: '../../../static/icon/sell.png',
						title: '退款/售后',
						url: '/pages/user/my_shop/after_sale?tianchong=1',
					},
				],
				menu_list: [{
						title_left: '店铺首页',
						title_right: '',
						left_icon: '../../../static/icon/store_home.png',
						right_icon: '',
						url: '/pages/shop/shop'
					},
					{
						title_left: '店铺数据',
						title_right: '',
						left_icon: '../../../static/icon/store_data.png',
						right_icon: '',
						url: '/pages/user/my_shop/my_shop_data'
					},
					{
						title_left: '商品管理',
						title_right: '',
						left_icon: '../../../static/icon/commodity_management.png',
						right_icon: '',
						url: '/pages/user/my_shop/pro_admin'
					}
				],
				shop_data: {
					supplier: {}
				},
				user_id: '',
				shop_supplier_id: '',
				is_open: 0,
				is_full: 0,
				statusBarHeight: 0,
				titleBarHeight:0,
				msg_count: 0
			}
		},
		onShow() {
			this.get_user_Data();
			this.get_shop_Data();
		},
		onLoad() {
			this.GetStatusBarHeight();
		},
		methods: {
			GetStatusBarHeight() {
				const SystemInfo = uni.getSystemInfoSync();
				// #ifdef MP-WEIXIN
				let statusBarHeight = SystemInfo.statusBarHeight;
				this.statusBarHeight = uni.getMenuButtonBoundingClientRect().top;
				this.titleBarHeight = uni.getMenuButtonBoundingClientRect().height;
				// #endif
				// #ifndef MP-WEIXIN
				this.statusBarHeight = SystemInfo.statusBarHeight;
				this.titleBarHeight = 30
				// #endif
			},
			jumpPage(path) {
				if(path.indexOf('?') != -1){
					this.gotoPage(path + '&shopSupplierId=' + this.shop_supplier_id);
				}else{
					this.gotoPage(path + '?shopSupplierId=' + this.shop_supplier_id);
				}
			},
			// 获取用户数据
			get_user_Data() {
				let self = this;
				uni.showLoading({
					title: '加载中'
				})
				self._post('user/index/index', {

				}, function(res) {
					self.user_id = res.data.user.userId;
					self.shop_supplier_id = res.data.supplierUser.shopSupplierId;
					//self.menu_list[0].url = '/pages/shop/shop?shopSupplierId=' + self.shop_supplier_id;
					uni.hideLoading()
				})
			},
			// 获取店铺成交数据
			get_shop_Data() {
				let self = this;
				self._post('supplier/index/tradeData', {}, function(res) {
					self.shop_data = res.data;
					console.log(self.shop_data.supplier.cashMoney);
					self.is_open = res.data.isOpen;
					self.is_full = res.data.supplier.isFull;
					//self.msg_count = res.data.msg_count;
				})
			},
			goback() {
				uni.navigateBack();
			},
			// 支付保证金
			gotoPay() {
				this.gotoPage('/pages/order/deposit-pay');
			},
			toLive() {
				let self = this;
				//if (this.is_full == 1) {
				this.gotoPage('/pagesLive/my_live/my-live/my-live?shopSupplierId='+self.shop_supplier_id)
				/*} else {
					uni.showModal({
						title: '提示',
						content: '请先前往后台商户PC端完善个人资料并添加商品'
					})
				}*/
			},
		}
	}
</script>

<style>
	page {
		background: #FFFFFF;
	}

	.iconfont {
		font-size: 14px;
	}

	.reg180 {
		padding-right: 20rpx;
		text-align: right;
		transform: rotateY(180deg);
		position: absolute;
		bottom: 0;
	}

	.head_top .icon-jiantou {
		color: #333333;
		font-size: 30rpx;
	}

	.head_top {
		width: 100%;
		position: relative;
		/* padding-top: var(--status-bar-height); */
		height: 30px;
		line-height: 30px;
		color: #333333;
		font-size: 36rpx;
	}

	.bg_topimg {
		position: absolute;
		top: 0;
		width: 100%;
		height: 400rpx;
		z-index: -1;
	}

	.weidian {}


	.weidian_head {
		width: 100%;
		padding-bottom: 195rpx;
		/* height: 360rpx; */
		background: linear-gradient(-90deg, #FFC89B 0%, #FFE3C4 100%);
	}

	.weidian_body {
		/* margin-top: -125rpx; */
		position: relative;
	}

	.weidian_head_body_top {
		width: 100%;
		height: 200rpx;
	}

	.weidian_head_body_top_left {
		width: 50%;
		height: 200rpx;
		display: inline-block;
		font-size: 30rpx;
		color: white;
		padding-top: 40rpx;
		padding-left: 45rpx;
		box-sizing: border-box;
	}

	.weidian_head_body_top_left view {
		margin-top: 20rpx;
	}

	.weidian_head_body_top_left image {
		width: 36rpx;
		height: 36rpx;
		display: inline-block;
	}

	.weidian_head_body_top_right {
		width: 30%;
		display: inline-block;
		position: relative;
		top: -25rpx;
		left: 120rpx;
	}

	.weidian_head_body_top_right button {
		width: 82%;
		height: 60rpx;
		line-height: 60rpx;
		border-radius: 30rpx;
		color: #F36A23;
	}

	.weidian_head_body_bottom {
		width: 100%;
		padding-top: 72rpx;
		box-sizing: border-box;
		font-size: 26rpx;
		display: flex;
		justify-content: space-around;
		align-items: center;
	}

	.weidian_head_body_bottom_item {
		display: inline-block;
		float: left;
		color: white;
		text-align: center;
	}

	/* 我的订单css */
	.seller_center_body_order {
		width: 710rpx;
		background-color: white;
		margin: 0 auto;
		border-radius: 20rpx;
		padding: 30rpx 24rpx 30rpx 30rpx;
		box-sizing: border-box;
		position: absolute;
		top: -170rpx;
		left: 0;
		right: 0;
	}

	.seller_center_body_order_head {
		font-size: 30rpx;
		border-bottom: 1rpx solid #D9D9D9;
		padding-bottom: 20rpx;
		display: flex;
		justify-content: space-between;
		align-items: flex-end;
	}

	.seller_center_body_order_head_more {
		font-size: 26rpx;
		color: #666666;
		display: flex;
		align-items: center;
	}

	.seller_center_body_order_head_more .icon-jiantou {
		font-size: 24rpx;
		color: #666666;
	}

	.seller_center_body_order_body {
		width: 100%;
		padding-top: 30rpx;
	}

	.seller_center_body_order_body_item {
		display: inline-block;
		width: 25%;
		text-align: center;
	}

	.seller_center_body_order_body image {
		width: 60rpx;
		height: 60rpx;
		margin: 0 auto;
		margin-bottom: 14rpx;
	}

	/* 订单css  END */
	.weidian_body_menu_list {
		/* width: 90%; */
		padding-left: 50rpx;
		font-size: 30rpx;
		color: #484848;
		background-color: #FFFFFF;
		border-top: 120rpx solid #f2f2f2;
	}

	.weidian_body_menu_list_item {
		width: 97%;
		height: 100rpx;
		line-height: 100rpx;
		border-bottom: 1rpx #D9D9D9 solid;
		box-sizing: border-box;
	}

	.weidian_body_menu_list_title_left {
		display: inline-block;
		float: left;
	}

	.weidian_body_menu_list_right_icon {
		display: inline-block;
		float: right;
		font-size: 26rpx;
	}

	.weidian_body_menu_list_left_icon {
		display: inline-block;
		float: left;
		margin-right: 10rpx;
	}

	.weidian_body_menu_list_title_right {
		display: inline-block;
		float: right;
		color: #d5d5d5;
		font-size: 24rpx;
		position: relative;
		/* top: 5rpx; */
		margin-right: 5rpx;
	}

	.left_iconimg {
		width: 36rpx;
		height: 36rpx;
		display: inline-block;
	}
</style>
