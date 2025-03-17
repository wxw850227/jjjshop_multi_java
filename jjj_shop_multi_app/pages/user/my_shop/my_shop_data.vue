<template>
	<view>
		<view class="shop_data shop_data_top">
				<view class="shop_data_title">店铺记录</view>
				<view class="deal" v-if="!loadding">
					<view class="title">今日成交量</view>
					<view class="d-s-c  deal_list">
						<view class="deal_item d-c  d-c-c">
							<view class="item_num">{{order.orderTotalPriceT}}</view>
							<view>成交额</view>
						</view>
						<view class="deal_item d-c  d-c-c">
							<view class="item_num">{{order.orderPerPriceT}}</view>
							<view>客单价</view>
						</view>
						<view class="deal_item d-c d-c-c">
							<view class="item_num">{{order.orderTotalT}}</view>
							<view>付款订单</view>
						</view>
						<view class="deal_item d-c  d-c-c">
							<view class="item_num">{{order.orderUserTotalT}}</view>
							<view>付款人数</view>
						</view>
						<view class="deal_item d-c  d-c-c">
							<view class="item_num">{{order.orderRefundMoneyT}}</view>
							<view>成功退款金额</view>
						</view>
						<view class="deal_item d-c  d-c-c">
							<view class="item_num">{{order.orderRefundTotalT}}</view>
							<view>退款订单</view>
						</view>
					</view>
				</view>
				<view class="visit" v-if="!loadding">
					<view class="title">今日访问量</view>
					<view class="d-a-c visit_list">
						<view class="visit_item d-c d-c-c">
							<view class="item_num">{{visit.favStoreT}}</view>
							<view>店铺收藏</view>
						</view>
						<view class="visit_item d-c d-c-c">
							<view class="item_num">{{visit.favProductT}}</view>
							<view>商品收藏</view>
						</view>
						<view class="visit_item d-c d-c-c">
							<view class="item_num">{{visit.visitUserT}}</view>
							<view>访客数</view>
						</view>
						<view class="visit_item d-c d-c-c">
							<view class="item_num">{{visit.visitTotalT}}</view>
							<view>访问量</view>
						</view>
					</view>
				</view>
			</view>
			<view class="shop_data">
				<view class="shop_data_title">订单结算</view>
				<view>
					<view v-for="(item,index) in orderList" :key="index" class="data_list">
						<view class="d-b-c mb20">
							<view class="datalist_item" style="width: 400rpx;">订单号：{{item.orderNo}}</view>
							<view class="datalist_item orange"  style="width:50rpx;" @click="todetail(item.settledId)">详情</view>
						</view>
						<view class="d-b-c">
							<view class="datalist_item" style="width: 250rpx;">支付金额：{{item.payMoney}}</view>
							<view class="datalist_item" style="width: 250rpx;">结算金额：{{item.supplierMoney}}</view>
						</view>
					</view>
					<!-- 没有记录 -->
					<view class="d-c-c p30" v-if="orderList.length==0 && !loadding">
						<text class="iconfont icon-wushuju"></text>
						<text class="cont">亲，暂无相关记录哦</text>
					</view>
					<uni-load-more v-else :loadingType="loadingType"></uni-load-more>
				</view>	
			</view>
	</view>
	</template>

<script>
	import uniLoadMore from "@/components/uni-load-more.vue";
	export default {
		data() {
			return {
				order:{},
				visit:{},
				orderList:[],
				/*底部加载*/
				loadding: true,
				/*没有更多*/
				no_more: false,
				/*当前页面*/
				page: 1,
				last_page:0
			}
		},
		components: {
			uniLoadMore
		},
		computed: {
			/*加载中状态*/
			loadingType() {
				if (this.loadding) {
					return 1;
				} else {
					if (this.orderList.length != 0 && this.no_more) {
						return 2;
					} else {
						return 0;
					}
				}
			}
		},
		mounted() {
			this.getData();
		},
		methods: {
			/*可滚动视图区域到底触发*/
			onReachBottom() {
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
			//获取数据列表
			getData(e) {
				let self = this;
				let page = self.page;
				self.loadding = true;
				uni.showLoading({
					title:'加载中'
				})
				self._postBody('supplier/index/storedata', {
					pageIndex: page || 1
				}, (res) => {
					self.order=res.data.order;
					self.orderList=self.orderList.concat(res.data.ordersettle.records);
					self.visit=res.data.visit;
					self.last_page = res.data.ordersettle.lastPage;
					if (res.data.ordersettle.lastPage <= 1) {
						self.no_more = true;
					} else {
						self.no_more = false;
					}
					self.loadding=false;
					uni.hideLoading()
				})
			},
			todetail(id){
				this.gotoPage('/pages/user/my_shop/my_shop_detail?settled_id=' + id);
			}
		}
	}
</script>

<style scoped>
	.shop_data{
		width: 90%;
		padding: 30rpx;
		background-color: white;
		margin: 0 auto;
		margin-top: 30rpx;
		box-sizing: border-box;
		border-radius: 30rpx;
		color: #585858;
	}
	.shop_data_title{
		font-size: 30rpx;
		margin-bottom: 50rpx;
		font-weight: 800;
	}
	.deal .title,.visit .title{
		
		font-size: 30rpx;
		padding-bottom: 15rpx;
	}
	.deal_list {
		flex-wrap: wrap;
	}

	.deal_item {
		width: 33%;
		margin:20rpx 0;
	}
	.item_num{
		margin-bottom: 20rpx;
		color: #ff8a00;
	}
	.datalist_item{
		/* text-align: center; */
	}
	.data_list{
		border-top: 1rpx solid #CCCCCC;
		padding: 20rpx 20rpx;
	}
</style>
