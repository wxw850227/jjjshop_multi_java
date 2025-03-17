<template>
	<view>
		<!-- <apply v-if="supplierStatus == -1 || supplierStatus == 2"></apply> -->
		<apply v-if="form || supplierStatus == -1"></apply>
		<!-- <application v-if="supplierStatus == 0 || supplierStatus == 2" @reset-form="resetForm"></application> -->
		<application v-else @reset-form="resetForm"></application>
	</view>
</template>

<script>
import apply from './apply.vue';
import application from './application_status.vue';
export default {
	components: {
		apply,
		application
	},
	data() {
		return {
			supplierStatus: -100,
			supplierIsDelete: 0,
			form: false,
		};
	},
	onShow() {
		this.getData();
	},
	methods: {
		getData() {
			let self = this;
			uni.showLoading({
				title: '加载中...'
			});
			self._post('supplier/apply/detail',{
				
			}, res => {
				if(res.data.status || res.data.status == 0) {
					self.supplierStatus = res.data.status;
				}else {
					self.supplierStatus = -1;
				}
				if (self.supplierStatus == 1 && res.data.supplierIsDelete != 1) {
					self.gotoPage('pages/user/my_shop/my_shop', 'redirect');
				} else if (res.data.supplierIsDelete == 1) {
					uni.hideLoading();
					uni.showModal({
						content: '店铺异常,请联系客服处理'
					});
				} else {
					let title = '';
					if(self.supplierStatus == 0){
						title = '申请入驻';
					}else{
						title = '申请审核中';
					}
					uni.setNavigationBarTitle({
					    title: title
					});
					
					uni.hideLoading();
				}
			});
		},
		resetForm(){
			this.form = true;
			console.log("llll")
		}
	}
};
</script>

<style></style>
