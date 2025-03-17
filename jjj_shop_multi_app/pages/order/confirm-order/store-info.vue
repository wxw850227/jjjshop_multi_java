<template>
	<view style="flex: 1;">
		<!--地址-->
		<template v-if="!extractStore.storeId">
			<view class="d-b-c pr20" @click="addAddress()">
				<view class="add-address d-s-c">
					<view class="icon-box mr10">
						<span class="icon iconfont icon-dizhi1"></span>
					</view>
					<text>请选择自提点</text>
				</view>
				<view>
					<i class='iconfont icon-jiantou'></i>
				</view>
			</view>
		</template>
		<template v-else>
			<view class="d-b-c pr20" @click="addAddress()">
				<view class="address-defalut-wrap">
					<view class="info d-s-s">
						<text class="state">当前自提点</text>
						<view class="province-c-a d-s-s flex-1">
							<text>{{ extractStore.province }}</text>
							<text>{{ extractStore.city }}</text>
							<text>{{ extractStore.region }}</text>
						</view>
					</view>
					<view class="address">
						<text class="fb gray3">{{ extractStore.storeName }}</text>
						<view class="icon-box"></view>
					</view>
					<view class="user">
						<text class="name">{{ extractStore.address }}</text>
						<text class="tel">{{ extractStore.phone }}</text>
					</view>
				</view>
				<view>
					<i class='iconfont icon-jiantou'></i>
				</view>
			</view>
		</template>
		<Adress :isAddress='isAddress' :chooseSotr='chooseSotr' :storeId='storeId' @close="closeAdress"></Adress>
	</view>
</template>

<script>
	import Adress from './address/address';
	export default {
		data() {
			return {
				isAddress: false,
				storeId: 0
			};
		},
		components: {
			Adress,
		},
		props: ['extractStore', 'lastExtract','chooseSotr'],
		onLoad() {

		},
		mounted() {

		},
		methods: {
			/*添加地址*/
			addAddress() {
				let storeId = -1;
				if (this.extractStore.storeId) {
					storeId = this.extractStore.storeId;
				}
				this.storeId = storeId;
				this.isAddress = true;
			},
			closeAdress() {
				this.isAddress = false;
			}
		}
	};
</script>

<style scoped>
	.buy-checkout.vender .item .key {
		width: 200rpx;
	}

	.pr20 {
		padding-right: 20rpx;
		padding-bottom: 60rpx;
	}
</style>
