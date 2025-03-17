package net.jjjshop.supplier.service.supplier.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierAccess;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.entity.supplier.SupplierUserRole;
import net.jjjshop.common.mapper.supplier.SupplierUserRoleMapper;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.mapper.supplier.SupplierUserMapper;
import net.jjjshop.common.service.supplier.SupplierAccessService;
import net.jjjshop.common.service.supplier.SupplierUserRoleService;
import net.jjjshop.config.constant.CommonConstant;
import net.jjjshop.config.constant.CommonRedisKey;
import net.jjjshop.config.properties.JwtProperties;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.shiro.cache.SupplierLoginRedisService;
import net.jjjshop.framework.shiro.jwt.JwtToken;
import net.jjjshop.framework.shiro.util.JwtTokenUtil;
import net.jjjshop.framework.shiro.util.JwtUtil;
import net.jjjshop.framework.shiro.util.SaltUtil;
import net.jjjshop.framework.shiro.vo.*;
import net.jjjshop.framework.util.PasswordUtil;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.supplier.SupplierUserPageParam;
import net.jjjshop.supplier.param.supplier.SupplierUserParam;
import net.jjjshop.supplier.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.supplier.vo.supplier.SupplierUserVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商家用户记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
@Slf4j
@Service
public class SupplierUserServiceImpl extends BaseServiceImpl<SupplierUserMapper, SupplierUser> implements SupplierUserService {
    @Autowired
    private SupplierUserMapper supplierUserMapper;
    @Autowired
    private SupplierUserRoleMapper supplierUserRoleMapper;
    @Lazy
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Lazy
    @Autowired
    private JwtProperties jwtProperties;
    @Lazy
    @Autowired
    private RedisTemplate redisTemplate;
    @Lazy
    @Autowired
    private SupplierLoginRedisService supplierLoginRedisService;
    @Autowired
    private SupplierAccessService supplierAccessService;
    @Autowired
    private SupplierUserRoleService supplierUserRoleService;


    /**
     * 供应商登陆
     * @param username
     * @param password
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    public LoginSupplierUserTokenVo login(String username, String password){
        SupplierUser supplierUser = this.getOne(new LambdaQueryWrapper<SupplierUser>()
                .eq(SupplierUser::getUserName, username)
                .comment(CommonConstant.NOT_WITH_App_Id));
        if(supplierUser == null){
            throw new AuthenticationException("用户名或密码错误");
        }
        String encryptPassword = PasswordUtil.encrypt(password, supplierUser.getSalt());
        if (!encryptPassword.equals(supplierUser.getPassword())) {
            throw new AuthenticationException("用户名或密码错误");
        }
        // 将系统用户对象转换成登录用户对象
        LoginSupplierUserVo loginSupplierUserVo = new LoginSupplierUserVo();
        BeanUtils.copyProperties(supplierUser, loginSupplierUserVo);
        // 获取用户权限集合
        // 1.如果是超管，则有所有权限
        List<SupplierAccess> accessList = null;
        if(supplierUser.getIsSuper() == 1){
            accessList = supplierAccessService.list(new LambdaQueryWrapper<SupplierAccess>()
                    .eq(SupplierAccess::getIsShow, true)
                    .orderByAsc(SupplierAccess::getSort).orderByAsc(SupplierAccess::getCreateTime)
                    .comment(CommonConstant.NOT_WITH_App_Id));
        }else{
            accessList = supplierUserMapper.getAccessByUserId(supplierUser.getSupplierUserId());
            if (CollectionUtils.isEmpty(accessList)) {
                throw new AuthenticationException("权限列表不能为空");
            }
        }
        loginSupplierUserVo.setPermissions(this.transPermissions(accessList));
        loginSupplierUserVo.setMenus(this.transMenus(accessList));
        // 获取数据库中保存的盐值
        String newSalt = SaltUtil.getSalt(supplierUser.getSalt(), jwtProperties);

        // 生成token字符串并返回
        Long expireSecond = jwtProperties.getExpireSecond();
        String token = JwtUtil.generateToken(username, newSalt, Duration.ofSeconds(expireSecond));
        log.debug("token:{}", token);

        // 创建AuthenticationToken
        JwtToken jwtToken = JwtToken.build(token, username, newSalt, expireSecond);

        boolean enableShiro = springBootJjjProperties.getShiro().isEnable();
        if (enableShiro) {
            // 从SecurityUtils里边创建一个 subject
            Subject subject = SecurityUtils.getSubject();
            // 执行认证登录
            subject.login(jwtToken);
        } else {
            log.warn("未启用Shiro");
        }

        // 缓存登录信息到Redis
        supplierLoginRedisService.cacheLoginInfo(jwtToken, loginSupplierUserVo);
        log.debug("登录成功,username:{}", username);

        // 缓存登录信息到redis
        String tokenSha256 = DigestUtils.sha256Hex(token);
        redisTemplate.opsForValue().set(tokenSha256, loginSupplierUserVo, 1, TimeUnit.DAYS);

        // 返回token和登录用户信息对象
        LoginSupplierUserTokenVo loginSupplierUserTokenVo = new LoginSupplierUserTokenVo();
        loginSupplierUserTokenVo.setToken(token);
        loginSupplierUserTokenVo.setLoginSupplierUserVo(loginSupplierUserVo);
        return loginSupplierUserTokenVo;
    }

    /**
     * 后端权限集合
     * @param accessList
     * @return
     */
    private Set<String> transPermissions(List<SupplierAccess> accessList){
        Set<String> set = new HashSet<String>();
        accessList.forEach(item -> {
            set.add(item.getPath());
        });
        return set;
    }

    /**
     * 前端权限菜单集合
     * @param accessList
     * @return
     */
    private List<LoginSupplierAccessVo> transMenus(List<SupplierAccess> accessList){
        // 转成vo
        List<LoginSupplierAccessVo> voList = accessList.stream().map(e -> {
            LoginSupplierAccessVo supplierAccessVo = new LoginSupplierAccessVo();
            BeanUtils.copyProperties(e, supplierAccessVo);
            return supplierAccessVo;
        }).collect(Collectors.toList());
        // 遍历成树形结构
        List<LoginSupplierAccessVo> collect = voList.stream()
                // 2. 找出所有顶级（规定 0 为顶级）
                .filter(o -> StrUtil.equals("0", String.valueOf(o.getParentId())))
                // 3.给当前父级的 childList 设置子
                .peek(o -> o.setChildren(getChildList(o, voList)))
                // 4.收集
                .collect(Collectors.toList());
        return collect;
    }

    // 根据当前父类 找出子类， 并通过递归找出子类的子类
    private List<LoginSupplierAccessVo> getChildList(SupplierAccess bean, List<LoginSupplierAccessVo> voList) {
        return voList.stream()
                //筛选出父节点id == parentId 的所有对象 => list
                .filter(o -> StrUtil.equals(String.valueOf(bean.getAccessId()), String.valueOf(o.getParentId())))
                .peek(o -> o.setChildren(getChildList(o, voList)))
                .collect(Collectors.toList());
    }

    /**
     * 修改密码
     * @param password
     * @return
     */
    public Boolean renew(String oldpass, String password, String confirmPass){
        // 当前登录用户
        SupplierUser supplierUser = this.getById(SupplierLoginUtil.getUserId());
        if(!password.equals(confirmPass)){
            throw new BusinessException("两次密码不相同");
        }
        String encryptOldPassword = PasswordUtil.encrypt(oldpass, supplierUser.getSalt());
        if(!encryptOldPassword.equals(supplierUser.getPassword())){
            throw new BusinessException("原密码不正确");
        }
        String encryptPassword = PasswordUtil.encrypt(password, supplierUser.getSalt());
        SupplierUser updateBean = new SupplierUser();
        updateBean.setSupplierUserId(supplierUser.getSupplierUserId());
        updateBean.setPassword(encryptPassword);
        return this.updateById(updateBean);
    }

    @Override
    public void logout(HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        // 获取token
        String token = JwtTokenUtil.getToken(request,"supplier");
        String username = JwtUtil.getUsername(token);
        // 删除Redis缓存信息
        deleteLoginInfo(token, username);
        log.info("登出成功,username:{},token:{}", username, token);
    }

    private void deleteLoginInfo(String token, String username) {
        if (token == null) {
            throw new IllegalArgumentException("token不能为空");
        }
        if (username == null) {
            throw new IllegalArgumentException("username不能为空");
        }
        String tokenMd5 = DigestUtils.md5Hex(token);
        // 1. delete tokenMd5
        redisTemplate.delete(String.format(CommonRedisKey.SUPPLIER_LOGIN_TOKEN, tokenMd5));
        // 2. delete username
        redisTemplate.delete(String.format(CommonRedisKey.SUPPLIER_LOGIN_USER, username));
        // 3. delete salt
        redisTemplate.delete(String.format(CommonRedisKey.SUPPLIER_LOGIN_SALT, username));
        // 4. delete user token
        redisTemplate.delete(String.format(CommonRedisKey.SUPPLIER_LOGIN_USER_TOKEN, username, tokenMd5));
    }


    /**
     * 列表
     * @param params
     * @throws Exception
     */
    public Paging<SupplierUserVo> getList(SupplierUserPageParam params){
        Page<SupplierUser> page = new PageInfo<>(params);
        IPage<SupplierUser> iPage = this.page(page, new LambdaQueryWrapper<SupplierUser>()
                .eq(SupplierUser::getShopSupplierId,SupplierLoginUtil.getShopSupplierId())
                .eq(SupplierUser::getIsDelete, false));
        // 最终返回分页对象
        IPage<SupplierUserVo> resultPage = iPage.convert(item -> {
            SupplierUserVo vo = new SupplierUserVo();
            BeanUtil.copyProperties(item, vo);
            // 角色名称
            if(!vo.getIsSuper()){
                vo.setRoleList(supplierUserRoleMapper.getRoleListByUser(vo.getSupplierUserId()));
            }
            return vo;
        });
        return new Paging(resultPage);
    }

    /**
     * 新增用户
     * @param supplierUserParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(SupplierUserParam supplierUserParam){
        //验证账号唯一性
        int count = this.count(new LambdaQueryWrapper<SupplierUser>()
                .eq(SupplierUser::getIsDelete,0)
                .comment(CommonConstant.NOT_WITH_App_Id)
                .eq(SupplierUser::getUserName,supplierUserParam.getUserName()));
        if(count > 0){
            throw new BusinessException("该用户名已存在，请更改后再试");
        }
        SupplierUser user = new SupplierUser();
        user.setUserName(supplierUserParam.getUserName());
        user.setRealName(supplierUserParam.getRealName());
        // 盐值
        String salt = SaltUtil.generateSalt();
        // 密码加密
        user.setSalt(salt);
        user.setPassword(PasswordUtil.encrypt(supplierUserParam.getPassword(), salt));
        user.setShopSupplierId(SupplierLoginUtil.getShopSupplierId());
        this.save(user);
        // 保存角色
        this.saveRole(supplierUserParam, user.getSupplierUserId());
        return true;
    }

    /**
     * 编辑用户
     * @param supplierUserParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(SupplierUserParam supplierUserParam){
        SupplierUser oldUser = this.getById(supplierUserParam.getSupplierUserId());
        //如果修改了账号，则验证账号唯一性
        if(!oldUser.getUserName().equals(supplierUserParam.getUserName())){
            int count = this.count(new LambdaQueryWrapper<SupplierUser>()
                    .eq(SupplierUser::getIsDelete,0)
                    .comment(CommonConstant.NOT_WITH_App_Id)
                    .eq(SupplierUser::getUserName,supplierUserParam.getUserName()));
            if(count > 0){
                throw new BusinessException("该用户名已存在，请更改后再试");
            }
        }

        SupplierUser user = new SupplierUser();
        user.setSupplierUserId(supplierUserParam.getSupplierUserId());
        user.setUserName(supplierUserParam.getUserName());
        user.setRealName(supplierUserParam.getRealName());
        if(StringUtils.isNotEmpty(supplierUserParam.getPassword())){
            // 盐值
            String salt = SaltUtil.generateSalt();
            // 密码加密
            user.setSalt(salt);
            user.setPassword(PasswordUtil.encrypt(supplierUserParam.getPassword(), salt));
        }
        this.updateById(user);
        // 先删除角色
        supplierUserRoleService.remove(new LambdaQueryWrapper<SupplierUserRole>()
                .eq(SupplierUserRole::getSupplierUserId, supplierUserParam.getSupplierUserId()));
        // 保存角色
        this.saveRole(supplierUserParam, supplierUserParam.getSupplierUserId());
        return true;
    }

    private void saveRole(SupplierUserParam supplierUserParam, Integer supplierUserId){
        List<SupplierUserRole> roleList = new ArrayList<>();
        supplierUserParam.getRoleId().forEach(item -> {
            SupplierUserRole role = new SupplierUserRole();
            role.setRoleId(item);
            role.setSupplierUserId(supplierUserId);
            roleList.add(role);
        });
        supplierUserRoleService.saveBatch(roleList);
    }

    /**
     * 删除用户
     * @param supplierUserId
     * @return
     */
    public Boolean setDelete(Integer supplierUserId){
        return this.update(new LambdaUpdateWrapper<SupplierUser>()
                .eq(SupplierUser::getSupplierUserId, supplierUserId).set(SupplierUser::getIsDelete, true));
    }
}
