<template>
	<view class="prodcut-list-wrap">
		<scroll-view scroll-y="true" :style="'height:' + scrollviewHigh + 'px;'" class="scroll-Y" lower-threshold="50"
		 @scrolltolower="scrolltolowerFunc">
			<view class="shop">
				<view class="shop_head">
					<view class="inner-tab">
						<view :class="type_active=='sell'?'item active':'item'" @click="tabTypeFunc('sell')">
							<view class="box">在售</view>
						</view>
						<view :class="type_active=='audit'?'item active':'item'" @click="tabTypeFunc('audit')">
							<view class="box">审核中</view>
						</view>
						<view :class="type_active=='lower'?'item active':'item'" @click="tabTypeFunc('lower')">
							<view class="box">已下架</view>
						</view>
					</view>
				</view>
				<view class="shop_body" v-if="!loading">
					<view class="shop_body_l_item" v-for="(item,index) in product_list" :key="index" >
						<view class="d-s-c mb20">
							<view class="shop_body_l_item_img">
								<image class="item_img" :src="item.productImage" mode="aspectFill"></image>
							</view>
							<view class=" flex-1">
								<view class="shop_body_l_item_info_title h1">{{item.productName}}</view>
								<view class="shop_body_l_item_info_price h2 red">¥<text class="h1">{{item.productPrice}}</text></view>
								<view class="d-b-c">
									<view>库存:{{item.productStock}}</view>
									<button v-if="type_active=='sell'" type="warn" class='shop_body_b_btn' @click="operation(item.productId,20)">下架</button>
									<text v-if="type_active=='audit'" class="audit_txt">审核中</text>
									<button v-if="type_active=='lower'" type="warn" class='shop_body_b_btn' @click="operation(item.productId,10)">上架</button>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="d-c-c p30" v-if="product_list.length==0 && !loading">
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
				shop_info: '', //店铺信息
				product_list: '', //商品列表
				adList: '', //banner列表
				dataList: '',
				shop_supplier_id: '', //店铺ID
				// ***********
				/*是否显示点*/
				indicatorDots: false,
				/*是否自动*/
				autoplay: true,
				/*切换时间*/
				interval: 5000,
				/*动画过渡时间*/
				duration: 1000,
				/*数据列表*/
				listData: [], //优惠券列表
				// ************
				/*顶部刷新*/
				topRefresh: false,
				/*底部加载*/
				loading: true,
				/*没有更多*/
				no_more: false,
				/*类别选中*/
				type_active: 'sell',
				/*价格选中*/
				price_top: false,
				/*店铺列表*/
				shopData: [],
				/*当前页面*/
				page: 1,
				search: '',
				last_page: 0,
				/*可滚动视图区域高度*/
				scrollviewHigh: 0,

			}
		},
		computed: {
			/*加载中状态*/
			loadingType() {
				if (this.loading) {
					return 1;
				} else {
					if (this.product_list.length != 0 && this.no_more) {
						return 2;
					} else {
						return 0;
					}
				}
			}
		},
		onLoad(option) {
			let self = this;
			console.log(option);
			self.shop_supplier_id = option.shopSupplierId;

		},
		onShow() {
			this.init();
			this.restoreData();
			this.getData(this.type_active);
		},
		onPullDownRefresh() {
			/*下拉到顶，页面值还原初始化*/
			this.restoreData();
			this.getData(this.type_active);
		},
		methods: {
			/*初始化*/
			init() {
				let self = this;
				uni.getSystemInfo({
					success(res) {
						self.scrollviewHigh = res.windowHeight;
					}
				});
			},
			/*可滚动视图区域到底触发*/
			scrolltolowerFunc() {
				let self = this;
				self.bottomRefresh = true;
				self.page++;
				self.loading = true;
				if (self.page > self.last_page) {
					self.loading = false;
					self.no_more = true;
					return;
				}
				self.getData(self.type_active);
			},
			/*还原初始化*/
			restoreData() {
				this.shopData = [];
				this.product_list=[];
				this.category_id = 0;
				this.search = '';
				this.sortType = '';
				this.sortPrice = 0;
			},
			/*类别切换*/
			tabTypeFunc(e) {
				if(this.loading){
					return;
				}
				let self = this;
				if (e != self.type_active) {
					self.product_list = [];
					self.page = 1;
					self.no_more = false;
					self.loading = true;
					self.type_active = e;
					self.getData(e);
				}
			},

			//获取数据列表
			getData(e) {
				let self = this;
				self.loading = true;
				self._postBody('supplier/product/index', {
					pageIndex:self.page,
					shopSupplierId: self.shop_supplier_id,
					type: e,
				}, (res) => {
					self.last_page = res.data.lastPage
					self.product_list = [...self.product_list, ...res.data.records]
					self.loading = false;
					uni.hideLoading()
				})
			},
			operation(id,n){
				let self=this;
				self.loading = true;
				uni.showLoading({
					title:'加载中'
				})
				self._postBody('supplier/product/modify', {
					productStatus:n,
					productId: id,
					shopSupplierId: self.shop_supplier_id
				}, (res) => {
					self.restoreData();
					self.getData(self.type_active);
					
				})
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: f2f2f2;
	}

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

	.red {
		color: #E2231A;
	}

	.shop_head {}

	.shop_head_info {
		width: 100%;
		margin: 0 auto;
		padding: 5%;
		box-sizing: border-box;
		background-color: white;
	}

	.shop_list_body_item_shop {
		width: 100%;
		height: 150rpx;
		display: flex;
		justify-content: space-between;
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

	.brand {
		position: relative;
		top: -20rpx;
		color: #585858;
	}

	.sales {
		color: #585858;
	}

	.collect {
		color: #585858;
	}

	.shop_list_body_item_shop_others button {
		width: 140rpx;
		height: 60rpx;
		line-height: 60rpx;
		padding: 0;
		background-image: linear-gradient(90deg, #F11E0B, #F77737);
		color: white;
	}

	.shop_head_banner {
		width: 100%;
		height: 360rpx;
	}

	.shop_head_banner swiper {
		width: 100%;
		height: 100%;
	}

	.swiper-item {
		width: 100%;
		height: 360rpx;
	}

	.swiper-item image {
		width: 100%;
		height: 100%;
	}

	.shop_body {
		width: 100%;
		background-color: #f2f2f2;
	}

	.shop_body_l_item {
		width: 90%;
		// height: 250rpx;
		margin: 0 auto;
		background-color: white;
		border-radius: 15rpx;
		margin-top: 20rpx;
		// display: flex;
		padding: 20rpx;
		box-sizing: border-box;
	}

	.shop_body_l_item_img image {
		width: 235rpx;
		height: 235rpx;
		background-color: rgba(0, 0, 0, 0.1);
		margin-right: 15rpx;
	}

	.shop_body_l_item_info {
		width: 70%;
		height: 100%;
		display: flex;
		justify-content: space-between;
		flex-direction: column;
		padding-left: 20rpx;
		box-sizing: border-box;
	}

	.shop_body_l_item_info_title {
		font-size: 28rpx;
		height: 100rpx;
		line-height: 50rpx;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
		text-overflow: ellipsis;
		-webkit-box-orient: vertical;
		word-wrap: break-word;
		word-break: break-all;
		overflow: hidden;
	}

	.shop_body_l_item_info_price {
		display: flex;
		align-items: flex-end;
	}

	.shop_body_l_item_info_price view {
		margin-right: 15rpx;
	}

	.shop_body_l_item_info_others {
		width: 100%;
		height: 30rpx;
		display: flex;
		justify-content: space-between;
	}

	.shop_body_l_item_info_others_activity {
		width: 150rpx;
		height: 30rpx;
		line-height: 30rpx;
		border: 1rpx #E22319 solid;
		border-radius: 30rpx;
		/* font-size: 16rpx; */
		color: #E22319;
		text-align: center;
		box-sizing: border-box;
	}

	.shop_body_l_item_info_others_sales {
		color: #585858;
	}

	.shop_body2 {
		width: 100%;
		display: flex;
		justify-content: flex-start;
		flex-wrap: wrap;
		background-color: #f2f2f2;
	}

	.shop_body_t_item {
		width: 45%;
		margin: 0 2.5%;
		margin-top: 20rpx;
		height: 520rpx;
		overflow: hidden;
		background-color: white;
	}

	.collect text {
		color: #E2231A;
	}

	.shop_body_t_item image {
		width: 100%;
		height: 337.5rpx;
		background-color: rgba(0, 0, 0, 0.1);
	}

	.shop_body_t_item_info {
		height: 182.5rpx;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		padding: 10rpx;
		box-sizing: border-box;
	}

	.shop_body_t_item_info_title {
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
		text-overflow: ellipsis;
		-webkit-box-orient: vertical;
		word-wrap: break-word;
		word-break: break-all;
		overflow: hidden;
	}

	.shop_body_t_item_info_price {
		display: flex;
		align-items: flex-end;
	}

	.shop_body_t_item_info_others {
		display: flex;
		justify-content: space-between;
	}

	.shop_body_t_item_info_others_activity {}

	.shop_body_t_item_info_others_sales {
		color: #585858;
	}

	/* ***************************************** */
	/* ***************************************** */
	/* ***************************************** */
	.diy-coupon {
		margin: 20rpx;
	}

	.diy-coupon .swiper {
		width: 750rpx;
		height: 168rpx;
	}

	.diy-coupon .coupon-item {
		width: 710rpx;
		height: 168rpx;
		align-items: stretch;
		align-content: stretch;
		color: #ffffff;
	}

	.diy-coupon .coupon-item.bg-red {
		background: #e62423;
	}

	.diy-coupon .coupon-item.bg-blue {
		background: #178ed9;
	}

	.diy-coupon .coupon-item.bg-yellow {
		background: #f4a50b;
	}

	.diy-coupon .coupon-item.bg-violet {
		background: #ab0bf6;
	}

	.diy-coupon .coupon-item .left-type {
		padding: 0 30rpx 0 40rpx;
		width: 40rpx;
		font-size: 40rpx;
		line-height: 40rpx;
		text-align: center;
		font-weight: bold;
		border-right: 4rpx dashed rgba(255, 255, 255, .4);
	}

	.diy-coupon .left-side-line {
		position: absolute;
		width: 20rpx;
		top: 0;
		left: -15rpx;
		overflow: hidden;
	}

	.diy-coupon .right-side-line {
		position: absolute;
		width: 20rpx;
		top: 0;
		right: -15rpx;
		overflow: hidden;
	}

	.diy-coupon .side-line .round {
		display: block;
		width: 20rpx;
		height: 20rpx;
		border-radius: 50%;
		margin: 4rpx 0;
		background: #ffffff;
	}

	.diy-coupon .center-content::before,
	.diy-coupon .center-content::after {
		position: absolute;
		display: block;
		content: '';
		width: 30rpx;
		height: 15rpx;
		background: #FFFFFF;
	}

	.diy-coupon .center-content::before {
		top: 0;
		right: -16rpx;
		border-radius: 0 0 15rpx 15rpx;
	}

	.diy-coupon .center-content::after {
		bottom: 0;
		right: -16rpx;
		border-radius: 15rpx 15rpx 0 0;
	}

	.diy-coupon .coupon-item .center-content {
		padding: 20rpx 40rpx;
		display: flex;
		justify-content: space-between;
		flex-direction: column;
		align-items: flex-start;
		flex: 1;
	}

	.diy-coupon .coupon-item .center-content .content-top {
		height: 50rpx;
		line-height: 50rpx;
	}

	.diy-coupon .coupon-item .center-content .content-datatime {
		padding: 4rpx 10rpx;
		border-radius: 30rpx;
		font-size: 20rpx;
		background: rgba(0, 0, 0, .2);
	}

	.diy-coupon .coupon-item .right-receive {
		padding: 0 40rpx 0 30rpx;
		width: 30rpx;
		text-align: center;
		font-size: 30rpx;
		line-height: 30rpx;
		text-align: center;
		border-left: 4rpx dashed rgba(255, 255, 255, .4);
		background: rgba(0, 0, 0, .6);
	}

	.diy-coupon .coupon-item .no-receive {
		background: #acacac;
		color: #787878;
	}

	/* ***************************** */
	.inner-tab {
		position: relative;
		height: 80rpx;
		display: flex;
		justify-content: space-around;
		align-items: center;
		border-bottom: 1px solid #dddddd;
		background: #ffffff;
		box-shadow: 0 8rpx 12rpx 0 rgba(0, 0, 0, .1);
		z-index: 9;
	}

	.inner-tab .item {
		flex: 1;
		font-size: 30rpx;
	}

	.inner-tab .item.active,
	.inner-tab .item .arrow.active .iconfont {
		color: $dominant-color;
	}

	.inner-tab .item .box {
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: row;
	}

	.inner-tab .item .arrows {
		margin-left: 10rpx;
		line-height: 0;
	}

	.inner-tab .item .iconfont {
		line-height: 24rpx;
		font-size: 24rpx;
	}

	.inner-tab .item .arrow,
	.inner-tab .item .svg-icon {
		width: 20rpx;
		height: 20rpx;
	}

	.box image {
		width: 36rpx;
		height: 36rpx;
	}
	.shop_body_l_item_info_price{
		line-height: 100rpx;
		height: 100rpx;
	}
	.shop_body_b_btn{
		width: 140rpx;
		height: 56rpx;
		// border: 1rpx solid #CACACA;
		border-radius: 10rpx;
		font-size: 23rpx;
		text-align: center;
		line-height: 56rpx;
		margin-right: 26rpx;
	}
	.audit_txt{
		color: #FF6633;
		font-size: 23rpx;
		margin-right: 26rpx;
	}
</style>
