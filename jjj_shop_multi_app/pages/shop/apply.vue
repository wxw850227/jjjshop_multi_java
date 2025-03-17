<template>
	<view class="index">
		<view class="bg_image"><image :src="bg_image"></image></view>
		<view class="certification">
			<form @submit="formSubmit" @reset="formReset">
				<view class="certification_head">
					<view class="certification_head_body">
						<view class="certification_head_body_item">
							<view class="icon iconfont icon-qiye"></view>
							<input type="text" name="storeName" v-model="form.storeName" placeholder="请输入店铺名称" />
						</view>
						<view class="certification_head_body_item">
							<view class="icon iconfont icon-ziyuan1"></view>
							<input type="text" name="userName" v-model="form.userName" placeholder="请输入真实姓名" />
						</view>
						<view class="certification_head_body_item">
							<view class="icon iconfont icon-phone"></view>
							<input type="text" name="mobile" v-model="form.mobile" placeholder="请输入手机号码" />
							<button type="default" v-if="smsOpen == 1" :disabled="isSend" @click="getCode(form.mobile)">{{ send_btn_txt }}</button>
						</view>
						<view class="certification_head_body_item">
							<view class="icon iconfont icon-mima"></view>
							<input type="text" name="password" v-model="form.password" password="true" placeholder="请输入登录密码" />
						</view>
						<view class="certification_head_body_item" v-if="smsOpen == 1">
							<view class="icon iconfont icon-yanzhengma"></view>
							<input type="text" name="code" v-model="form.code" placeholder="请输入验证码" />
						</view>
						<picker class=""  @change="changeSelect($event)" :range="classify_data_name">
							<view class="certification_head_body_item d-b-c">
								<view class="make-item input-box flex-1" v-if="classify_data_name_active">{{ classify_data_name_active }}</view>
								<view class="make-item input-box flex-1" style="color: #777777;" v-else>请选择您店铺的主营项目</view>
								<view class="icon iconfont icon-jiantou" style="font-size: 24rpx;"></view>
							</view>
						</picker>
					</view>
				</view>
				<view class="certification_body">
					<view class="login_index_updata_name">
						<text style="color:red;">*</text>
						<text style="font-size: 32rpx;font-weight: 500;color: #000000;">请上传营业执照</text>
					</view>
					<view class="login_index_updata_updata">
						<view class="login_index_updata_updata_item2" @click="openUpload('license')">
							<view class="login_index_updata_updata_item_body">
								<view v-if="business_id_path != ''" class="img"><image :src="business_id_path"></image></view>
								<view v-if="business_id_path == ''">点击上传营业执照</view>
								<input style="display:none" name="business_id" v-model="form.business_id" />
							</view>
						</view>
					</view>
				</view>
				<view class="login_index_btn"><button form-type="submit" type="default">确定</button></view>
				<Upload v-if="isupload" :isupload="isupload" :type="type" @getImgs="getImgsFunc">上传图片</Upload>
			</form>
		</view>
	</view>
</template>

<script>
import Upload from '@/components/upload/upload';
export default {
	components: {
		/*编辑组件*/
		Upload
	},
	data() {
		return {
			form: {
				image_frontid: 0,
				business_id: 0,
				mobile: '',
				password: ''
			},
			isupload: false,
			type: 'frontid',
			image_frontid_path: '',
			business_id_path: '',
			bg_image: '',
			classify_data_name: '', //列表名字
			classify_data_id: '', //列表id
			classify_data_name_active: '', //当前选中的列表名字
			classify_data_id_active: '', //当前选中的列表id
			/*当前秒数*/
			second: 60,
			send_btn_txt: '获取验证码',
			/*是否已发验证码*/
			is_send: false,
			smsOpen: 0
		};
	},
	created() {
		this.getData();
		this.get_classify();
	},
	methods: {
		/*改变发送验证码按钮文本*/
		changeMsg() {
			if (this.second > 0) {
				this.send_btn_txt = this.second + '秒';
				this.second--;
				setTimeout(this.changeMsg, 1000);
			} else {
				this.send_btn_txt = '获取验证码';
				this.second = 60;
				this.is_send = false;
			}
		},
		/*获取数据*/
		getData() {
			let self = this;
			self._post('user/index/index', {}, function(res) {
				self.bg_image = res.data.setting.supplierImage;
			});
		},
		//获取验证码
		getCode(mobile) {
			let self = this;
			self._post(
				'supplier/apply/sendCode',
				{
					mobile: mobile
				},
				function(res) {
					self.is_send = true;
					self.changeMsg();
				}
			);
		},
		//获取分类
		get_classify() {
			let self = this;
			self._post('supplier/apply/category', {}, function(res) {
				let list_name = [];
				let list_id = [];
				//合成select列表
				for (var i = 0; i < res.data.list.length; i++) {
					list_name.push(res.data.list[i].name);
				}
				//合成上传所需id数组
				for (var i2 = 0; i2 < res.data.list.length; i2++) {
					list_id.push(res.data.list[i2].categoryId);
				}
				self.classify_data_name = list_name;
				self.classify_data_id = list_id;
				self.smsOpen = res.data.smsOpen;
			});
		},
		//选择分类
		select() {
			let self = this;
			uni.showActionSheet({
				itemList: self.classify_data_name,
				success: function(res) {
					self.classify_data_name_active = self.classify_data_name[res.tapIndex];
					self.classify_data_id_active = self.classify_data_id[res.tapIndex];
				},
				fail: function(res) {}
			});
		},
		/*提交*/
		formSubmit: function(e) {
			let self = this;
			var formdata = e.detail.value;
			formdata.businessId = self.form.business_id;
			console.log(formdata);
			console.log(self.form);
			formdata.categoryId = self.classify_data_id_active;
			if (self.classify_data_name_active == '') {
				uni.showToast({
					title: '请填主营类别',
					duration: 1000,
					icon: 'none'
				});
				return false;
			}
			if (formdata.storeName == '') {
				uni.showToast({
					title: '请输入店铺名称',
					duration: 1000,
					icon: 'none'
				});
				return false;
			}
			if (formdata.businessId == 0) {
				uni.showToast({
					title: '请上传营业执照',
					duration: 1000,
					icon: 'none'
				});
				return false;
			}
			if (formdata.userName == '') {
				uni.showToast({
					title: '请输入真实姓名',
					duration: 1000,
					icon: 'none'
				});
				return false;
			}

			if (formdata.mobile == '') {
				uni.showToast({
					title: '请输入手机号码',
					duration: 1000,
					icon: 'none'
				});
				return false;
			}
			if (formdata.code == '' && self.smsOpen == 1) {
				uni.showToast({
					title: '请填写验证码',
					duration: 1000,
					icon: 'none'
				});
				return false;
			}
			if (formdata.password == '') {
				uni.showToast({
					title: '请填写登录密码',
					duration: 1000,
					icon: 'none'
				});
				return false;
			}

			let reg = /^((0\d{2,3}-\d{7,8})|(1[3456789]\d{9}))$/;
			if (!reg.test(formdata.mobile)) {
				uni.showToast({
					title: '手机号码格式不正确',
					duration: 1000,
					icon: 'none'
				});
				return false;
			}
			uni.showLoading({
				title: '正在提交',
				mask: true
			});
			self._postBody('supplier/apply/index', formdata, function(res) {
				self.showSuccess(
					res.msg,
					function() {
						self.gotoPage('/pages/shop/application_status');
					},
					null,
					function() {
						uni.hideLoading();
					}
				);
			});
		},
		/*上传*/
		openUpload(e) {
			this.type = e;
			this.isupload = true;
		},

		/*获取图片*/
		getImgsFunc(e) {
			console.log(e);
			if (e != null && e.length > 0) {
				if (this.type == 'frontid') {
					console.log(e[0].fileId);
					this.image_frontid_path = e[0].filePath;
					this.form.image_frontid = e[0].fileId;
				} else if (this.type == 'license') {
					console.log(e[0].fileId);
					this.business_id_path = e[0].filePath;
					this.form.business_id = e[0].fileId;
				}
			}
			this.isupload = false;
		},
		/* 下拉框 */
		changeSelect(e) {
			this.classify_data_id_active = this.classify_data_id[e.detail.value];
			this.classify_data_name_active = this.classify_data_name[e.detail.value];
		}
	}
};
</script>

<style lang="scss" scoped>
page {
	/* background: #FF3D19; */
	// background: rgb(243,106,36);
}
.bg_image {
	position: absolute;
	left: 0;
	top: 0;
	z-index: 1;
	width: 750rpx;
}
.bg_image image {
	width: 100%;
}
.iconfont {
	font-size: 48rpx;
}

.index {
	width: 750rpx;
	background: rgb(243, 106, 36);
	padding: 80rpx 0;
	min-height: 100vh;
	box-sizing: border-box;
}

.certification {
	width: 675rpx;
	margin: 0 auto;
	border-radius: 30rpx;
	padding: 30rpx;
	box-sizing: border-box;
	background-color: white;
	/* position: absolute;
		top: 260rpx;
		left: 37.5rpx; */
	margin-bottom: 50rpx;
	position: relative;
	z-index: 2;
}

.certification_head {
	width: 100%;
}

.certification_head_body {
	width: 100%;
}

.certification_head_body_item {
	width: 100%;
	height: 100rpx;
	display: flex;
	align-items: center;
	font-size: 32rpx !important;
}

.certification_head_body_item view {
	margin-right: 10rpx;
}

.certification_head_body_item button {
	width: 172rpx;
	height: 56rpx;
	line-height: 56rpx;
	border: 1rpx #e2231a solid;
	border-radius: 40rpx;
	font-size: 26rpx;
	color: #e2231a;
	padding: 0 20rpx;
}

.certification_body {
}

.login_index_updata_name {
	margin-top: 28rpx;
	margin-left: 48rpx;
}

.login_index_updata_updata {
	width: 90%;
	height: 360rpx;
	margin: 0 auto;
	margin-top: 35rpx;
}

.login_index_updata_updata_item2 {
	width: 100%;
	height: 360rpx;
	border: 1rpx #9a9a9a dotted;
	border-radius: 15rpx;
	text-align: center;
}

.login_index_updata_updata_item_body {
	width: 150rpx;
	height: 360rpx;
	text-align: center;
	margin: 0 auto;
	display: flex;
	justify-content: center;
	align-items: center;
}

.img image {
	width: 554rpx;
	height: 360rpx;
	position: relative;
}

.login_index_btn {
	position: relative;
	top: 30rpx;
	margin-bottom: 30rpx;
}

.login_index_btn button {
	width: 90%;
	height: 80rpx;
	background-color: #f36a24;
	border-radius: 50rpx;
	line-height: 80rpx;
	margin: 0 auto;
	color: white;
	font-size: 30rpx;
}

.certification_rule {
	position: relative;
	left: 500rpx;
	top: -10rpx;
}

.certification_rule text {
	color: #8a8a8a;
}

.certification_rule image {
	width: 24rpx;
	height: 24rpx;
	display: inline-block;
}

.classify_text {
	color: #808080;
	margin-left: 40rpx;
}

.select input {
	padding-left: 60rpx;
	color: #808080;
}
.make-item {
	padding-left: 60rpx;
}
</style>
