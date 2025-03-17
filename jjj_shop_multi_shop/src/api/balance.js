import request from '@/utils/request'

let BalanceApi = {
  /*获取积分列表*/
  getBalanceLog (data, errorback) {
    return request._get('/shop/user.balance/log', data, errorback);
  },
}
export default BalanceApi;
