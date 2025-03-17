import request from "@/utils/request";

let DataApi = {
  /*用户接口*/
  getUser(data, errorback) {
    return request._postBody("/shop/data/user/lists", data, errorback);
  },
  /*用户接口*/
  getRegion(data, errorback) {
    return request._post('/shop/data/region/lists', data, errorback);
  },
};
export default DataApi;
