import request from '@/utils/request'

let IndexApi = {

  /*基础配置*/
  base(data, errorback) {
    return request._post('/supplier/index/base', data, errorback);
  },

  /*首页*/
  getCount(data, errorback) {
    return request._post('/supplier/index/index', data, errorback);
  },



}

export default IndexApi;
