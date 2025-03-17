import request from '@/utils/request'

let CashApi = {
    /*首页概况*/
    index(data, errorback) {
        return request._post('/shop/cash/cash/index', data, errorback);
    },
    /*首页概况*/
    settled(data, errorback) {
        return request._post('/shop/cash/settled/index', data, errorback);
    },
    getSettledByDate(data, errorback) {
        return request._post('/shop/cash/settled/data', data, errorback);
    },
    /*订单结算*/
    order(data, errorback) {
        return request._post('/shop/cash/order/index', data, errorback);
    },
    /*订单结算详情*/
    orderDetail(data, errorback) {
        return request._post('/shop/cash/order/detail', data, errorback);
    },
}

export default CashApi;
