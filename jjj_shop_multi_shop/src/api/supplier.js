import request from '@/utils/request'

let SupplierApi = {
  /*供应商列表*/
  supplierList(data, errorback) {
    return request._postBody('/shop/supplier/supplier/index', data, errorback);
  },
  /*添加供应商*/
  toaddSupplier(data, errorback) {
    return request._post('/shop/supplier/supplier/toAdd', data, errorback);
  },
  /*添加供应商*/
  addSupplier(data, errorback) {
    return request._postBody('/shop/supplier/supplier/add', data, errorback);
  },
  /*供应商编辑*/
  toEditSupplier(data, errorback) {
    return request._post('/shop/supplier/supplier/toEdit', data, errorback);
  },
  /*供应商编辑*/
  editSupplier(data, errorback) {
    return request._postBody('/shop/supplier/supplier/edit', data, errorback);
  },
  /*删除供应商*/
  deleteSupplier(data, errorback) {
    return request._post('/shop/supplier/supplier/delete', data, errorback);
  },
  /*提现记录*/
  cashList(data, errorback) {
    return request._postBody('/shop/supplier/cash/index', data, errorback);
  },
  /*提现审核*/
  cashSubmit(data, errorback) {
    return request._postBody('/shop/supplier/cash/submit', data, errorback);
  },
  /*提现确认打款*/
  cashMoney(data, errorback) {
    return request._post('/shop/supplier/cash/money', data, errorback);
  },
  /*供应商待审核列表*/
  supplierPendList(data, errorback) {
    return request._postBody('/shop/supplier/supplier/apply', data, errorback);
  },
  /*获取主营分类*/
  Category(data, errorback) {
    return request._post('/shop/supplier/category/index', data, errorback);
  },
    /*添加分类*/
    addCategory(data, errorback) {
    return request._postBody('/shop/supplier/category/add', data, errorback);
  },
    /*编辑分类*/
  editCategory(data, errorback) {
    return request._postBody('/shop/supplier/category/edit', data, errorback);
  },
    /*删除分类*/
    deleteCategory(data, errorback) {
    return request._post('/shop/supplier/category/delete', data, errorback);
  },
  /*服务保障申请列表*/
    securityList(data, errorback) {
    return request._postBody('/shop/supplier/supplier/security', data, errorback);
  },
  /*服务保障操作*/
    securityVerify(data, errorback) {
    return request._postBody('/shop/supplier/supplier/verify', data, errorback);
  },
    /*获取广告分类*/
    SecurityList(data, errorback) {
    return request._postBody('/shop/supplier/security/index', data, errorback);
  },
    /*添加分类*/
    addSecurity(data, errorback) {
    return request._postBody('/shop/supplier/security/add', data, errorback);
  },
    /*编辑分类*/
    editSecurity(data, errorback) {
    return request._postBody('/shop/supplier/security/edit', data, errorback);
  },
    /*删除分类*/
    deleteSecurity(data, errorback) {
    return request._post('/shop/supplier/security/delete', data, errorback);
  },
  /*开启禁止*/
    supplierRecycle(data, errorback) {
    return request._post('/shop/supplier/supplier/recycle', data, errorback);
  },
}
export default SupplierApi;
