import request from '@/utils/request'

let ShopApi = {

  /*服务保障*/
  security(data, errorback) {
    return request._post('/supplier/shop/security/index', data, errorback);
  },
  /*申请服务*/
  apply(data, errorback) {
    return request._postBody('/supplier/shop/security/apply', data, errorback);
  },
  /*退出申请*/
  quit(data, errorback) {
    return request._postBody('/supplier/shop/security/quit', data, errorback);
  },
}

export default ShopApi;
