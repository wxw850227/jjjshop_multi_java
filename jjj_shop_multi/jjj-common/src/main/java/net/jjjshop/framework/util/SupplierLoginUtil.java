

package net.jjjshop.framework.util;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.config.constant.CommonRedisKey;
import net.jjjshop.framework.shiro.util.JwtTokenUtil;
import net.jjjshop.framework.shiro.util.JwtUtil;
import net.jjjshop.framework.shiro.vo.LoginSupplierUserRedisVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 获取登录信息工具类
 */
@Slf4j
@Component
public class SupplierLoginUtil {

    private static RedisTemplate redisTemplate;

    public SupplierLoginUtil(RedisTemplate redisTemplate) {
        SupplierLoginUtil.redisTemplate = redisTemplate;
    }


    /**
     * 获取当前登录用户对象
     *
     * @return
     */
    public static LoginSupplierUserRedisVo getLoginSupplierUserRedisVo() {
        // 获取当前登录用户
        String token = JwtTokenUtil.getToken("supplier");
        String username = JwtUtil.getUsername(token);
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return (LoginSupplierUserRedisVo) redisTemplate.opsForValue().get(String.format(CommonRedisKey.SUPPLIER_LOGIN_USER, username));
    }

    /**
     * 获取当前登录用户的ID
     *
     * @return
     */
    public static Integer getUserId() {
        LoginSupplierUserRedisVo loginSupplierUserRedisVo = getLoginSupplierUserRedisVo();
        if (loginSupplierUserRedisVo == null) {
            return null;
        }
        return loginSupplierUserRedisVo.getSupplierUserId();
    }

    /**
     * 获取当前登录用户的供应商ID
     *
     * @return
     */
    public static Integer getShopSupplierId() {
        LoginSupplierUserRedisVo loginSupplierUserRedisVo = getLoginSupplierUserRedisVo();
        if (loginSupplierUserRedisVo == null) {
            return 0;
        }
        return loginSupplierUserRedisVo.getShopSupplierId();
    }

    /**
     * 获取当前登录用户的账号
     *
     * @return
     */
    public static String getUsername() {
        LoginSupplierUserRedisVo loginSupplierUserRedisVo = getLoginSupplierUserRedisVo();
        if (loginSupplierUserRedisVo == null) {
            return null;
        }
        return loginSupplierUserRedisVo.getUserName();
    }

}
