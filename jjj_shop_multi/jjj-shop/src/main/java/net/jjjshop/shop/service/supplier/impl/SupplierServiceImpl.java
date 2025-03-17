package net.jjjshop.shop.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.mapper.supplier.SupplierMapper;
import net.jjjshop.common.util.SupplierUserUtils;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.shiro.util.SaltUtil;
import net.jjjshop.framework.util.PasswordUtil;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.supplier.SupplierPageParam;
import net.jjjshop.shop.param.supplier.SupplierParam;
import net.jjjshop.shop.service.product.ProductService;
import net.jjjshop.shop.service.supplier.SupplierService;
import net.jjjshop.shop.service.supplier.SupplierUserService;
import net.jjjshop.shop.service.user.UserService;
import net.jjjshop.shop.vo.supplier.SupplierVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 供应商表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
@Slf4j
@Service
public class SupplierServiceImpl extends BaseServiceImpl<SupplierMapper, Supplier> implements SupplierService {
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private SupplierUserService supplierUserService;
    @Autowired
    private SupplierUserUtils supplierUserUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    /**
     * 商户分页列表
     * @param supplierPageParam
     * @return
     */
    public Paging<SupplierVo> getList(SupplierPageParam supplierPageParam) {
        Page<Supplier> page = new PageInfo<>(supplierPageParam);
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(supplierPageParam.getSearch())) {
            wrapper.like(Supplier::getName, supplierPageParam.getSearch());
        }
        wrapper.eq(Supplier::getIsDelete, 0);
        wrapper.orderByDesc(Supplier::getCreateTime);
        IPage<Supplier> iPage = this.page(page, wrapper);
        IPage<SupplierVo> result = iPage.convert(item -> {
            SupplierVo vo = new SupplierVo();
            BeanUtils.copyProperties(item, vo);
            vo.setLogoFilePath(uploadFileUtils.getFilePath(vo.getLogoId()));
            vo.setBusinessFilePath(uploadFileUtils.getFilePath(vo.getBusinessId()));
            SupplierUser user = supplierUserService.getOne(new LambdaQueryWrapper<SupplierUser>()
                    .eq(SupplierUser::getUserId, vo.getUserId())
                    .eq(SupplierUser::getShopSupplierId, vo.getShopSupplierId())
                    .last("limit 1"));
            vo.setUserName(user.getUserName());
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 添加
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(SupplierParam supplierParam) {
        if (!supplierUserUtils.checkExist(supplierParam.getUserName())) {
            throw new BusinessException("用户名已存在");
        }
        //用户是否已经绑定
        User user = null;
        if (supplierParam.getUserId() != null && supplierParam.getUserId() > 0) {
            user = userService.getById(supplierParam.getUserId());
            if (user != null && user.getUserType() != 1) {
                throw new BusinessException("该用户已经绑定供应商，或绑定用户正在审核");
            }
        }
        //添加供应商
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierParam, supplier);
        Integer appId = ShopLoginUtil.getAppId();
        supplier.setAppId(appId);
        this.save(supplier);
        //添加登陆用户
        SupplierUser supplierUser = new SupplierUser();
        supplierUser.setUserId(supplier.getUserId());
        supplierUser.setUserName(supplierParam.getUserName());
        // 盐值
        String salt = SaltUtil.generateSalt();
        supplierUser.setSalt(salt);
        // 密码加密
        supplierUser.setPassword(PasswordUtil.encrypt(supplierParam.getPassword(), salt));
        supplierUser.setRealName(supplierParam.getUserName());
        supplierUser.setShopSupplierId(supplier.getShopSupplierId());
        supplierUser.setIsSuper(1);
        supplierUser.setAppId(appId);
        supplierUserService.save(supplierUser);
        //后台添加的直接算审核通过
        if (user != null) {
            user.setUserType(2);
            userService.updateById(user);
        }
        return true;
    }

    /**
     * 获取编辑参数
     */
    public SupplierVo toEdit(Integer id) {
        Supplier supplier = this.getById(id);
        SupplierVo vo = new SupplierVo();
        BeanUtils.copyProperties(supplier, vo);
        vo.setLogoFilePath(uploadFileUtils.getFilePath(vo.getLogoId()));
        vo.setBusinessFilePath(uploadFileUtils.getFilePath(vo.getBusinessId()));
        SupplierUser user = supplierUserService.getOne(new LambdaQueryWrapper<SupplierUser>()
                .eq(SupplierUser::getUserId, vo.getUserId())
                .eq(SupplierUser::getIsSuper, 1)
                .eq(SupplierUser::getShopSupplierId, vo.getShopSupplierId()));
        vo.setUserName(user.getUserName());
        if(user.getUserId() != null && user.getUserId() > 0) {
            vo.setAvatarurl(userService.getById(user.getUserId()).getAvatarurl());
        }
        return vo;
    }

    /**
     * 编辑供应商
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(SupplierParam supplierParam) {
        SupplierUser superUser = supplierUserService.getOne(new LambdaQueryWrapper<SupplierUser>()
                .eq(SupplierUser::getShopSupplierId, supplierParam.getShopSupplierId())
                .eq(SupplierUser::getIsSuper, 1));
        Integer oldUserId = 0;
        if (superUser != null) {
            oldUserId = superUser.getUserId();
        }
        //修改了用户名,验证唯一性
        if (superUser != null && !superUser.getUserName().equals(supplierParam.getUserName()) ){
            if(!supplierUserUtils.checkExist(supplierParam.getUserName()) ){
                throw new BusinessException("用户名已存在");
            }
        }
        //用户是否已绑定
        User user = null;
        Boolean userChange = false;
        if (superUser != null
                && supplierParam.getUserId() > 0
                && supplierParam.getUserId().intValue() != superUser.getUserId().intValue()) {
            int count = this.count(new LambdaQueryWrapper<Supplier>().eq(Supplier::getUserId, supplierParam.getUserId()).eq(Supplier::getIsDelete,0));
            user = userService.getById(supplierParam.getUserId());
            if ((user != null && user.getUserType() != 1) || count > 0) {
                throw new BusinessException("该用户已经绑定供应商，或绑定用户正在审核");
            }
            userChange = true;
        }
        //修改供应商
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierParam, supplier);
        this.updateById(supplier);
        // 修改登录用户
        if(superUser != null){
            superUser.setUserId(supplierParam.getUserId());
            superUser.setUserName(supplierParam.getUserName());
            if (StringUtils.isNotEmpty(supplierParam.getPassword())) {
                // 盐值
                String salt = SaltUtil.generateSalt();
                superUser.setSalt(salt);
                // 密码加密
                superUser.setPassword(PasswordUtil.encrypt(supplierParam.getPassword(), salt));
            }
            supplierUserService.updateById(superUser);
        }
        if (userChange) {
            user.setUserType(2);
            if (oldUserId != null && oldUserId > 0) {
                userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, oldUserId).set(User::getUserType, 1));
            }
        }
        return true;
    }

    /**
     * 软删除
     */
    public Boolean setDelete(Integer id) {
        return this.update(new LambdaUpdateWrapper<Supplier>().eq(Supplier::getShopSupplierId,id).set(Supplier::getIsDelete,1));
    }

    /**
     * 开启禁止
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean setRecycle(Integer isRecycle,Integer id) {
        if(isRecycle == 1) {
            productService.update(new LambdaUpdateWrapper<Product>().eq(Product::getShopSupplierId,id).set(Product::getProductStatus, 20));
        }
        return this.update(new LambdaUpdateWrapper<Supplier>().eq(Supplier::getShopSupplierId,id).set(Supplier::getIsRecycle,isRecycle));
    }

    /**
     * 提现驳回：解冻资金
     */
    public Boolean backFreezeMoney(Integer shopSupplierId, BigDecimal money){
        Supplier supplier = this.getById(shopSupplierId);
        supplier.setMoney(supplier.getMoney().add(money));
        supplier.setFreezeMoney(supplier.getFreezeMoney().subtract(money));
        return this.updateById(supplier);
    }

    /**
     * 提现打款成功：累积提现金额
     */
    public Boolean totalMoney(Integer shopSupplierId, BigDecimal money) {
        Supplier supplier = this.getById(shopSupplierId);
        supplier.setFreezeMoney(supplier.getFreezeMoney().subtract(money));
        supplier.setCashMoney(supplier.getCashMoney().add(money));
        return this.updateById(supplier);
    }

    /**
     * 获取平台的总销售额
     */
    public BigDecimal getTotalMoney(String type){
        QueryWrapper<Supplier> wrapper = new QueryWrapper<>();
        Map<String, Object> result = null;
        if("total".equals(type)) {
            wrapper.select("IFNULL(sum(total_money),0) AS totalMoney");
            result = this.getMap(wrapper);
        }else if("money".equals(type)){
            wrapper.select("IFNULL(sum(money),0) AS totalMoney");
            result = this.getMap(wrapper);
        }else if("freezeMoney".equals(type)){
            wrapper.select("IFNULL(sum(freeze_money),0) AS totalMoney");
            result = this.getMap(wrapper);
        }else if("cashMoney".equals(type)){
            wrapper.select("IFNULL(sum(cash_money),0) AS totalMoney");
            result = this.getMap(wrapper);
        }else if("depositMoney".equals(type)) {
            wrapper.select("IFNULL(sum(deposit_money),0) AS totalMoney");
            result = this.getMap(wrapper);
        }
        if(result != null) {
            return (BigDecimal) result.get("totalMoney");
        }else {
            return BigDecimal.ZERO;
        }
    }
}
