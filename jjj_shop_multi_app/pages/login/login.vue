<template>
	<view class="login-container">

		<view class="wechatapp">
			<view class="header">
        <!-- #ifdef MP-WEIXIN -->
				<open-data class="" type="userAvatarUrl"></open-data>
        <!-- #endif -->
			</view>
		</view>
		<view class="auth-title">申请获取以下权限</view>
		<view class="auth-subtitle">获得你的公开信息（昵称、头像等）</view>
		<view class="login-btn">
			<!-- #ifdef MP-WEIXIN -->
			<button class="btn-normal" @click="getUserInfo">授权登录</button>
			<!-- #endif -->
		</view>
		<view class="no-login-btn">
			<button class="btn-normal" @click="onNotLogin">暂不登录</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				background: '',
				listData: [],
				invitationId: 0
			}
		},
		onLoad(e) {
			this.invitationId = uni.getStorageSync('invitationId') ? uni.getStorageSync('invitationId') : 0;
		},
		methods: {
			onNotLogin: function() {
				uni.switchTab({
					url: '/pages/index/index'
				})
			},
			getUserInfo: function() {
				let self = this;
				wx.getUserProfile({
					lang: 'zh_CN',
					desc: '用于完善会员资料', 
					success: (res) => {
						if (res.errMsg !== 'getUserProfile:ok') {
							return false;
						}
						uni.showLoading({
							title: "正在登录",
							mask: true
						});
						// 执行微信登录
						uni.login({
							success(res_login) {
								// 发送用户信息
								self._postBody('user/userWx/login', {
									invitationId: self.invitationId,
									code: res_login.code,
									userInfo: res.rawData,
									encrypted_data: encodeURIComponent(res.encryptedData),
									iv: encodeURIComponent(res.iv),
									signature: res.signature,
									refereeId: uni.getStorageSync('refereeId')
								}, result => {
									// 记录token userId
									uni.setStorageSync('token', result.data.token);
									uni.setStorageSync('userId', result.data.loginUserVo.userId);
									// 执行回调函数
									uni.navigateBack();
								}, false, () => {
									uni.hideLoading();
								});
							}
						});
					}
				});
			},
		},
	}
</script>

<style>
	.login-container {
		padding: 30rpx;
	}

	.wechatapp {
		padding: 80rpx 0 48rpx;
		border-bottom: 1rpx solid #e3e3e3;
		margin-bottom: 72rpx;
		text-align: center;
	}

	.wechatapp .header {
		width: 190rpx;
		height: 190rpx;
		border: 2px solid #fff;
		margin: 0rpx auto 0;
		border-radius: 50%;
		overflow: hidden;
		box-shadow: 1px 0px 5px rgba(50, 50, 50, 0.3);
	}

	.auth-title {
		color: #585858;
		font-size: 34rpx;
		margin-bottom: 40rpx;
	}

	.auth-subtitle {
		color: #888;
		margin-bottom: 88rpx;
		font-size: 28rpx;
	}

	.login-btn {
		padding: 0 20rpx;
	}

	.login-btn button {
		height: 88rpx;
		line-height: 88rpx;
		background: #04be01;
		color: #fff;
		font-size: 30rpx;
		border-radius: 999rpx;
		text-align: center;
	}

	.no-login-btn {
		margin-top: 20rpx;
		padding: 0 20rpx;
	}

	.no-login-btn button {
		height: 88rpx;
		line-height: 88rpx;
		background: #dfdfdf;
		color: #fff;
		font-size: 30rpx;
		border-radius: 999rpx;
		text-align: center;
	}
</style>
