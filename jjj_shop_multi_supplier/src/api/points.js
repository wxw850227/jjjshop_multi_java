import request from '@/utils/request'

let PointsApi = {
   /*拼团活动列表*/
   activeList(data, errorback) {
     return request._postBody('/supplier/activity/point/index', data, errorback);
   },
   getProduct(data, errorback){
      return request._get('/supplier/activity/point/toAdd', data, errorback);
   },
   addProduct(data, errorback){
      return request._postBody('/supplier/activity/point/add', data, errorback);
   },
   /*我的活动列表*/
   myList(data, errorback) {
     return request._postBody('/supplier/activity/point/my', data, errorback);
   },
   /*根据主键查询*/
   detailProduct(data, errorback){
      return request._get('/supplier/activity/point/toEdit', data, errorback);
   },
   /*根据主键查询*/
   saveProduct(data, errorback){
      return request._postBody('/supplier/activity/point/edit', data, errorback);
   },
   delProduct(data, errorback){
       return request._post('/supplier/activity/point/delete', data, errorback);
   },
}
export default PointsApi;
