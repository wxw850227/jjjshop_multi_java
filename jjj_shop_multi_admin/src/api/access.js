import request from '@/utils/request'

let AccessApi = {
  /*菜单列表*/
  accessList(data, errorback) {
    return request._post('/admin/access/index', data, errorback);
  },

  /*添加菜单*/
  addAccess(data, errorback) {
    return request._postBody('/admin/access/add', data, errorback);
  },

  /*编辑菜单*/
  editAccess(data, errorback) {
    return request._postBody('/admin/access/edit', data, errorback);
  },

  /*删除菜单*/
  delAccess(data, errorback) {
    return request._post('/admin/access/delete', data, errorback);
  },

  /*修改状态*/
  status(data, errorback) {
    return request._post('/admin/access/status', data, errorback);
  },


  /*菜单列表*/
  supplieraccessList(data, errorback) {
    return request._post('/admin/supplierAccess/index', data, errorback);
  },

  /*添加菜单*/
  supplieraddAccess(data, errorback) {
    return request._postBody('/admin/supplierAccess/add', data, errorback);
  },

  /*编辑菜单*/
  suppliereditAccess(data, errorback) {
    return request._postBody('/admin/supplierAccess/edit', data, errorback);
  },

  /*删除菜单*/
  supplierdelAccess(data, errorback) {
    return request._post('/admin/supplierAccess/delete', data, errorback);
  },

  /*修改状态*/
  supplierstatus(data, errorback) {
    return request._post('/admin/supplierAccess/status', data, errorback);
  },
}

export default AccessApi;
