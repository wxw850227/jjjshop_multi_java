<template>
	<view>
		<view class="top-tabbar">
			<view :class="state_active == 0 ? 'tab-item active' : 'tab-item'" @click="stateFunc(0)">全部订单</view>
			<view :class="state_active == 1 ? 'tab-item active' : 'tab-item'" @click="stateFunc(1)">待付款</view>
			<view :class="state_active == 2 ? 'tab-item active' : 'tab-item'" @click="stateFunc(2)">待发货</view>
			<view :class="state_active == 3 ? 'tab-item active' : 'tab-item'" @click="stateFunc(3)">待收货</view>
			<view :class="state_active == 4 ? 'tab-item active' : 'tab-item'" @click="stateFunc(4)">待评价</view>
		</view>
		<!--列表-->
		<scroll-view scroll-y="true" class="scroll-Y" :style="'height:' + scrollviewHigh + 'px;'" lower-threshold="50"
		 @scrolltolower="scrolltolowerFunc">
			<view :class="topRefresh ? 'top-refresh open' : 'top-refresh'">
				<view class="circle" v-for="(circle, n) in 3" :key="n"></view>
			</view>
			<view class="order-list">
				<view class="item" v-for="(item, index) in listData" :key="index">
					<view class="order-head d-b-c">
						<view>
							<text class="state-text">{{ item.orderSourceText }}</text>
							<text class="shop-name flex-1 fb">订单号：{{ item.orderNo }}</text>
						</view>
						<view class="state">
							<text class="red">{{ item.stateText }}</text>
						</view>
					</view>
					<!--多个商品显示-->

					<view class="product-list pr" v-if="item.product.length > 1" @click="jumpPage(item.orderId)">
						<scroll-view scroll-x="true">
							<view class="list d-s-c pr100">
								<view class="cover mr10" v-for="(img, num) in item.product" :key="num">
									<image :src="img.productImage" mode="aspectFit"></image>
								</view>
							</view>
						</scroll-view>
						<view class="total-count">
							<view class="left-shadow"></view>
							<view class="price f22">
								¥
								<text class="f40">{{ item.payPrice}}</text>
							</view>
							<view class="count">共{{ item.product.length }}件</view>
						</view>
					</view>
					<!--一个商品显示-->
					<view class="one-product d-s-c" v-else @click="jumpPage(item.orderId)">
						<view class="cover" v-for="(img, num) in item.product" :key="num">
							<image :src="img.productImage" mode="aspectFit"></image>
						</view>
						<view class="pro-info flex-1">{{ item.product[0].productName }}</view>
						<view class="total-count">
							<view class="left-shadow"></view>
							<view class="price f22">
								¥
								<text class="f40">{{ item.payPrice }}</text>
							</view>
							<view class="count">共{{ item.product[0].totalNum }}件</view>
						</view>
					</view>
					<view class="order-bts">
						<!-- 取消订单 -->
						<block v-if="item.payStatus == 20 &&item.deliveryStatus==10&&item.orderStatus == 10">
							<button  @click="openClose(item.order_no)" type="default">取消订单</button>
						</block>
						<!-- 订单去发货 -->
						<block v-if="item.payStatus == 20 && item.deliveryType == 10 && item.orderStatus==10&&item.deliveryStatus == 10">
							<button class="btn-red-border"  @click="jumpPage(item.orderId)">去发货</button>
						</block>
						<block v-if="item.payStatus == 20 &&item.orderStatus==21">
							<button class="btn-red-border"  @click="jumpPage(item.orderId)">去审核</button>
						</block>
					</view>
				</view>
				<view class="d-c-c p30" v-if="listData.length == 0 && !loading">
					<text class="iconfont icon-wushuju"></text>
					<text class="cont">亲，暂无相关记录哦</text>
				</view>
				<uni-load-more v-else :loadingType="loadingType"></uni-load-more>
			</view>
		</scroll-view>
		<Popup :show="isClose" type="middle" @hidePopup="hideClose">
			<view class="ww100 p20 box-s-b">
				<view class="f32 fb mb20">取消订单</view>
				<view class="d-s-c mb20">
					<view class="w120 f26">订单号：</view>
					<view>{{order_no}}</view>
				</view>
<!-- 				<view class="d-s-s mb20">
					<view class="w120 f26">备注：</view>
					<textarea v-model="cancelRemark" placeholder="请输入备注" class="border p10 w400" />
				</view> -->
				<view class="d-c-c">
					<button class="send_btn btn-gray-border mr30" @click="hideClose">取消</button>
					<button class="send_btn btn-orange" @click="sendClose()">确定</button>
				</view>
			</view>
		</Popup>
	</view>
</template>

<script>
	import uniLoadMore from '@/components/uni-load-more.vue';
	import Popup from '@/components/uni-popup.vue';
	export default {
		components: {
			uniLoadMore,
			Popup
		},
		data() {
			return {
				/*手机高度*/
				phoneHeight: 0,
				/*可滚动视图区域高度*/
				scrollviewHigh: 0,
				/*状态选中*/
				state_active: 0,
				/*顶部刷新*/
				topRefresh: false,
				/*数据*/
				listData: [],
				/*数据类别*/
				dataType: 'all',
				/*订单id*/
				order_id: 0,
				/*最后一页码数*/
				last_page: 0,
				/*当前页面*/
				page: 1,
				/*每页条数*/
				list_rows: 10,
				/*有没有等多*/
				no_more: false,
				/*是否正在加载*/
				loading: true,
				shopSupplierId: '',
				isClose:false,
				cancel_remark:'',
				order_no:''
			};
		},
		computed: {
			/*加载中状态*/
			loadingType() {
				if (this.loading) {
					return 1;
				} else {
					if (this.listData.length != 0 && this.no_more) {
						return 2;
					} else {
						return 0;
					}
				}
			}
		},
		onLoad(e) {
			this.shopSupplierId = e.shopSupplierId;
			if (typeof e.dataType != 'undefined') {
				this.dataType = e.dataType;
			}
			if (this.dataType == 'payment') {
				this.state_active = 1;
			} else if (this.dataType == 'delivery') {
				this.state_active = 2;
			} else if (this.dataType == 'received') {
				this.state_active = 3;
			}
		},
		mounted() {
			this.init();
			
		},
		onShow() {
			this.initData();
			/*获取订单列表*/
			this.getData();
		},
		methods: {
			/*初始化*/
			init() {
				let self = this;
				uni.getSystemInfo({
					success(res) {
						self.phoneHeight = res.windowHeight;
						// 计算组件的高度
						let view = uni.createSelectorQuery().select('.top-tabbar');
						view.boundingClientRect(data => {
							let h = self.phoneHeight - data.height;
							self.scrollviewHigh = h;
						}).exec();
					}
				});
			},
			initData(){
				let self = this;
				self.page = 1;
				self.loading = true;
				self.listData = [];
				self.no_more=false;
			},
			/*状态切换*/
			stateFunc(e) {
				let self = this;
				if (self.state_active != e) {
					self.page = 1;
					self.loading = true;
					self.state_active = e;
					switch (e) {
						case 0:
							self.listData = [];
							self.dataType = 'all';
							break;
						case 1:
							self.listData = [];
							self.dataType = 'payment';
							break;
						case 2:
							self.listData = [];
							self.dataType = 'delivery';
							break;
						case 3:
							self.listData = [];
							self.dataType = 'received';
							break;
						case 4:
							self.listData = [];
							self.dataType = 'comment';
							break;
					}
					self.getData();
				}
			},

			/*可滚动视图区域到底触发*/
			scrolltolowerFunc() {
				let self = this;
				if (self.no_more) {
					return;
				}
				self.page++;
				if (self.page <= self.last_page) {
					self.getData();
				} else {
					self.no_more = true;
				}
			},

			/*获取数据*/
			getData() {
				let self = this;
				self.loading = true;
				let dataType = self.dataType;
				self._postBody(
					'supplier/order/index', {
						shopSupplierId: self.shopSupplierId,
						type: dataType,
						pageIndex: self.page,
						pageSize: self.list_rows,
						paySource: self.getPlatform()
					},
					function(res) {
						self.loading = false;
						self.listData = self.listData.concat(res.data.records);
						self.last_page = res.data.lastPage;
						if (res.data.lastPage <= 1) {
							self.no_more = true;
						} else {
							self.no_more = false;
						}
					}
				);
			},

			/*跳转页面*/
			jumpPage(e) {
				this.gotoPage('/pages/order/order-detail?source=supplier&orderId=' + e);
			},
			/* 打开取消订单 */
			openClose(e) {
				this.isClose = true;
				this.order_no=e;
			},
			sendClose(){
				let self = this;
				let order_no = self.order_no;
				wx.showModal({
					title: '提示',
					content: '您确定要取消订单吗?',
					success: function(o) {
						if (o.confirm) {
							self.isClose = false;
							uni.showLoading({
								title: '正在处理'
							});
							self._get(
								'supplier/order/orderCancel', {
									order_no: order_no,
									cancel_remark:self.cancel_remark
								},
								function(res) {
									uni.hideLoading();
									uni.showToast({
										title: '操作成功',
										duration: 2000,
										icon: 'success'
									});
									self.listData = [];
									self.getData();
								}
							);
						}
					}
				});
			},
			/*关闭取消订单 */
			hideClose(e) {
				this.isClose = false;
			},
		}
	};
</script>

<style lang="scss">
	.order-list .order-head .state-text {
		padding: 4rpx 8rpx;
		margin-right: 10rpx;
		border-radius: 4rpx;
		background: #e2231a;
		color: #ffffff;
	}

	.order-list .item {
		margin-top: 30rpx;
		padding: 30rpx;
		background: #ffffff;
	}

	.order-list .product-list,
	.order-list .one-product {
		padding: 20rpx 0;
		height: 160rpx;
	}

	.one-product .pro-info {
		padding: 0 30rpx;
		display: -webkit-box;
		overflow: hidden;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
		font-size: 28rpx;
		color: #666666;
	}

	.order-list .cover,
	.order-list .cover image {
		width: 160rpx;
		height: 160rpx;
	}

	.order-list .total-count {
		padding-left: 20rpx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: flex-end;
	}

	.total-count .count {
		padding-top: 10rpx;
		color: #666;
		font-size: 28rpx;
	}

	.product-list .total-count {
		position: absolute;
		top: 0;
		right: 0;
		bottom: 0;
		background: rgba(255, 255, 255, 0.9);
	}

	.product-list .total-count .left-shadow {
		position: absolute;
		top: 0;
		bottom: 0;
		left: -24rpx;
		width: 24rpx;
		overflow: hidden;
	}

	.product-list .total-count .left-shadow::after {
		position: absolute;
		top: 0;
		bottom: 0;
		width: 24rpx;
		right: -12rpx;
		display: block;
		content: '';
		background-image: radial-gradient(rgba(0, 0, 0, 0.2) 10%, rgba(0, 0, 0, 0.1) 40%, rgba(0, 0, 0, 0) 80%);
	}

	.order-list .order-bts {
		display: flex;
		justify-content: flex-end;
		align-items: center;
	}

	.order-list .order-bts button {
		margin: 0;
		padding: 0 30rpx;
		height: 60rpx;
		line-height: 60rpx;
		margin-left: 20rpx;
		border-radius: 30rpx;
		font-size: 24rpx;
		border: 1px solid #cccccc;
		white-space: nowrap;
		background: #ffffff;
	}

	.order-list .order-bts button::after {
		display: none;
	}

	.order-list .order-bts button.btn-border-red {
		border: 1px solid $dominant-color;
		font-size: 24rpx;
		color: $dominant-color;
	}

	.order-list .order-bts button.btn-red {
		background: $dominant-color;
		border: 1px solid $dominant-color;
		font-size: 24rpx;
		color: #ffffff;
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

	.item-dianpu {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-bottom: 30rpx;
		font-size: 24rpx;
		line-height: 30rpx;
	}

	.item-d-l {
		display: flex;
	}

	.icon-dianpu1 {
		margin-right: 30rpx;
	}
</style>
