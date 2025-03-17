<template>
	<view class="wrap pt20" :data-theme='theme()' :class="theme() || ''" v-if="!loading">
		<Myinfo :Address="Address" :existAddress="existAddress"></Myinfo>
		<!--购买的产品-->
		<view class="vender">
			<view class="list">
				<view v-for="(supplier_item, supplier_index) in ProductData" :key="supplier_index">
					<view class="sup_itemtit" v-if="storeOpen">
						<i class="icon iconfont icon-dianpu1"></i>
						<view class="f26 gray3">
							{{supplier_item.supplier.name}}
						</view>
					</view>
					<view class="item" v-for="(item, index) in supplier_item.productList" :key="index">
						<view class="d-f">
							<view class="cover">
								<image :src="item.productImage" mode="aspectFit"></image>
							</view>
							<view class="info">
								<view class="d-b-s">
									<view class="flex-1">
										<view class="title f32 gray3">{{ item.productName }}</view>
										<view class="theme-price mt10 f18">
											¥<text class="f26">{{ item.productPrice }}</text>
										</view>
										<view class="describe mt10 f24">{{ item.productSku.productAttr }}
										</view>
									</view>
									<view>
										<view class=" count_choose">
											<view class="num-wrap">
												<view class="f22 tr">×{{ item.totalNum }}</view>
											</view>
										</view>
									</view>
								</view>
							</view>
						</view>
						<view class="mt10 tr f28" v-if="item.isUserGrade">
							<text class="f26">会员折扣价：</text>
							<text class="theme-price f32">￥{{item.gradeProductPrice}}</text>
						</view>
						<view class="mt10 tr f28" v-if="item.productReduceMoney > 0">
							<text class="f26">立减：</text>
							<text class="theme-price f32">￥{{item.productReduceMoney}}</text>
						</view>
					</view>

					<view class="d-f-c" @tap="DistUp(supplier_item)">
						<view v-if="supplier_item.orderData.delivery==10">
							配送方式 <text class="extp">普通快递</text>
						</view>
						<view v-if="supplier_item.orderData.delivery==20">
							<view class="info d-s-s" style="padding-left: 0;">
								<text class="zt theme-btn">当前自提点</text>
								<view class="province-c-a d-s-s flex-1">
									<text>{{ storeList[supplier_item.shopSupplierId]}}</text>
								</view>
							</view>
						</view>
						<view v-if="supplier_item.orderData.delivery==30">
							虚拟商品：无需物流
						</view>
						<view class="dfjac" v-if="supplier_item.orderData.delivery!=30">
							<view v-if="supplier_item.orderData.delivery==10">
								{{supplier_item.orderData.expressPrice != 0?"￥ "+supplier_item.orderData.expressPrice:"快递 免费"}}
							</view>
							<i class='iconfont icon-jiantou'></i>
						</view>
					</view>
					<template v-if="true">
<!--						<view class="d-f-c" v-if="!OrderData.forcePoints">-->
<!--							<view class="">优惠券</view>-->
<!--							<block v-if="objKeys(supplier_item.orderData.couponList,1) > 0">-->
<!--								<text class="vlaue theme-price" v-if="supplier_item.orderData.couponMoney> 0"-->
<!--									@tap="onTogglePopupCoupon(supplier_item.orderData.couponList,supplier_item)">-￥{{supplier_item.orderData.couponMoney}}</text>-->
<!--								<text v-else class="vlaue theme-price"-->
<!--									@tap="onTogglePopupCoupon(supplier_item.orderData.couponList,supplier_item)">有{{ objKeys(supplier_item.orderData.couponList,1)}}张优惠券可用<i-->
<!--										class='iconfont icon-jiantou'></i></text>-->
<!--							</block>-->
<!--							<text v-else class="f24 gray9">无优惠券可用</text>-->
<!--						</view>-->
						<view class="d-f-c" v-if="supplier_item.orderData.reduce">
							<view>店铺{{supplier_item.orderData.reduce.activeName}}</view>
							<view class="theme-price f24">
								-￥{{supplier_item.orderData.reduce.reducedPrice}}
							</view>
						</view>
						<view v-if="isUsePoints==1&&!OrderData.forcePoints&&supplier_item.orderData.pointsMoney>0">
							<view class="d-f-c">
								<view>积分抵扣金额</view>
								<view class="theme-price f24">-￥{{supplier_item.orderData.pointsMoney}}</view>
							</view>
						</view>
						<view class="d-f-c">
							<view class="ww100">
								<view class="mb20">订单备注</view>
								<view class="ww100"><textarea
										v-model="storeData[supplier_item.shopSupplierId].remark"
										placeholder="选填,请先和商家协商一致" class="text_area" /></view>
							</view>
						</view>
						<!--总数-->
						<view class="total-box vthl">
							<view>
								<text class="f26 gray3">共{{supplier_item.productList.length}}件商品,总价:</text>
								<text class="f20 gray3">￥</text>
								<text class="f26 gray3">{{ supplier_item.orderData.orderTotalPrice }}</text>
							</view>
							<view class="d-f">
								<view class="f26" v-if="!settledRule.forcePoints">
									实付款：<text class="theme-price f20">￥</text><text class="price theme-price"
										style="font-size: 32rpx;">{{toDecimal2(supplier_item.orderData.orderPayPrice)}}</text>
								</view>
							</view>
						</view>
					</template>

				</view>
			</view>
		</view>
		<!--其它费用-->
		<view class="buy-checkout">
			<view class="item">
				<text class="key f26">订单总金额：</text>
				<view>
					<text class="theme-price f24">￥{{ OrderData.orderTotalPrice }}</text>
				</view>
			</view>
			<view class="item f26" v-if="settledRule.isCoupon">
				<text class="key">平台优惠券：</text>
				<block v-if="couponNum > 0">
					<text class="f24  theme-price" v-if="OrderData.couponMoneySys > 0"
						@tap="onTogglePopupCoupon(couponList,0)">-￥{{OrderData.couponMoneySys}}</text>
					<text v-else class="f24  theme-price"
						@tap="onTogglePopupCoupon(couponList,0)">有{{ couponNum }}张优惠券可用</text>
				</block>
				<text v-else class="f24 gray9">无优惠券可用</text>
			</view>
			<view class="item" v-if="OrderData.productReduceMoney > 0">
				<text class="key">商品立减：</text>
				<view>
					<text class="theme-price f24">-￥{{ OrderData.productReduceMoney }}</text>
				</view>
			</view>
			<view class="item"
				v-if="OrderData.isAllowPoints && settledRule.forcePoints == false &&OrderData.pointsMoney != 0">
				<text class="key">可用积分抵扣：</text>
				<view class="">
					<text class="theme-price f24">-￥{{toDecimal2(OrderData.pointsMoney)}}</text>
					<switch style="transform: scale(0.7); margin-right: -10rpx;" checked=true @change="onShowPoints" />
				</view>
			</view>
		</view>
		<!--底部支付-->
		<view class="foot-pay-btns">
			<template v-if="true">
				<view class="price" v-if="!settledRule.forcePoints">
					¥
					<text class="num">{{ OrderData.orderPayPrice }}</text>
				</view>
				<view class="price" v-if="settledRule.forcePoints">
					<text class="gray9">所需积分</text>
					<text class="num theme-price fb">{{ ProductData[0].orderData.pointsNum }}</text>
					<template v-if="OrderData.orderPayPrice > 0">
						<text class="theme-price">+</text>
						<text class="theme-price">¥</text>
						<text class="num fb theme-price">{{ OrderData.orderPayPrice }}</text>
					</template>
				</view>
			</template>
			<button type="primary" @tap="SubmitOrder">提交订单</button>
		</view>

		<!--优惠券-->
		<Coupon :isCoupon="isCoupon" :couponList="couponList" @close="closeCouponFunc"></Coupon>
		<Dist :isDist="isDist" :chooseSotr="chooseSotr" @close="closeDistFunc" :extractStore="extractStore"
			:lastExtract="lastExtract" :deliverySetting="deliverySetting"></Dist>

	</view>
</template>

<script>
	import uniPopup from '@/components/uni-popup.vue';
	import Myinfo from './confirm-order/my-info';
	import Coupon from './confirm-order/coupon';
	import Dist from './confirm-order/distr';
	import {
		pay
	} from '@/common/pay.js';

	export default {
		components: {
			Myinfo,
			Coupon,
			Dist
		},
		data() {
			return {
				/*是否加载完成*/
				loading: true,
				options: {},
				indicatorDots: true,
				autoplay: true,
				interval: 2000,
				duration: 500,
				/*商品id*/
				product_id: '',
				/*商品数量*/
				product_num: '',
				/*商品数据*/
				ProductData: [],
				/*订单数据数据*/
				OrderData: [],
				// 是否存在收货地址
				existAddress: false,
				/*默认地址*/
				Address: {
					region: []
				},
				extractStore: [],
				lastExtract: {},
				productSkuId: 0,
				/*配送方式*/
				delivery: 10,
				/*自提店id*/
				storeId: 0,
				/*优惠券id*/
				couponId: -1,
				/*是否使用积分,默认使用*/
				isUsePoints: 1,
				remark: '',
				/*支付方式*/
				pay_type: 20,
				/*是否显示优惠券*/
				isCoupon: false,
				/*优惠券列表*/
				platformCouponList: {},
				// 商家优惠券
				couponList: [],
				/*优惠券数量*/
				couponNum: 0,
				/* 是否显示配送方式 */
				isDist: false,
				/*消息模板*/
				temlIds: [],
				productCouponid: 0,
				chooseSotr: 0,
				/* 支持的配送方式 */
				deliverySetting: [],
				chooseDelivery: 10,
				storeData: {},
				// 当前店铺选择的门店
				chooseStoreId: 0,
				storeList: {},
				balance: '',
				storeOpen: true,
				settledRule: {},
				roomId: 0,
			};
		},
		onLoad(options) {
			let self = this;
			self.options = options;
			self.roomId = options.roomId || 0;
			self.$fire.on('selectStoreId', function(e) {
				self.extractStore = e;
				self.chooseStoreId = e.storeId;
			});
			self.$fire.on('checkedfir', function(n) {
				self.chooseDelivery = n;
			})
		},
		onShow() {
			this.getData();
		},
		mounted() {},
		methods: {
			init() {
				let key = '';
				let value = "";
				let self = this;
				self.ProductData.forEach((item, index) => {
					key = item.shopSupplierId;
					value = {
						couponId: item.orderData.couponId,
						delivery: item.orderData.delivery,
						storeId: 0,
						remark: ""
					}
					self.storeData = {
						...self.storeData,
						[key]: value
					}
				});
				console.log(self.storeData);
			},
			/**/
			hasType(e) {
				if (this.deliverySetting.indexOf(e) != -1) {
					return true;
				} else {
					return false;
				}
			},
			/*支付方式选择*/
			payTypeFunc(e) {
				this.pay_type = e;
			},
			/*是否使用积分选择*/
			onShowPoints: function(e) {
				let self = this;
				if (e.target.value == true) {
					self.isUsePoints = 1;
				} else {
					self.isUsePoints = 0;
				}
				self.getData();
			},
			/*选择优惠卷*/
			onTogglePopupCoupon(e, id) {
				let self = this;
				if (id != 0) {
					self.chooseSotr = id.shopSupplierId;
				} else {
					self.chooseSotr = 0;
				}
				self.isCoupon = true;
				self.couponList = e;
			},
			/*关闭优惠券*/
			closeCouponFunc(e) {
				let self = this;
				if (e && typeof e != 'number') {
					self.isCoupon = false;
					return;
				}
				if (self.chooseSotr != 0) {
					let storid = self.chooseSotr;
					if (e > 0) {
						self.storeData[storid].couponId = e;
					} else {
						self.storeData[storid].couponId = 0;
					}
					self.chooseSotr = 0;
				} else {
					if (e > 0) {
						self.couponId = e;
					} else {
						self.couponId = 0;
					}
				}
				self.isCoupon = false;
				self.getData();
			},
			/*获取数据*/
			getData() {
				let self = this;
				uni.showLoading({
					title: '加载中'
				});

				let callback = function(res) {
					self.OrderData = res.data.orderData;
					self.temlIds = res.data.templateArr;
					self.ProductData = res.data.supplierList;
					self.existAddress = self.OrderData.existAddress;
					self.Address = self.OrderData.address;
					self.lastExtract = self.OrderData.lastExtract;
					self.settledRule = res.data.settledRule;
					self.couponList = self.OrderData.couponList;
					self.couponId = self.OrderData.couponIdSys;
					if(self.couponList != null){
						self.couponNum = Object.keys(self.couponList).length;
					}
				
					//#ifdef MP-WEIXIN
					self.storeOpen = res.data.storeOpen;
					//#endif
					if (self.OrderData.orderPayPrice == 0) {
						self.pay_type = 10;
					}
					if (JSON.stringify(self.storeData) == "{}") {
						self.init()
					}
					self.loading = false;
				};

				// 请求的参数
				let params = {
					delivery: self.delivery,
					storeId: self.storeId,
					couponId: self.couponId,
					isUsePoints: self.isUsePoints,
					paySource: self.getPlatform(),
				};
        if(self.roomId && self.roomId != 'undefined'){
          params.roomId = self.roomId;
        }else {
          params.roomId = 0;
        }
				if (JSON.stringify(self.storeData) == "{}") {
					params = params;
				} else {
					params = {
						...params,
						supplier: self.storeData
					};
				}
				//直接购买
				if (self.options.orderType === 'buy') {
					self._postBody(
						'order/order/toBuy', Object.assign({}, params, {
							productId: self.options.productId,
							productNum: self.options.productNum,
							specSkuId: self.options.specSkuId
						}),
						function(res) {
							callback(res);
						},err=>{
							uni.navigateBack()
						}
					);
				}
				// 购物车结算
				else if (self.options.orderType === 'cart') {
					self._postBody(
						'order/order/toCart',
						Object.assign({}, params, {
							cartIds: self.options.cartIds || 0
						}),
						function(res) {
							callback(res);
						},err=>{
							//uni.navigateBack()
                //地址错误，跳转我的地址页面
                uni.navigateTo({
                  url:"/pages/user/address/address?source=order"
                })
              }
					);
				}
				// 积分兑换结算
				else if (self.options.orderType == 'points') {
					console.log(self.options);
					self._postBody(
						'plus/points/order/toBuy', Object.assign({}, params, {
							pointProductId: self.options.pointProductId,
							pointProductSkuId: self.options.pointProductSkuId,
							productSkuId: self.options.productSkuId,
							productNum: self.options.productNum
						}),
						function(res) {
							callback(res);
						},err=>{
							uni.navigateBack()
						}
					);
				}
				// 限时秒杀
				else if (self.options.orderType === 'seckill') {
					self._postBody(
						'plus/seckill/order/toBuy', Object.assign({}, params, {
							seckillProductId: self.options.seckillProductId,
							productSkuId: self.options.productSkuId,
							seckillProductSkuId: self.options.seckillProductSkuId,
							productNum: self.options.productNum
						}),
						function(res) {
							callback(res);
						},err=>{
							uni.navigateBack()
						}
					);
				}
				//砍价
				else if (self.options.orderType === 'bargain') {
					self._postBody(
						'plus/bargain/order/toBuy',
						Object.assign({}, params, {
							bargainProductId: self.options.bargainProductId,
							productSkuId: self.options.productSkuId,
							bargainProductSkuId: self.options.bargainProductSkuId,
							bargainTaskId: self.options.bargainTaskId
						}),
						function(res) {
							callback(res);
						},err=>{
							uni.navigateBack()
						}
					);
				}
				//拼团
				else if (self.options.orderType === 'assemble') {
					self._postBody(
						'plus/assemble/order/toBuy',
						Object.assign({}, params, {
							assembleProductId: self.options.assembleProductId,
							productSkuId: self.options.productSkuId,
							assembleProductSkuId: self.options.assembleProductSkuId,
							productNum: self.options.productNum,
							assembleBillId: self.options.assembleBillId,
						}),
						function(res) {
							callback(res);
						},err=>{
							uni.navigateBack()
						}
					);
				}
			},

			toDecimal2(x) {
				var f = parseFloat(x);
				if (isNaN(f)) {
					return "0.00";
				}
				var f = Math.round(x * 100)
				var n = Math.round(x * 1000)
				var r = n.toString().split("");
				r = r[r.length - 1];
				if (r >= 5) {
					f = (f - 1) / 100
				} else {
					f = f / 100
				}
				var s = f.toString();
				var rs = s.indexOf('.');
				if (rs < 0) {
					rs = s.length;
					s += '.';
				}
				while (s.length <= rs + 2) {
					s += '0';
				}
				return s;
			},
			/* 配送选择 */
			DistUp(item) {
				if (item.orderData.delivery == 30) {
					return;
				}
				let self = this;
				self.storeId = item.shopSupplierId;
				self.chooseSotr = item.shopSupplierId;
				self.chooseDelivery = item.orderData.delivery;
				self.deliverySetting = item.orderData.deliverySetting;
				if(item.orderData.extractStore == null){
					self.extractStore = {};
				}else{
					self.extractStore = item.orderData.extractStore;
				}
				self.isDist = true;
			},
			/* 关闭配送选择 */
			closeDistFunc(e) {
				let self = this;
				self.chooseDelivery = e;
				self.isDist = false;
				let storname = '';
				if (self.extractStore.region) {
					storname = self.extractStore.province + self.extractStore.city + self.extractStore
						.region + self.extractStore.storeName;
				}
				let storid = self.chooseSotr;
				let chooseStoreId = self.chooseStoreId;

				self.delivery = self.chooseDelivery;
				self.storeId = storid;
				self.storeData[storid].storeId = chooseStoreId;
				self.storeData[storid].delivery = self.chooseDelivery;
				self.storeList[storid] = storname;
				self.chooseSotr = 0;
				self.getData();
			},
			objKeys(obj, n) {
				if (obj) {
					if (n == 1) {
						return Object.keys(obj).length
					} else {
						return Object.keys(obj)
					}
				}
			},
			/*提交订单*/
			SubmitOrder() {
				let self = this;
				if (self.existAddress) {
					uni.showLoading({
						title: '加载中',
						mask: true
					});

					let params = {
						couponId: self.couponId,
						isUsePoints: self.isUsePoints
					};
          if(self.roomId && self.roomId != 'undefined'){
            params.roomId = self.roomId;
          }else {
            params.roomId = 0;
          }
					params = Object.assign(params, {
						supplier: self.storeData
					});

					// 创建订单-直接下单
					let url = '';
					if (self.options.orderType === 'buy') {
						url = 'order/order/buy';
						params = Object.assign(params, {
							productId: self.options.productId,
							productNum: self.options.productNum,
							specSkuId: self.options.specSkuId,
						});
					}
					// 创建订单-购物车结算
					if (self.options.orderType === 'cart') {
						url = 'order/order/cart';
						params = Object.assign(params, {
							cartIds: self.options.cartIds || 0
						});
					}

					// 创建订单-积分兑换
					if (self.options.orderType === 'points') {
						url = 'plus/points/order/buy';
						params = Object.assign(params, {
							pointProductId: self.options.pointProductId,
							pointProductSkuId: self.options.pointProductSkuId,
							productSkuId: self.options.productSkuId,
							productNum: self.options.productNum
						});
					}
					// 创建订单-限时秒杀
					if (self.options.orderType === 'seckill') {
						url = 'plus/seckill/order/buy';
						params = Object.assign(params, {
							seckillProductId: self.options.seckillProductId,
							productSkuId: self.options.productSkuId,
							seckillProductSkuId: self.options.seckillProductSkuId,
							productNum: self.options.productNum
						});
					}
					// 创建订单-砍价
					if (self.options.orderType === 'bargain') {
						url = 'plus/bargain/order/buy';
						params = Object.assign(params, {
							bargainProductId: self.options.bargainProductId,
							productSkuId: self.options.productSkuId,
							bargainProductSkuId: self.options.bargainProductSkuId,
							bargainTaskId: self.options.bargainTaskId
						});
					}

					// 创建订单-限时拼团
					if (self.options.orderType === 'assemble') {
						url = 'plus/assemble/order/buy';
						params = Object.assign(params, {
							assembleProductId: self.options.assembleProductId,
							productSkuId: self.options.productSkuId,
							assembleProductSkuId: self.options.assembleProductSkuId,
							assembleBillId: self.options.assembleBillId,
							productNum: self.options.productNum,
						});
					}

					let callback = function() {
						self._postBody(url, params, function(res) {
							let pages = '/pages/order/cashier?orderType=10&orderId=' + res.data;
							self.gotoPage(pages,'reLaunch');
						});
					};

					self.subMessage(self.temlIds, callback);
				} else {
					uni.showToast({
						title: '请选择配送地址'
					})
				}
			},
		}
	};
</script>

<style lang="scss">
	page {
		background-color: #F2F2F2;
	}

	.f-d-c {
		flex-direction: column;
	}

	.wrap {
		padding-bottom: 110rpx;
	}

	.d-f-c {
		display: flex;
		justify-content: space-between;
		align-items: center;
		overflow: hidden;
		padding: 30rpx 0;
		margin: 0 30rpx;
		font-size: 24rpx;
		border-bottom: 1rpx solid #EEEEEE;
	}

	.dfjac {
		display: flex;
		align-items: center;
	}

	.d-txar {
		white-space: nowrap;
		width: 200px;
		margin-right: 34rpx;
	}

	.extp {
		color: #9e9e9e;
		margin-left: 34rpx;
	}

	.vender .list .item {
		border-bottom: none;
	}

	.icon-jiantou {
		margin-left: 15rpx;
	}

	.icon-dianpu1 {
		margin-right: 15rpx;
		font-size: 28rpx;
		color: #333333;
	}

	.sup_itemtit {
		padding: 40rpx 30rpx;
		display: flex;
		align-items: center;
	}

	.vender .total-box {
		height: 87rpx;
		line-height: 87rpx;
		border-bottom: 16rpx solid #f2f2f2;
	}

	.d-f {
		display: flex;
	}

	.zt {
		padding: 2rpx 10rpx;
		margin-right: 10rpx;
		border-radius: 8rpx;
		font-size: 22rpx;
	}

	.icon-box .icon-zhifubao {
		color: #1296db;
		font-size: 50rpx;
	}

	.icon-dianpu1 {}

	.text_area {
		width: 100%;
		height: 120rpx;
		background: #f2f2f2;
		border-radius: 6rpx;
		padding: 20rpx;
		box-sizing: border-box;
		font-size: 24rpx;
	}

	.icon-xuanze {
		font-size: 38rpx;
	}

	.buy-checkout .item {
		padding: 30rpx 0;
		margin: 0 30rpx;
	}
</style>