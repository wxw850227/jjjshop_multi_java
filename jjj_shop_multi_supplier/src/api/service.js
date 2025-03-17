import request from '@/utils/request'

let ServiceApi = {

  /*聊天列表*/
  servicelist(data, errorback) {
    return request._post('/supplier/chat.chat/index', data, errorback);
  },
  /*聊天列表*/
  recordlist(data, errorback) {
    return request._post('/supplier/chat.chat/record', data, errorback);
  },
  getinfo(data, errorback) {
    return request._post('/supplier/chat.chat/getinfo', data, errorback);
  },
  /*聊天列表*/
  chatlist(data, errorback) {
    return request._post('/supplier/chat.chat/list', data, errorback);
  },
  bindUid(data, errorback) {
    return request._post('/supplier/chat.chat/bindClient', data, errorback);
  },
}

export default ServiceApi;
