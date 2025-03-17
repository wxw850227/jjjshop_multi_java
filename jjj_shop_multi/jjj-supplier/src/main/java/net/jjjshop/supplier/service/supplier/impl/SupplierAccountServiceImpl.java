package net.jjjshop.supplier.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierAccount;
import net.jjjshop.common.mapper.supplier.SupplierAccountMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.supplier.param.supplier.SupplierAccountParam;
import net.jjjshop.supplier.service.supplier.SupplierAccountService;
import net.jjjshop.supplier.vo.supplier.SupplierAccountVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 供应商提现账号表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierAccountServiceImpl extends BaseServiceImpl<SupplierAccountMapper, SupplierAccount> implements SupplierAccountService {

    /**
     * 获取列表数据
     * @param shopSupplierId
     * @return
     */
    public SupplierAccountVo detail(Integer shopSupplierId) {
        SupplierAccount account = this.getOne(new LambdaQueryWrapper<SupplierAccount>().eq(SupplierAccount::getShopSupplierId, shopSupplierId));
        SupplierAccountVo vo = new SupplierAccountVo();
        if(account!=null) {
            BeanUtils.copyProperties(account, vo);
        }
        return vo;
    }

    /**
     * 获取列表数据
     * @param shopSupplierId
     * @param supplierAccountParam
     * @return
     */
    public Boolean add(Integer shopSupplierId, SupplierAccountParam supplierAccountParam) {
        SupplierAccount account = this.getOne(new LambdaQueryWrapper<SupplierAccount>().eq(SupplierAccount::getShopSupplierId, shopSupplierId));
        if (account == null) {
            account = new SupplierAccount();
            BeanUtils.copyProperties(supplierAccountParam, account);
            account.setShopSupplierId(shopSupplierId);
            return this.save(account);
        }
        BeanUtils.copyProperties(supplierAccountParam, account);
        account.setShopSupplierId(shopSupplierId);
        return this.updateById(account);
    }


}
