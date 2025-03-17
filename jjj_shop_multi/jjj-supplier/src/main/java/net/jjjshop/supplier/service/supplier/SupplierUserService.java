package net.jjjshop.supplier.service.supplier;

import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.shiro.vo.LoginSupplierUserTokenVo;
import net.jjjshop.supplier.param.supplier.SupplierUserPageParam;
import net.jjjshop.supplier.param.supplier.SupplierUserParam;
import net.jjjshop.supplier.vo.supplier.SupplierUserVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 商家用户记录表 服务类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
public interface SupplierUserService extends BaseService<SupplierUser> {

    /**
     * 供应商登陆
     * @param username
     * @param password
     * @return
     */
    LoginSupplierUserTokenVo login(String username, String password);

    /**
     * 修改密码
     * @param password
     * @return
     */
    Boolean renew(String oldpass, String password, String confirmPass);
    /**
     * 退出
     * @param request
     * @throws Exception
     */
    void logout(HttpServletRequest request) throws Exception;

    /**
     * 列表
     * @param params
     * @throws Exception
     */
    Paging<SupplierUserVo> getList(SupplierUserPageParam params);

    /**
     * 新增用户
     * @param supplierUserParam
     * @return
     */
    Boolean add(SupplierUserParam supplierUserParam);

    /**
     * 编辑用户
     * @param supplierUserParam
     * @return
     */
    Boolean edit(SupplierUserParam supplierUserParam);

    /**
     * 删除用户
     * @param supplierUserId
     * @return
     */
    Boolean setDelete(Integer supplierUserId);
}
