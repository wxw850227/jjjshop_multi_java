import request from '@/utils/request'

let AdApi = {
    /*广告列表*/
    adList(data, errorback) {
      return request._postBody('/supplier/operate/ad/index', data, errorback);
    },
    /*添加广告*/
    addAd(data, errorback) {
      return request._postBody('/supplier/operate/ad/add', data, errorback);
    },
     /*添加广告*/
    toaddAd(data, errorback) {
      return request._get('/supplier/operate/ad/toAdd', data, errorback);
    },
    /*广告详情*/
    adDetail(data, errorback) {
      return request._get('/supplier/operate/ad/detail', data, errorback);
    },
    /*修改广告*/
    editAd(data, errorback) {
      return request._postBody('/supplier/operate/ad/edit', data, errorback);
    },
    /*删除广告*/
    deleteAd(data, errorback) {
      return request._post('/supplier/operate/ad/delete', data, errorback);
    },
}

export default AdApi;
