package net.jjjshop.shop.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierApply;
import net.jjjshop.common.entity.supplier.SupplierCategory;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.mapper.supplier.SupplierApplyMapper;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.shiro.util.SaltUtil;
import net.jjjshop.framework.util.PasswordUtil;
import net.jjjshop.shop.param.supplier.SupplierApplyPageParam;
import net.jjjshop.shop.param.supplier.SupplierApplyParam;
import net.jjjshop.shop.service.supplier.SupplierApplyService;
import net.jjjshop.shop.service.supplier.SupplierCategoryService;
import net.jjjshop.shop.service.supplier.SupplierService;
import net.jjjshop.shop.service.supplier.SupplierUserService;
import net.jjjshop.shop.service.user.UserService;
import net.jjjshop.shop.vo.supplier.SupplierApplyVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 商户申请表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierApplyServiceImpl extends BaseServiceImpl<SupplierApplyMapper, SupplierApply> implements SupplierApplyService {

    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private SupplierCategoryService supplierCategoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierUserService supplierUserService;

    /**
     * 获取列表数据
     */
    public Paging<SupplierApplyVo> getList(SupplierApplyPageParam supplierApplyPageParam) {
        Page<SupplierApply> page = new PageInfo<>(supplierApplyPageParam);
        LambdaQueryWrapper<SupplierApply> wrapper = new LambdaQueryWrapper<>();
        if(supplierApplyPageParam.getStatus() != null && supplierApplyPageParam.getStatus()>0){
            wrapper.eq(SupplierApply::getStatus, supplierApplyPageParam.getStatus());
        }
        if(StringUtils.isNotEmpty(supplierApplyPageParam.getStoreName())) {
            wrapper.like(SupplierApply::getStoreName, supplierApplyPageParam.getStoreName());
        }
        wrapper.orderByDesc(SupplierApply::getCreateTime);
        //查询列表数据
        IPage<SupplierApply> iPage = this.page(page, wrapper);
        IPage<SupplierApplyVo> result = iPage.convert(item -> {
            SupplierApplyVo vo = new SupplierApplyVo();
            BeanUtils.copyProperties(item, vo);
            vo.setBusinessImage(uploadFileUtils.getFilePath(vo.getBusinessId()));
            vo.setCategoryName(supplierCategoryService.getById(vo.getCategoryId()).getName());
            vo.setNickName(userService.getById(vo.getUserId()).getNickname());
            return vo;
        });
        return new Paging<>(result);
    }

    /**
     * 获取详情数据
     */
    public SupplierApplyVo detail(Integer supplierApplyId) {
        SupplierApply apply = this.getById(supplierApplyId);
        SupplierApplyVo vo = new SupplierApplyVo();
        BeanUtils.copyProperties(apply, vo);
        vo.setBusinessImage(uploadFileUtils.getFilePath(vo.getBusinessId()));
        vo.setCategoryName(supplierCategoryService.getById(vo.getCategoryId()).getName());
        vo.setNickName(userService.getById(vo.getUserId()).getNickname());
        return vo;
    }


    /**
     * 审核
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean audit(SupplierApplyParam supplierApplyParam) {
        SupplierApply apply = this.getById(supplierApplyParam.getSupplierApplyId());
        User user = userService.getById(apply.getUserId());
        if(supplierApplyParam.getStatus() == 2) {
            if(StringUtils.isNotEmpty(supplierApplyParam.getContent())){
                throw new BusinessException("备注不能为空");
            }
            user.setUserType(1);
            userService.updateById(user);
            apply.setStatus(2);
            this.updateById(apply);
        }else if(supplierApplyParam.getStatus() == 1) {
            //用户为供应商
            user.setUserType(2);
            userService.updateById(user);
            apply.setStatus(1);
            this.updateById(apply);

            Supplier supplier = new Supplier();
            SupplierUser supplierUser = new SupplierUser();

            //记录供应商
            supplier.setName(apply.getStoreName());
            supplier.setUserId(apply.getUserId());
            supplier.setRealName(apply.getUserName());
            supplier.setLinkName(apply.getUserName());
            supplier.setLinkPhone(apply.getMobile());
            supplier.setBusinessId(apply.getBusinessId());
            supplier.setCategoryId(apply.getCategoryId());

            SupplierCategory category = supplierCategoryService.getById(apply.getCategoryId());
            if(category.getDepositMoney().compareTo(BigDecimal.ZERO)>0) {
                supplier.setStatus(20);
            }else {
                supplier.setStatus(0);
            }
            supplierService.save(supplier);
            //记录供应商用户
            supplierUser.setUserName(apply.getUserName());
            supplierUser.setUserId(supplier.getUserId());
            // 盐值
            String salt = SaltUtil.generateSalt();
            supplierUser.setSalt(salt);
            // 密码加密
            supplierUser.setPassword(PasswordUtil.encrypt(apply.getPassword(), salt));
            supplierUser.setRealName(apply.getUserName());
            supplierUser.setShopSupplierId(supplier.getShopSupplierId());
            supplierUser.setIsSuper(1);
            supplierUserService.save(supplierUser);
        }
        //todo 发送短信给用户

//            $smsConfig = SettingModel::getItem('sms', self::$app_id);
//            $send_template_code = $smsConfig['engine']['aliyun'][$template_code];
//            $SmsDriver = new SmsDriver($smsConfig);
//            $SmsDriver->sendSms($this['mobile'], $send_template_code, '');
        return true;
    }

}
