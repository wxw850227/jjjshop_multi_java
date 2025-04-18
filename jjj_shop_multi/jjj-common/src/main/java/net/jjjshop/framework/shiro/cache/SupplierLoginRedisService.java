

package net.jjjshop.framework.shiro.cache;


import net.jjjshop.framework.shiro.jwt.JwtToken;
import net.jjjshop.framework.shiro.vo.LoginShopUserRedisVo;
import net.jjjshop.framework.shiro.vo.LoginShopUserVo;
import net.jjjshop.framework.shiro.vo.LoginSupplierUserRedisVo;
import net.jjjshop.framework.shiro.vo.LoginSupplierUserVo;

/**
 * 登录信息Redis缓存操作服务
 **/
public interface SupplierLoginRedisService {

    /**
     * 缓存登录信息
     *
     * @param jwtToken
     * @param loginShopUserVo
     */
    void cacheLoginInfo(JwtToken jwtToken, LoginSupplierUserVo loginSupplierUserVo);


    /**
     * 刷新登录信息
     *
     * @param oldToken
     * @param username
     * @param newJwtToken
     */
    void refreshLoginInfo(String oldToken, String username, JwtToken newJwtToken);

    /**
     * 通过用户名，从缓存中获取登录用户LoginSysUserRedisVo
     *
     * @param username
     * @return
     */
    LoginSupplierUserRedisVo getLoginSupplierUserRedisVo(String username);

    /**
     * 获取登录用户对象
     *
     * @param username
     * @return
     */
    LoginSupplierUserVo getLoginSupplierUserVo(String username);

    /**
     * 通过用户名称获取盐值
     *
     * @param username
     * @return
     */
    String getSalt(String username);

    /**
     * 删除对应用户的Redis缓存
     *
     * @param token
     * @param username
     */
    void deleteLoginInfo(String token, String username);

    /**
     * 判断token在redis中是否存在
     *
     * @param token
     * @return
     */
    boolean exists(String token);

    /**
     * 删除用户所有登录缓存
     *
     * @param username
     */
    void deleteUserAllCache(String username);

}
