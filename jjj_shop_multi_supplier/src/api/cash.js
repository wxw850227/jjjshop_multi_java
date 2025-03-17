import request from '@/utils/request'

let SupplierApi = {

    /*首页概况*/
    index(data, errorback) {
        return request._postBody('/supplier/cash/cash/index', data, errorback);
    },
    /*获取用户提现账户信息*/
    getAccount(data, errorback) {
        return request._get('/supplier/cash/cash/account', data, errorback);
    },
    /*保存用户提现账户信息*/
    setAccount(data, errorback) {
        return request._postBody('/supplier/cash/cash/account', data, errorback);
    },
    /*提现记录*/
    lists(data, errorback) {
        return request._postBody('/supplier/cash/cash/lists', data, errorback);
    },
    /*申请提现*/
    apply(data, errorback) {
        return request._postBody('/supplier/cash/cash/apply', data, errorback);
    },
    /* 店铺结算*/
    settled(data, errorback) {
        return request._postBody('/supplier/cash/settled/index', data, errorback);
    },
    /* 店铺结算详情*/
    detail(data, errorback) {
        return request._post('/supplier/cash/settled/detail', data, errorback);
    },
    /*退保证金*/
    refund(data, errorback) {
        return request._post('/supplier/supplier/supplier/refund', data, errorback);
    },
}

export default SupplierApi;
