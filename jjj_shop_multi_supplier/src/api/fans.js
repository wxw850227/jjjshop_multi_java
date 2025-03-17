import request from '@/utils/request'

let FansApi = {
    /*粉丝列表*/
    fansList(data, errorback) {
      return request._postBody('/supplier/operate/fans/index', data, errorback);
    },
}

export default FansApi;
