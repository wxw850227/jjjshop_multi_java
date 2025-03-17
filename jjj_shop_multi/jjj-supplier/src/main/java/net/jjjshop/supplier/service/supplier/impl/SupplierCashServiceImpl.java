package net.jjjshop.supplier.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierAccount;
import net.jjjshop.common.entity.supplier.SupplierCash;
import net.jjjshop.common.enums.OrderPayTypeEnum;
import net.jjjshop.common.mapper.supplier.SupplierCashMapper;
import net.jjjshop.common.service.supplier.SupplierService;
import net.jjjshop.common.util.SupplierUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.supplier.SupplierCashPageParam;
import net.jjjshop.supplier.param.supplier.SupplierCashParam;
import net.jjjshop.supplier.service.supplier.SupplierAccountService;
import net.jjjshop.supplier.service.supplier.SupplierCashService;
import net.jjjshop.supplier.vo.supplier.SupplierCashVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 供应商提现明细表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierCashServiceImpl extends BaseServiceImpl<SupplierCashMapper, SupplierCash> implements SupplierCashService {

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierAccountService supplierAccountService;
    @Autowired
    private SupplierUtils supplierUtils;

    /**
     * 获取列表
     * @param shopSupplierId
     * @param supplierCashPageParam
     * @return
     */
    public Paging<SupplierCashVo> getList(Integer shopSupplierId, SupplierCashPageParam supplierCashPageParam) {
        Page<SupplierCash> page = new PageInfo<>(supplierCashPageParam);
        LambdaQueryWrapper<SupplierCash> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SupplierCash::getShopSupplierId, shopSupplierId);
        wrapper.orderByDesc(SupplierCash::getCreateTime);
        IPage<SupplierCash> iPage = this.page(page, wrapper);
        IPage<SupplierCashVo> result = iPage.convert(item -> {
            SupplierCashVo vo = new SupplierCashVo();
            BeanUtils.copyProperties(item, vo);
            vo.setPayTypeText(OrderPayTypeEnum.getName(vo.getPayType()));
            vo.setApplyStatusText(this.getApplyStatusText(vo.getApplyStatus()));
            return vo;
        });
        return new Paging<>(result);
    }

    private String getApplyStatusText(Integer status) {
        //申请状态 (10待审核 20审核通过 30驳回 40已打款)
        if (status == 10) {
            return "待审核";
        }
        if (status == 20) {
            return "审核通过";
        }
        if (status == 30) {
            return "驳回";
        }
        if (status == 40) {
            return "已打款";
        }
        return "";
    }

    /**
     * 提交申请
     * @param shopSupplierId
     * @param supplierCashParam
     * @return
     */
    public Boolean submit(Integer shopSupplierId, SupplierCashParam supplierCashParam) {
        Supplier supplier = supplierService.getById(shopSupplierId);
        SupplierAccount account = supplierAccountService.getOne(new LambdaQueryWrapper<SupplierAccount>().eq(SupplierAccount::getShopSupplierId, shopSupplierId).last("LIMIT 1"));
        if(account == null) {
            throw new BusinessException("请填写提现账户信息");
        }
        this.validation(supplier,supplierCashParam,account);
        SupplierCash supplierCash = new SupplierCash();
        supplierCash.setShopSupplierId(shopSupplierId);
        supplierCash.setApplyStatus(10);
        BeanUtils.copyProperties(supplierCashParam, supplierCash);
        this.save(supplierCash);
        supplierUtils.freezeMoney(supplier.getShopSupplierId(), supplierCashParam.getMoney());
        return true;
    }

    /**
     * 数据验证
     * @param supplier
     * @param param
     * @param account
     * @return
     */
    private Boolean validation(Supplier supplier, SupplierCashParam param, SupplierAccount account) {
        //最低提现佣金
        if (param.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("提现金额不正确");
        }
        if (supplier.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("没有可提现金额");
        }
        if (param.getMoney().compareTo(supplier.getMoney()) > 0) {
            throw new BusinessException("提现金额不能大于可提现金额");
        }
        if(param.getPayType() == 10) {
            if(StringUtils.isBlank(account.getAlipayName()) || StringUtils.isBlank(account.getAlipayAccount())){
                throw new BusinessException("请补全提现信息");
            }
        }else if (param.getPayType() == 20) {
            if (StringUtils.isBlank(account.getBankName()) ||
                    StringUtils.isBlank(account.getBankAccount()) ||
                    StringUtils.isBlank(account.getBankCard())) {
                throw new BusinessException("请补全提现信息");
            }
        }else {
            throw new BusinessException("提现方式不正确");
        }
        return true;
    }

}
