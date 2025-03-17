import { defineStore } from 'pinia';
import { setStorage, getStorage } from '@/utils/storageData';
import AuthApi from '@/api/auth.js';
import configObj from "@/config"; 
let { strongToken, renderMenu, menu } = configObj;
import { handRouterTable, handMenuData } from '@/utils/router';
export const useUserStore = defineStore('main', {
	state: () => {
		return {
			token: getStorage(strongToken),
			userInfo: getStorage('userInfo'),
			list: {},
			menus: getStorage(menu),
			renderMenus: getStorage(renderMenu),
		};
	},
	getters: {},
	actions: {
		bus_on(name, fn) {
			let self = this;
			self.list[name] = self.list[name] || [];
			self.list[name].push(fn);
		},
		// 发布
		bus_emit(name, data) {
			let self = this;
			if (self.list[name]) {
				self.list[name].forEach((fn) => {
					fn(data);
				});
			}
		},
		// 取消订阅
		bus_off(name) {
			let self = this;
			if (self.list[name]) {
				delete self.list[name];
			}
		},
		/**
		 * @description 登录
		 * @param {*} token
		 */
		async afterLogin(info) {
			this.userInfo = this.userInfo || {};
			const { data: { token, loginSupplierUserVo: { appId, isSuper, shopUserId, userName} } } = info;
			this.token = token;
			const { data } = await AuthApi.getRoleList({});
			let renderMenusList = handMenuData(JSON.parse(JSON.stringify(data)));
			let menusList = handRouterTable(JSON.parse(JSON.stringify(data)));
			setStorage(JSON.stringify(menusList), menu);
			setStorage(JSON.stringify(renderMenusList), renderMenu); 
			this.userInfo.userName = userName;
			this.userInfo.shopUserId = shopUserId;
			this.userInfo.isSuper = isSuper;
			this.userInfo.AppID = appId;
			this.renderMenus = renderMenusList;
			this.menus = menusList;
			setStorage(JSON.stringify(token), strongToken); 
			setStorage(JSON.stringify(this.userInfo),'userInfo');
			await this.getUserInfo();
		},
		/**
		 * @description 获取用户基本信息
		 * @param {*} token
		 */
		async getUserInfo(){
			const { data } = await AuthApi.getUserInfo({});
			this.userInfo.shop_name = data.shop_name;
			this.userInfo.version = data.version;
			setStorage(JSON.stringify(this.userInfo),'userInfo');
		},
		/**
		 * @description 退出登录
		 * @param {*} token
		 */
		afterLogout() {
			sessionStorage.clear();
			// deleteSessionStorage(null);
			// delCookie(strongToken,null);
			this.token = null;
			this.menus = null;
		},
	}
});
export default useUserStore;