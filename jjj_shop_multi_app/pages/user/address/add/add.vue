<template>
	<view class="address-form">
		<form @submit="formSubmit" @reset="formReset">
			<view class="bg-white p-0-30 f30">
				<view class="d-s-c border-b-d9">
					<text class="key-name">收货人</text>
					<input class="ml20 flex-1 f32 p-30-0" name="name" type="text" placeholder-class="grary9" v-model="address.name"
					 placeholder="请输入收货人姓名" />
				</view>
				<view class="d-s-c border-b-d9">
					<text class="key-name">联系方式</text>
					<input class="ml20 flex-1 f32 p-30-0" name="phone" type="text" placeholder-class="grary9" v-model="address.phone"
					 placeholder="请输入收货人手机号" />
				</view>
				<view class="d-s-c border-b-d9">
					<text class="key-name">所在地区</text>
					<view class="input-box flex-1" @click="showMulLinkageThreePicker">
						<input class="ml20 f32 flex-1 p-30-0" type="text"  placeholder-class="grary9" placeholder="" v-model="selectCity"
						 disabled="true" style="pointer-events: none;" />
					</view>
				</view>
				<view class="d-s-c border-b-d9">
					<text class="key-name">详细地址</text>
					<textarea class="ml20 flex-1 p-30-0 lh150" name="detail" :auto-height="true" v-model="address.detail"
					 placeholder-class="grary9" placeholder="请输入街道小区楼牌号等"></textarea>
				</view>
			</view>
			<view class="p30"><button type="primary" form-type="submit" class="btn-gcred f32 mt60 addBtn">保存</button></view>
		</form>
		<mpvue-city-picker v-if="is_load" ref="mpvueCityPicker" :province="province" :city="city" :area="area" :pickerValueDefault="cityPickerValueDefault" @onConfirm="onConfirm"></mpvue-city-picker>
	</view>
</template>

<script>
	import mpvueCityPicker from '@/components/mpvue-citypicker/mpvueCityPicker.vue';
	export default {
		components: {
			mpvueCityPicker
		},
		data() {
			return {
				cityPickerValueDefault: [0, 0, 0],
				selectCity: '选择省,市,区',
				provinceId: 0,
				cityId: 0,
				regionId: 0,
				address: {},
				delta: 1,
				province: [],
				city: [],
				area: [],
				is_load: false
			};
		},
		onLoad: function(options) {
			this.delta = options.delta;
			this.getData();
		},
		methods: {
			// 获取省市区
			getData(){
				let self = this;
				self._post('settings/getRegion', {}, function(res) {
					self.province = res.data[0];
					self.city = res.data[1];
					self.area = res.data[2];
					self.is_load = true;
				});
			},
			/*提交*/
			formSubmit: function(e) {
				let self = this;
				var formdata = e.detail.value;
				formdata.provinceId = self.provinceId;
				formdata.cityId = self.cityId;
				formdata.regionId = self.regionId;
				console.log(formdata);
				if (formdata.name == '') {
					uni.showToast({
						title: '请输入收货人姓名',
						duration: 1000,
						icon: 'none'
					});
					return false;
				}

				if (formdata.phone == '') {
					uni.showToast({
						title: '请输入手机号码',
						duration: 1000,
						icon: 'none'
					});
					return false;
				}
				
				if (formdata.provinceId == 0 || formdata.cityId == 0 || formdata.regionId == 0) {
					uni.showToast({
						title: '请选择完整省市区',
						duration: 1000,
						icon: 'none'
					});
					return false;
				}

				if (formdata.detail == '') {
					uni.showToast({
						title: '请输入街道小区楼牌号等',
						duration: 1000,
						icon: 'none'
					});
					return false;
				}
				self._postBody('user/address/add', formdata, function(res) {
					self.showSuccess(res.msg, function() {
						// #ifndef H5
						uni.navigateBack({
							delta: parseInt(self.delta)
						});
						// #endif
						// #ifdef H5
						history.go(-self.delta);
						// #endif
					});
				});
			},

			formReset: function(e) {
				console.log('清空数据');
			},

			/*三级联动选择*/
			showMulLinkageThreePicker() {
				this.$refs.mpvueCityPicker.show();
			},

			/*确定选择的省市区*/
			onConfirm(e) {
				this.selectCity = e.label;
				this.provinceId = e.cityCode[0];
				this.cityId = e.cityCode[1];
				this.regionId = e.cityCode[2];
			}
		}
	};
</script>

<style>
	page {
		background-color: #FFFFFF;
	}

	.address-form {
		/* border-top: 16rpx solid #f2f2f2; */
	}

	.address-form .key-name {
		width: 140rpx;
		font-size: 32rpx
	}

	.address-form .btn-red {
		height: 88rpx;
		line-height: 88rpx;
		border-radius: 44rpx;
		box-shadow: 0 8rpx 16rpx 0 rgba(226, 35, 26, .6);
	}

	.addBtn {
		height: 80rpx;
		line-height: 80rpx;
		border-radius: 40rpx;
	}
</style>
