package net.jjjshop.shop.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierAccount;
import net.jjjshop.common.entity.supplier.SupplierCapital;
import net.jjjshop.common.entity.supplier.SupplierCash;
import net.jjjshop.common.enums.OrderPayTypeEnum;
import net.jjjshop.common.mapper.supplier.SupplierCashMapper;
import net.jjjshop.common.service.supplier.SupplierAccountService;
import net.jjjshop.common.service.supplier.SupplierCapitalService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.supplier.SupplierCashPageParam;
import net.jjjshop.shop.param.supplier.SupplierCashParam;
import net.jjjshop.shop.service.supplier.SupplierCashService;
import net.jjjshop.shop.service.supplier.SupplierService;
import net.jjjshop.shop.vo.supplier.SupplierCashVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private SupplierCapitalService supplierCapitalService;

    /**
     * 获取列表数据
     */
    public Paging<SupplierCashVo> getList(SupplierCashPageParam supplierCashPageParam) {
        Page<SupplierCash> page = new PageInfo<>();
        LambdaQueryWrapper<SupplierCash> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(supplierCashPageParam.getSearch())) {
            List<Supplier> list = supplierService.list(new LambdaQueryWrapper<Supplier>()
                    .like(Supplier::getName, supplierCashPageParam.getSearch()));
            if (CollectionUtils.isNotEmpty(list)) {
                List<Integer> supplierIds = list.stream().map(Supplier::getShopSupplierId).collect(Collectors.toList());
                wrapper.in(SupplierCash::getShopSupplierId, supplierIds);
            } else {
                wrapper.eq(SupplierCash::getShopSupplierId, -1);
            }
        }
        if (supplierCashPageParam.getApplyStatus() != null && supplierCashPageParam.getApplyStatus() > -1) {
            wrapper.eq(SupplierCash::getApplyStatus, supplierCashPageParam.getApplyStatus());
        }
        if (supplierCashPageParam.getPayType() != null && supplierCashPageParam.getPayType() > -1) {
            wrapper.eq(SupplierCash::getPayType, supplierCashPageParam.getPayType());
        }
        wrapper.orderByDesc(SupplierCash::getCreateTime);
        //查询数据
        IPage<SupplierCash> iPage = this.page(page, wrapper);
        IPage<SupplierCashVo> result = iPage.convert(item -> {
            SupplierCashVo vo = new SupplierCashVo();
            BeanUtils.copyProperties(item, vo);
            SupplierAccount account = supplierAccountService.getOne(new LambdaQueryWrapper<SupplierAccount>().eq(SupplierAccount::getShopSupplierId, vo.getShopSupplierId()));
            if (account != null) {
                BeanUtils.copyProperties(account, vo);
            }
            vo.setPayTypeText(OrderPayTypeEnum.getName(vo.getPayType()));
            vo.setSupplierName(supplierService.getById(vo.getShopSupplierId()).getName());
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 分销商提现审核
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean submit(SupplierCashParam supplierCashParam) {
        SupplierCash cash = this.getById(supplierCashParam.getId());
        cash.setApplyStatus(supplierCashParam.getApplyStatus());
        if(supplierCashParam.getApplyStatus() == 30) {
            cash.setRejectReason(supplierCashParam.getRejectReason());
        }
        //更新申请记录
        cash.setAuditTime(new Date());
        this.updateById(cash);
        //提现驳回:解冻分销商资金
        if(supplierCashParam.getApplyStatus() == 30) {
            supplierService.backFreezeMoney(cash.getShopSupplierId(), cash.getMoney());
        }
        return true;
    }

    /**
     * 确认已打款
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean money(Integer id){
        SupplierCash cash = this.getById(id);
        //更新申请状态
        cash.setApplyStatus(40);
        cash.setAuditTime(new Date());
        this.updateById(cash);

        SupplierCapital capital = new SupplierCapital();
        capital.setMoney(cash.getMoney());
        capital.setShopSupplierId(cash.getShopSupplierId());
        capital.setFlowType(20);
        capital.setDescription("提现支出:"+capital.getMoney().toString());
        supplierCapitalService.save(capital);

        //更新分销商累计提现佣金
        supplierService.totalMoney(cash.getShopSupplierId(), cash.getMoney());
        return true;
    }
}
