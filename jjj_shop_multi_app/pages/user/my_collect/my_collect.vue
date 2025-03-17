<template>
	<view>
		<scroll-view scroll-y="true" class="scroll-Y" :style="'height:' + scrollviewHigh + 'px;'" lower-threshold="50"
		 @scrolltolower="scrolltolowerFunc">
			<view class="shop_list_body">
				<view class="shop_list_body_item" v-for="(item,index) in shop_list" :key="index">
					<view class="shop_list_body_item_shop" @click="goto_shop(item.shopSupplierId)">
						<view class="shop_list_body_item_shop_logo">
							<image :src="item.logoFilePath"></image>
						</view>
						<view class="shop_list_body_item_shop_info">
							<view class="h1 title">{{item.name}}</view>
							<view class="h3 brand">主营品牌：{{item.categoryName}}</view>
							<view class="h3 sales">销量{{item.productSales}}件</view>
						</view>
						<view class="shop_list_body_item_shop_others">
							<view class="h3 attention"><text class="red">{{item.favCount}}</text>人关注</view>
							<view class="h3 collect">店铺评分：<text class="red">{{item.serverScore}}</text></view>
						</view>
					</view>

					<view v-if="shop_list[index].productList.length>0" :class="shop_list[index].productList.length<3?'shop_list_body_item_product2':'shop_list_body_item_product'">
						<view class="shop_list_body_item_product_item" v-for="(item,index2) in shop_list[index].productList" :key="index2"
						 @click="goto_product(item.productId)">
							<image :src="item.productImage"></image>
							<view class="shop_list_body_item_product_item_price">
								<view class="h4 red">¥<text class="h3">{{item.productPrice>1000?item.productPrice*1:item.productPrice}}</text></view>
								<view class="h6 huaxianjia">¥<text class="h5">{{item.linePrice>1000?item.linePrice*1:item.linePrice}}</text></view>
							</view>
						</view>
					</view>
				</view>
			</view>
			<!-- 没有记录 -->
			<view class="d-c-c p30" v-if="shop_list.length==0 && !loading">
				<text class="iconfont icon-wushuju"></text>
				<text class="cont">亲，暂无相关记录哦</text>
			</view>
			<uni-load-more v-else :loadingType="loadingType"></uni-load-more>
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
				shop_list: [], //店铺列表数据
				/*底部加载*/
				loading: true,
				/*没有更多*/
				no_more: false,
				//页面高度
				scrollviewHigh: '',
				//当前页
				page: 1,
				//总页数
				last_page: '',
				isfollow:'',
			}
		},
		computed: {
			/*加载中状态*/
			loadingType() {
				if (this.loading) {
					return 1;
				} else {
					if (this.shop_list.length != 0 && this.no_more) {
						return 2;
					} else {
						return 0;
					}
				}
			}
		},
		onShow() {
			this.getData();
			this.init();
		},
		methods: {
			/*初始化*/
			init() {
				let self = this;
				self.shop_list=[];
				self.page=1;
				uni.getSystemInfo({
					success(res) {
						self.scrollviewHigh = res.windowHeight;
					}
				});
			},
			//获取数据
			getData() {
				let self = this;
				self._postBody('user/favorite/list', {
					pageIndex: self.page,
					type: 10,
					pageSize: 15,
				}, (res) => {
					self.loading = false;
					self.shop_list = self.shop_list.concat(res.data.records);
					self.last_page = res.data.lastPage;
					if (res.data.lastPage <= 1) {
						self.no_more = true;
					} else {
						self.no_more = false;
					}
				})
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
			//跳转店铺首页
			goto_shop(shop_supplier_id) {
				this.gotoPage('/pages/shop/shop?shopSupplierId=' + shop_supplier_id);
			},
			//跳转商品页面
			goto_product(product_id) {
				this.gotoPage('/pages/product/detail/detail?productId=' + product_id);
			},
		}
	}
</script>

<style>
	.h1 {
		font-size: 32rpx;
	}

	.h2 {
		font-size: 28rpx;
	}

	.h3 {
		font-size: 24rpx;
	}

	.h4 {
		font-size: 20rpx;
	}

	.h5 {
		font-size: 16rpx;
	}

	.h6 {
		font-size: 12rpx;
	}

	.huaxianjia {
		text-decoration: line-through;
		color: #585858;
	}

	.shop_list_body {
		width: 100%;
		padding: 20rpx;
		box-sizing: border-box;
	}

	.shop_list_body_item {
		display: flex;
		width: 100%;
		max-height: 470rpx;
		margin-bottom: 30rpx;
		flex-direction: column;
		background-color: white;
		border-radius: 20rpx;
		padding: 10rpx;
		border-bottom: 2rpx #f2f2f2 solid;
		box-sizing: border-box;
	}

	.shop_list_body_item_shop {
		width: 100%;
		height: 150rpx;
		display: flex;
		justify-content: space-between;
		margin-bottom: 10rpx;
	}

	.shop_list_body_item_shop_logo {
		width: 150rpx;
		height: 150rpx;
	}

	.shop_list_body_item_shop_logo image {
		width: 100%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0.1);
		border-radius: 15rpx;
	}

	.shop_list_body_item_shop_info {
		padding: 10rpx;
		box-sizing: border-box;
		margin-left: -10%;
		padding-top: 0;
		display: flex;
		justify-content: space-between;
		flex-direction: column;
	}

	.shop_list_body_item_shop_others {
		padding: 10rpx;
		box-sizing: border-box;
		display: flex;
		justify-content: space-between;
		flex-direction: column;
		text-align: right;
		padding-top: 0;
	}

	.shop_list_body_item_product {
		width: 100%;
		height: 280rpx;
		display: flex;
		justify-content: space-around;
		align-items: center;
		background-color: #f2f2f2;
		padding: 10rpx;
		box-sizing: border-box;
		border-radius: 15rpx;
	}

	.shop_list_body_item_product2 {
		width: 100%;
		height: 280rpx;
		display: flex;
		justify-content: flex-start;
		align-items: center;
		background-color: #f2f2f2;
		padding: 10rpx;
		box-sizing: border-box;
		border-radius: 15rpx;
	}

	.shop_list_body_item_product2 .shop_list_body_item_product_item {
		margin: 0 10rpx;
	}

	.shop_list_body_item_product_item {
		width: 30%;
		height: 240rpx;
		background-color: white;
	}

	.shop_list_body_item_product_item image {
		width: 100%;
		height: 200rpx;
		background-color: rgba(0, 0, 0, 0.1);
	}

	.shop_list_body_item_product_item view {
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		text-overflow: ellipsis;
		-webkit-box-orient: vertical;
		word-wrap: break-word;
		word-break: break-all;
		overflow: hidden;
	}

	.shop_list_body_item_product_item_price {
		display: flex;
		justify-content: flex-start;
		align-items: flex-end;
	}

	.brand {
		position: relative;
		top: -20rpx;
		color: #585858;
	}

	.sales {
		color: #585858;
	}
</style>
