<template>
	<view class="refund-list">
		<view class="top-tabbar">
			<view :class="state_active==-1?'tab-item active':'tab-item'" @click="stateFunc(-1)">
				全部
			</view>
			<view :class="state_active==0?'tab-item active':'tab-item'" @click="stateFunc(0)">
				待处理
			</view>
		</view>
		<scroll-view scroll-y="true" class="scroll-Y" :style="'height:' + scrollviewHigh + 'px;'" lower-threshold="50"
			@scrolltoupper="scrolltoupperFunc" @scrolltolower="scrolltolowerFunc">
			<view :class="topRefresh?'top-refresh open':'top-refresh'">
				<view class="circle" v-for="(circle,n) in 3" :key="n"></view>
			</view>
			<view class="list">
				<view class="item bg-white p30 mb20" v-for="(item,index) in tableData" :key="index">
					<view class="d-b-c">
						<text>{{item.createTime}}</text>
						<text class="red">{{item.stateText}}</text>
					</view>
					<view class="one-product d-s-c pt20">
						<view class="cover">
							<image :src="item.orderProduct.productImage" mode="aspectFit"></image>
						</view>
						<view class="flex-1">
							<view class="pro-info">{{item.orderProduct.productName}}</view>
						</view>
					</view>
					<view class="d-e-c pt20">
						<view>
							商品金额：
							<text class="red">¥{{item.orderProduct.linePrice}}</text>
						</view>
					</view>
					<view class="d-e-c pt10">
						<view>
							订单实付金额：
							<text class="red">¥{{item.orderProduct.totalPrice}}</text>
						</view>
					</view>
					<view class="d-e-c mt20 pt20 border-t">
						<button type="default" class="btn-gray-border"
							@click="gotoRefundDetail(item.orderRefundId)">查看详情</button>
					</view>
				</view>
				<!-- 没有记录 -->
				<view class="d-c-c p30" v-if="tableData.length==0 && !loading">
					<text class="iconfont icon-wushuju"></text>
					<text class="cont">亲，暂无相关记录哦</text>
				</view>
				<uni-load-more v-else :loadingType="loadingType"></uni-load-more>
			</view>
		</scroll-view>
	</view>
</template>

<script>
	import uniLoadMore from "@/components/uni-load-more.vue";
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
				state_active: -1,
				/*页面数据*/
				tableData: [],
				list_rows: 5,
				last_page: 0,
				page: 1,
				no_more: false,
				loading: true,
				/*顶部刷新*/
				topRefresh: false,
				shopSupplierId: '',
			}
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
		onLoad(e) {
			this.shopSupplierId = e.shopSupplierId;
		},
		mounted() {
			this.init();
			/*获取页面数据*/
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
			/*页面数据*/
			getData() {
				let self = this;
				self.loading = true;
				let page = self.page;
				let state = self.state_active;
				let list_rows = self.list_rows;
				self._postBody('supplier/order/refund', {
					shopSupplierId: self.shopSupplierId,
					state: state,
					pageIndex: page || 1,
					pageSize: list_rows,
					type:0
				}, function(res) {
					self.loading = false;
					self.tableData = self.tableData.concat(res.data.records);
					// console.log(self.tableData);
					self.last_page = res.data.lastPage;
					if (self.last_page <= 1) {
						self.no_more = true;
						return false;
					}
				});

			},
			/*类别切换*/
			stateFunc(e) {
				let self = this;
				if (self.state_active != e) {
					self.tableData = [];
					self.loading = true;
					self.page = 1;
					self.state_active = e;
					self.getData();
				}
			},
			/*查看售后详情*/
			gotoRefundDetail(e) {
				this.gotoPage('/pages/order/refund/detail/detail?source=supplier&order_refund_id=' + e);
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
			}

		}
	}
</script>

<style>

</style>
