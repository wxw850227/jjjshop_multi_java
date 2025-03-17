<template>
	<view class="evaluate pb100" v-if="!loadding">
		<form @submit="formSubmit" @reset="formReset">
			<view class="evaluate-item p30" v-for="(item, index) in tableData" :key="index">
				<view class="product d-s-c">
					<view class="cover"><image :src="item.productImage" mode="aspectFit"></image></view>
					<view class="info ml20">
						<view class="name f28">{{ item.productName }}</view>
						<view class="price pt10 f22 red">
							¥
							<text class="f40">{{ item.productPrice }}</text>
						</view>
					</view>
				</view>
				<view class="grade evalu p-30-0 mt30">
					<view class="evalu-value">
						描述相符：
						<view class="eval">
							<i
								v-for="(itemEv, indexEv) in describe[index]"
								:key="indexEv"
								:class="itemEv ? 'icon iconfont icon-start' : 'icon iconfont icon-start1'"
								@click="choosees(indexEv, index)"
							></i>
						</view>
					</view>
					<view class="evalu-value">
						服务态度：
						<view class="eval">
							<i
								v-for="(itemEv, indexEv) in service[index]"
								:key="indexEv"
								:class="itemEv ? 'icon iconfont icon-start' : 'icon iconfont icon-start1'"
								@click="chooseServ(indexEv, index)"
							></i>
						</view>
					</view>
					<view class="evalu-value">
						配送服务：
						<view class="eval">
							<i
								v-for="(itemEv, indexEv) in logistics[index]"
								:key="indexEv"
								:class="itemEv ? 'icon iconfont icon-start' : 'icon iconfont icon-start1'"
								@click="chooseLog(indexEv, index)"
							></i>
						</view>
					</view>
				</view>
				<view class="grade d-b-c p-30-0 mt30">
					<view :class="item.score == 10 ? 'flex-1 d-c-c active' : 'flex-1 d-c-c'" @click="gradeFunc(item, 10, index)">
						<view class="item d-c-c">
							<text class="icon iconfont icon-pingjiahaoping"></text>
							<text class="ml10">好评</text>
						</view>
					</view>
					<view :class="item.score == 20 ? 'flex-1 d-c-c active' : 'flex-1 d-c-c'" @click="gradeFunc(item, 20, index)">
						<view class="item d-c-c">
							<text class="icon iconfont icon-pingjiazhongping"></text>
							<text class="ml10">中评</text>
						</view>
					</view>
					<view :class="item.score == 30 ? 'flex-1 d-c-c active' : 'flex-1 d-c-c'" @click="gradeFunc(item, 30, index)">
						<view class="item d-c-c">
							<text class="icon iconfont icon-pingjiachaping"></text>
							<text class="ml10">差评</text>
						</view>
					</view>
				</view>
				<view class="textarea-box d-s-c f28"><textarea class="p10 box-s-b border flex-1" v-model="item.content" placeholder="请输入评价内容" /></view>
				<view class="upload-list d-s-c">
					<view class="item" v-for="(imgs, img_num) in item.imageList" :key="img_num" @click="deleteImg(index, img_num)">
						<image :src="imgs.filePath" mode="aspectFit"></image>
					</view>
					<view class="item upload-btn d-c-c d-c" @click="openUpload(index)" v-if="item.imageList.length < 9">
						<text class="icon iconfont icon-xiangji"></text>
						<text class="gray9">上传图片</text>
					</view>
				</view>
			</view>
			<view class="foot-btns"><button form-type="submit" class="btn-red">确认提交</button></view>
		</form>

		<!--上传图片-->
		<Upload v-if="isUpload" @getImgs="getImgsFunc"></Upload>
	</view>
</template>

<script>
import Upload from '@/components/upload/upload.vue';
export default {
	components: {
		Upload
	},
	data() {
		return {
			/*是否加载完成*/
			loadding: true,
			orderId: '',
			/*页面数据*/
			tableData: [],
			score: 10,
			/*是否打开上传图片*/
			isUpload: false,
			imageId: [],
			params: [{}],
			img: '/static/temp/photo.jpg',
			service: [],
			logistics: [],
			describe: [],
			index: ''
		};
	},
	onLoad(e) {
		this.orderId = e.orderId;
	},
	mounted() {
		uni.showLoading({
			title: '加载中'
		});
		/*获取页面数据*/
		this.getData();
	},
	methods: {
		getData() {
			let self = this;
			self._get(
				'user/comment/toOrder',
				{
					orderId: self.orderId
				},
				function(res) {
					self.tableData = res.data;
					console.log(self.tableData);
					self.tableData.forEach((item, index) => {
						self.$set(self.tableData[index], 'score', 10);
						self.$set(self.tableData[index], 'imageList', []);
						// self.tableData[index].score = 10;
						//self.tableData[index].imageList = [];
						self.service.push([false, false, false, false, false]);
						self.logistics.push([false, false, false, false, false]);
						self.describe.push([false, false, false, false, false]);
					});
					self.loadding = false;
					uni.hideLoading();
				}
			);
		},
		/*选择评价*/
		gradeFunc(item, e, index) {
			item.score = e
      this.tableData[index] = item;
			console.log(this.tableData);
		},
		/* 物流评分 */
		chooseLog(n, m) {
			let self = this;
			self.tableData[m].express_score = 0;
			this.logistics[m].forEach((item, index) => {
				if (index <= n) {
					this.logistics[m].splice(index, 1, true);
					self.tableData[m].express_score++;
				} else {
					this.logistics[m].splice(index, 1, false);
				}
			});
		},
		/* 服务评分 */
		chooseServ(n, m) {
			let self = this;
			self.tableData[m].server_score = 0;
			this.service[m].forEach((item, index) => {
				if (index <= n) {
					this.service[m].splice(index, 1, true);
					self.tableData[m].server_score++;
				} else {
					this.service[m].splice(index, 1, false);
				}
			});
			console.log(self.tableData);
		},
		/* 描述评分 */
		choosees(n, m) {
			let self = this;
			self.tableData[m].describe_score = 0;
			this.describe[m].forEach((item, index) => {
				if (index <= n) {
					this.describe[m].splice(index, 1, true);
					self.tableData[m].describe_score++;
				} else {
					this.describe[m].splice(index, 1, false);
				}
			});
			console.log(self.tableData);
		},

		/*提交*/
		formSubmit: function(e) {
			let self = this;
			let formData = [];
			this.tableData.forEach((item, index) => {
				formData[index] = {
					orderProductId: item.orderProductId,
					productId: item.productId,
					score: item.score,
					imageList: item.imageList,
					expressScore: item.express_score,
					serverScore: item.server_score,
					describeScore: item.describe_score,
					orderId: item.orderId,
					userId: item.userId,
					content: ''
				};
				if (item.content) {
					formData[index].content = item.content;
				}
			});
			self._postBody('user/comment/order', { orderId: self.orderId, params:formData }, function(res) {
				self.showSuccess('评价成功！', function() {
					self.gotoPage('/pages/order/myorder', 'redirect');
				});
			});
		},

		/*打开上传图片*/
		openUpload(index) {
			this.index = index;
			this.isUpload = true;
		},

		/*获取上传的图片*/
		getImgsFunc(e) {
			let self = this;
			if (e && typeof e != 'undefined') {
				let index = self.index;
				self.tableData[index].imageList = self.tableData[index].imageList.concat(e);
			}
			self.isUpload = false;
		},

		/*点击图片删除*/
		deleteImg(i, n) {
			this.tableData[i].imageList.splice(n, 1);
		}
	}
};
</script>

<style>
.evaluate {
	/* background: #eeeeee; */
}
.evaluate-item {
	margin-bottom: 20rpx;
	background: #ffffff;
	border-bottom: 1px solid #dddddd;
}
.product .cover,
.product .cover image {
	width: 160rpx;
	height: 160rpx;
}
.evaluate .grade .item .iconfont {
	width: 60rpx;
	height: 60rpx;
	line-height: 60rpx;
	border-radius: 50%;
	font-size: 40rpx;
	color: #ffffff;
	text-align: center;
}
.evaluate .grade .item {
	height: 60rpx;
	padding-right: 20rpx;
	line-height: 60rpx;
	border-radius: 30rpx;
	transition: background-color 0.4s;
}
.grade .flex-1:nth-child(1) .iconfont {
	background: #f42222;
}
.grade .flex-1:nth-child(2) .iconfont {
	background: #f2b509;
}
.grade .flex-1:nth-child(3) .iconfont {
	background: #999999;
}
.grade .flex-1.active:nth-child(1) .item {
	background: #f42222;
	color: #ffffff;
}
.grade .flex-1.active:nth-child(2) .item {
	background: #f2b509;
	color: #ffffff;
}
.grade .flex-1.active:nth-child(3) .item {
	background: #999999;
	color: #ffffff;
}
.icon-start {
	color: #f5a623;
}
.evalu-value {
	display: flex;
	margin-bottom: 30rpx;
}
.eval {
	display: flex;
	justify-content: space-around;
	align-items: center;
}
.evalu {
	display: flex;
	align-items: baseline;
	flex-direction: column;
}
</style>
