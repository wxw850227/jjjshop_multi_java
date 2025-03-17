package net.jjjshop.shop.service.supplier;

import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.supplier.SupplierPageParam;
import net.jjjshop.shop.param.supplier.SupplierParam;
import net.jjjshop.shop.vo.supplier.SupplierVo;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 供应商表 服务类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
public interface SupplierService extends BaseService<Supplier> {
    /**
     * 商户分页列表
     * @param supplierPageParam
     * @return
     */
    Paging<SupplierVo> getList(SupplierPageParam supplierPageParam);

    /**
     * 添加
     */
    Boolean add(SupplierParam supplierParam);

    /**
     * 获取编辑参数
     */
    SupplierVo toEdit(Integer id);

    /**
     * 编辑供应商
     */
    Boolean edit(SupplierParam supplierParam);

    /**
     * 软删除
     */
    Boolean setDelete(Integer id);

    /**
     * 开启禁止
     */
    Boolean setRecycle(Integer isRecycle,Integer id);

    /**
     * 提现驳回：解冻资金
     */
    Boolean backFreezeMoney(Integer shopSupplierId, BigDecimal money);

    /**
     * 提现打款成功：累积提现金额
     */
    Boolean totalMoney(Integer shopSupplierId, BigDecimal money);


    /**
     * 获取平台的总销售额
     */
    BigDecimal getTotalMoney(String type);
}
