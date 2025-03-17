<template>
	<view class="refund-list">
		<view class="top-tabbar">
			<view :class="state_active == 1 ? 'tab-item active' : 'tab-item'" @click="stateFunc(1)">售后</view>
			<view :class="state_active == 2 ? 'tab-item active' : 'tab-item'" @click="stateFunc(2)">退款</view>
			<!-- <view :class="state_active == -1 ? 'tab-item active' : 'tab-item'" @click="stateFunc(-1)">售后</view>
			<view :class="state_active == 0 ? 'tab-item active' : 'tab-item'" @click="stateFunc(0)">待处理</view>
			<view :class="state_active == 'cancel' ? 'tab-item active' : 'tab-item'" @click="stateFunc('cancel')">已取消</view>
			<view :class="state_active == 'cancelApply' ? 'tab-item active' : 'tab-item'" @click="stateFunc('cancelApply')">申请中</view> -->
		</view>
		<scroll-view scroll-y="true" class="scroll-Y" :style="'height:' + scrollviewHigh + 'px;'" lower-threshold="50"
			@scrolltoupper="scrolltoupperFunc" @scrolltolower="scrolltolowerFunc">
			<view :class="topRefresh ? 'top-refresh open' : 'top-refresh'">
				<view class="circle" v-for="(circle, n) in 3" :key="n"></view>
			</view>
			<view class="list" v-if="state_active == 1">
				<view class="item bg-white p30 mb20" v-for="(item, index) in tableData" :key="index">
					<view class="d-b-c">
						<text>{{ item.createTime }}</text>
						<text class="red">{{ item.stateText }}</text>
					</view>
					<view class="one-product d-s-c pt20">
						<view class="cover">
							<image :src="item.orderProduct.productImage" mode="aspectFit"></image>
						</view>
						<view class="flex-1">
							<view class="pro-info">{{ item.orderProduct.productName }}</view>
							<view class="pt10 p-0-30">
								<text class="f24 gray9">{{item.orderProduct.productAttr}}</text>
							</view>
						</view>
					</view>
					<view class="d-e-c pt20">
						<view>
							商品金额：
							<text class="red">¥{{ item.orderProduct.totalPrice }}</text>
						</view>
					</view>
					<view class="d-e-c pt10">
						<view>
							订单实付金额：
							<text class="red">¥{{ item.orderProduct.totalPayPrice || 0 }}</text>
						</view>
					</view>

					<view class="d-e-c mt20 pt20 border-t">
						<button
							v-if="item.isAgree == 0 && item.plateStatus == 0 && item.type == 30 && (item.status == 0 || item.status == 10)"
							style="margin-right: 15rpx;" type="default" class="btn-gray-border"
							@click="intervention(item.orderRefundId)">
							申请平台介入
						</button>
						<text
							v-if="item.isAgree == 0 && item.plateStatus == 10 && item.type == 30 && (item.status == 0 || item.status == 10)"
							style="margin-right: 15rpx;" type="default" class="text_red">
							平台介入处理中
						</text>
						<button type="default" class="btn-gray-border"
							@click="gotoRefundDetail(item.orderRefundId)">查看详情</button>
					</view>
				</view>
				<view class="d-c-c p30" v-if="tableData.length == 0 && !loading">
					<text class="iconfont icon-wushuju"></text>
					<text class="cont">亲，暂无相关记录哦</text>
				</view>
				<uni-load-more v-else :loadingType="loadingType"></uni-load-more>
			</view>
			<view v-else class="order-list">
				<view class="item bg-white p30 mb20" v-for="(item, index) in tableData" :key="index">
					<view class="d-b-c">
						<text>{{ item.createTime }}</text>
						<text class="red">{{ item.stateText }}</text>
					</view>
					<!--多个商品显示-->
					<view class="product-list pr" v-if="item.product.length > 1" @click="gotoOrder(item.orderId)">
						<scroll-view scroll-x="true">
							<view class="list d-s-c pr100">
								<view class="cover mr10" v-for="(img, num) in item.product" :key="num"><image :src="img.productImage" mode="aspectFit"></image></view>
							</view>
						</scroll-view>
						<view class="total-count">
							<view class="left-shadow"></view>
							<view class="price f22 theme-price">
								¥
								<text class="f40 theme-price">{{ item.payPrice }}</text>
							</view>
							<view class="count">共{{ item.totalNum }}件</view>
						</view>
					</view>
					<!--一个商品显示-->
					<view class="one-product d-s-c" v-else @click="gotoOrder(item.orderId)">
						<view class="cover" v-for="(img, num) in item.product" :key="num"><image :src="img.productImage" mode="aspectFit"></image></view>
						<view class="pro-info flex-1">{{ item.product[0].productName }}</view>
						<view class="total-count">
							<view class="left-shadow"></view>
							<view class="price theme-price f22">
								¥
								<text class="f40 theme-price" v-if="item.orderSource == 80">{{ item.payPrice }}</text>
								<text class="f40 theme-price" v-else>{{ item.payPrice }}</text>
							</view>
							<view class="count">共{{ item.totalNum }}件</view>
						</view>
					</view>
					<view class="d-e-c pt10">
						<view>
							订单实付金额：
							<text class="red">¥{{ item.payPrice }}</text>
						</view>
					</view>
					<view class="order-bts">
							<text v-if="item.orderStatus == 21" class="count">退款处理中</text>
							<text v-if="item.orderStatus == 20" class="count red">已退款</text>
					</view>
				</view>
				<view class="d-c-c p30" v-if="tableData.length == 0 && !loading">
					<text class="iconfont icon-wushuju"></text>
					<text class="cont">亲，暂无相关记录哦</text>
				</view>
				<uni-load-more v-else :loadingType="loadingType"></uni-load-more>
			</view>
		</scroll-view>
	</view>
</template>

<script>
	import uniLoadMore from '@/components/uni-load-more.vue';
	export default {
		components: {
			uniLoadMore
		},
		data() {
			return {
				/*手机高度*/
				phoneHeight: 0,
				/*可滚动视图区域高度*/
				scrollviewHigh: 0,
				/*选中状态*/
				state_active: 1,
				/*页面数据*/
				tableData: [],
				list_rows: 5,
				last_page: 0,
				page: 1,
				no_more: false,
				loading: true,
				/*顶部刷新*/
				topRefresh: false
			};
		},
		computed: {
			/*加载中状态*/
			loadingType() {
				if (this.loading) {
					return 1;
				} else {
					if (this.tableData.length != 0 && this.no_more) {
						return 2;
					} else {
						return 0;
					}
				}
			}
		},
		mounted() {
			this.init();
			/*获取页面数据*/
			this.getData();
		},
		onPullDownRefresh() {
			/*下拉到顶，页面值还原初始化*/
			//this.restoreData();
			//this.getData();
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
			/*页面数据*/
			getData() {
				let self = this;
				self.loading = true;
				let page = self.page;
				let state = self.state_active;
				let list_rows = self.list_rows;
				self._postBody(
					'user/refund/lists', {
						state: -1,
						pageIndex: page || 1,
						pageSize: list_rows,
					},
					function(res) {
						self.loading = false;
						self.tableData = res.data.records;
						self.last_page = res.data.lastPage;
						if (self.last_page <= 1) {
							self.no_more = true;
							return false;
						}
					}
				);
			},
			getData1() {
				let self = this;
				self.loading = true;
				let page = self.page;
				let list_rows = self.list_rows;
				let type = self.state_active;
				self._postBody(
					'user/order/lists',
					{
						pageIndex: page || 1,
						pageSize: list_rows,
						type: 'cancel',
					},
					function(res) {
						self.loading = false;
						self.tableData = self.tableData.concat(res.data.records);
						self.last_page = res.data.lastPage;
						if (self.last_page <= 1) {
							self.no_more = true;
							return false;
						}
					}
				);
			},

			/*类别切换*/
			stateFunc(e) {
				let self = this;
				if (self.state_active != e) {
					self.tableData = [];
					self.loading = true;
					self.page = 1;
					self.state_active = e;
					if (self.state_active == 1) {
						self.getData();
					} else {
						self.getData1();
					}

				}
			},
			/* 申请平台介入 */
			intervention(e) {
				let self = this;
				uni.showLoading({
					title: '加载中'
				});
				self._post(
					'user/refund/plateApply', {
						orderRefundId: e
					},
					function(data) {
						uni.hideLoading();
						uni.showToast({
							icon: 'none',
							title: '申请平台介入成功'
						});
						self.getData();
						self.loading = false;
					}
				);
			},
			/*查看售后详情*/
			gotoRefundDetail(e) {
				this.gotoPage('/pages/order/refund/detail/detail?order_refund_id=' + e);
			},
			/*可滚动视图区域到顶触发*/
			scrolltoupperFunc() {
				/* let self = this;
					self.topRefresh = true;
					self.no_more = false;
					setTimeout(function() {
						self.topRefresh = false;
					}, 2000);
					self.tableData = [];
					self.page = 1;
					self.getData(); */
			},

			/*可滚动视图区域到底触发*/
			scrolltolowerFunc() {
				let self = this;
				if (self.no_more) {
					return;
				}
				self.page++;
				if (self.page <= self.last_page) {
					if (self.state_active == 1) {
						self.getData();
					} else {
						self.getData1();
					}
				} else {
					self.no_more = true;
				}
			}
		}
	};
</script>

<style lang="scss">
	@import url("../../../../pages/order/css/myorder.css");
</style>