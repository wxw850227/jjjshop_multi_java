import request from '@/utils/request'

let UserApi = {

    /*用户登录*/
    login(data, errorback) {
        return request._post('/supplier/passport/login', data, errorback);
    },
    /*退出登录*/
    loginOut(data, errorback) {
        return request._post('/supplier/passport/logout', data, errorback);
    },
    /*用户列表*/
    userlist(data, errorback) {
        return request._post('/shop/user.user/index', data, errorback);
    },
    /*修改密码*/
    EditPass(data, errorback) {
        return request._post('/supplier/passport/editPass', data, errorback);
    },
}

export default UserApi;
