import request from '@/utils/request'

let SettingApi = {

  /*打印设置模板变量*/
  printingDetail(data, errorback) {
    return request._get('/supplier/setting/printing/index', data, errorback);
  },
  /*保存打印设置*/
  editPrinting(data, errorback) {
    return request._postBody('/supplier/setting/printing/index', data, errorback);
  },
  /*打印设置模板变量*/
  clearDetail(data, errorback) {
    return request._get('/supplier/setting.clear/index', data, errorback);
  },
  /*退货地址列表*/
  addressList(data, errorback) {
    return request._postBody('/supplier/setting/address/index', data, errorback);
  },
  /*添加退货地址*/
  addAddress(data, errorback) {
    return request._postBody('/supplier/setting/address/add', data, errorback);
  },
  /*退货地址详情*/
  addressDetail(data, errorback) {
    return request._get('/supplier/setting/address/toEdit', data, errorback);
  },
  /*修改退货地址*/
  editAddress(data, errorback) {
    return request._postBody('/supplier/setting/address/edit', data, errorback);
  },
  /*删除退货地址*/
  deleteAddress(data, errorback) {
    return request._post('/supplier/setting/address/delete', data, errorback);
  },
  /*打印机列表*/
  printerList(data, errorback) {
    return request._postBody('/supplier/setting/printer/index', data, errorback);
  },
  /*打印机类型*/
  printerType(data, errorback) {
    return request._get('/supplier/setting/printer/toAdd', data, errorback);
  },
  /*添加打印机*/
  addPrinter(data, errorback) {
    return request._postBody('/supplier/setting/printer/add', data, errorback);
  },
  /*打印机详情*/
  printerDetail(data, errorback) {
    return request._get('/supplier/setting/printer/toEdit', data, errorback);
  },
  /*修改打印机*/
  editPrinter(data, errorback) {
    return request._postBody('/supplier/setting/printer/edit', data, errorback);
  },
  /*删除打印机*/
  deletePrinter(data, errorback) {
    return request._post('/supplier/setting/printer/delete', data, errorback);
  },
  /*运费模板列表*/
  deliveryList(data, errorback) {
    return request._postBody('/supplier/setting/delivery/index', data, errorback);
  },
  /*添加运费模板*/
  addDelivery(data, errorback) {
    return request._postBody('/supplier/setting/delivery/add', data, errorback);
  },
  /*运费模板详情*/
  toAddDelivery(data, errorback) {
    return request._get('/supplier/setting/delivery/toAdd', data, errorback);
  },
  /*修改运费模板*/
  toEditDelivery(data, errorback) {
    return request._get('/supplier/setting/delivery/toEdit', data, errorback);
  },
  /*修改运费模板*/
  editDelivery(data, errorback) {
    return request._postBody('/supplier/setting/delivery/edit', data, errorback);
  },
  /*删除运费模板*/
  deleteDelivery(data, errorback) {
    return request._post('/supplier/setting/delivery/delete', data, errorback);
  },
  /*满额包邮变量*/
  fullfreeDetail(data, errorback) {
    return request._get('/supplier/setting/fullfree/index', data, errorback);
  },
  /*满额包邮设置*/
  editFullfree(data, errorback) {
    return request._postBody('/supplier/setting/fullfree/index', data, errorback);
  },
  /*获取商户信息*/
  getSupplier(data, errorback) {
    return request._get('/supplier/setting/supplier/index', data, errorback);
  },
  /*修改商户信息*/
  setSupplier(data, errorback) {
    return request._post('/supplier/setting/supplier/index', data, errorback);
  },
  /*客服设置*/
  getService(data, errorback) {
    return request._get('/supplier/setting/service/index', data, errorback);
  },
  /*客服设置*/
  setService(data, errorback) {
    return request._postBody('/supplier/setting/service/index', data, errorback);
  },
  /*物流公司列表*/
  expressList(data, errorback) {
    return request._postBody('/supplier/setting/express/index', data, errorback);
  },
}

export default SettingApi;
