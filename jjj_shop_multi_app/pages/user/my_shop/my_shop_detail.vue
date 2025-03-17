<template>
	<view>
		<view class="detail_data">
				<view>
					<view class="detail_data_title">店铺结算 <text class="detail_data_date ml20"> 订单日期：{{detail.createTime}}</text></view>
				</view>
				<view class="detail_data_content">
					<view class="detail_data_item">
						<view class="data_price">{{detail.orderMoney}}</view>
						<view class="data_txt">订单结算金额</view>
					</view>
					<view class="detail_data_item">
						<view class="data_price">{{detail.payMoney}}</view>
						<view class="data_txt">支付金额</view>
					</view>
					<view class="detail_data_item">
						<view class="data_price">{{detail.expressMoney}}</view>
						<view class="data_txt">运费金额</view>
					</view>
					<view class="detail_data_item">
						<view class="data_price">{{detail.realSupplierMoney}}</view>
						<view class="data_txt">店铺收入金额</view>
					</view>
					<view class="detail_data_item">
						<view class="data_price"> {{detail.supplierMoney}}</view>
						<view class="data_txt">店铺结算总金额</view>
					</view>
					<view class="detail_data_item">
						<view class="data_price">{{detail.refundMoney}}</view>
						<view class="data_txt">店铺退款金额</view>
					</view>
<!--					<view class="detail_data_item">-->
<!--						<view class="data_price">{{detail.agentMoney}}</view>-->
<!--						<view class="data_txt">分销佣金</view>-->
<!--					</view>-->
					<view class="detail_data_item">
						<view class="data_price">{{detail.sysMoney}}</view>
						<view class="data_txt">平台抽成金额</view>
					</view>
					<view class="detail_data_item">
						<view class="data_price">{{detail.realSysMoney}}</view>
						<view class="data_txt">平台结算总抽成</view>
					</view>
					<view class="detail_data_item">
						<view class="data_price">{{ detail.refundSysMoney }}</view>
						<view class="data_txt">平台退款抽成</view>
					</view>
				</view>
			</view>
			<view class="detail_data">
				<view class="detail_data_title">商品信息</view>
				<view class="detail_data_content m-bottom20" v-for="(item,index) in product_list" :key="index">
					<view class="d-s-c p20">
						<view>
							<image class="detail_prolist_img" :src="item.productImage" mode="aspectFill"></image>
						</view>
						<view>
							<view class="detail_prolist_name">{{item.productName}}</view>
							<view v-if="item.productAttr!=''" class="gray9">{{item.productAttr}}</view>
						</view>
					</view>
					<view class="detail_data_content">
						<view class="detail_data_item">
							<view class="data_price">x{{item.totalNum}}</view>
							<view class="data_txt">购买数量</view>
						</view>
						<view class="detail_data_item">
							<view class="data_price">{{item.totalPayPrice}}</view>
							<view class="data_txt">支付金额</view>
						</view>
						<view class="detail_data_item">
							<view class="data_price" v-if="item.refundMoney">{{item.refundMoney}}</view>
							<view class="data_price" v-else>0</view>
							<view class="data_txt">退款金额</view>
						</view>

					</view>
				</view>
			</view>
	</view>
	</template>

<script>
	export default {
		data() {
			return {
				order_id:'',//订单id
				detail:{},
				product_list:[],
				specData:{},
				index:0,
				price:0
			}
		},
		onLoad(e) {
			let self = this;
			self.settled_id = e.settled_id
		},
		onShow() {
			this.getData();
		},
		methods: {
			//获取商品数据
			getData(){
				let self = this;
				self._post('supplier/index/settledetail', {
						settledId: self.settled_id,
					},(res)=>{
					self.product_list=res.data.order.product;
					self.detail = res.data.model;
					self.detail.createTime = res.data.order.createTime;
				})
			},
		}
	}
</script>

<style>
	page{
		background: #ffffff;
	}
	.detail_data{
		background-color: #ffffff;
		padding: 30rpx;
		/* width: 85%; */
		margin: 0 auto;
	}
	.detail_data_title{
		font-size: 28rpx;
		margin-bottom: 18rpx;
	}
	.detail_data_date{
		font-size: 23rpx;

	}
	.detail_data_content{
		background-color: #e4ecff;
		border-radius: 15rpx;
		padding-top: 20rpx;
		display: flex;
		flex-wrap: wrap;
	}
	.detail_data_item{
		width: 33%;
		margin: 15rpx 0;
		text-align: center;
	}
	.data_price{
		color: #000000;
		font-size: 28rpx;
		margin-bottom: 14rpx;
	}
	.data_txt{
		font-size: 23rpx;
		color: #8d95a0;
		margin-bottom: 30rpx;
	}
	.detail_prolist_img{
		width: 120rpx;
		height: 120rpx;
		border-radius: 10rpx;
		margin-right: 15rpx;
	}
	.detail_prolist_name{
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
	}
	.m-bottom20{
		margin-bottom: 20rpx;
	}
</style>
