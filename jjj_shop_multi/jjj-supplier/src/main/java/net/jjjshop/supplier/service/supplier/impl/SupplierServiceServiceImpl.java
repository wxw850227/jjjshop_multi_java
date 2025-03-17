package net.jjjshop.supplier.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.supplier.SupplierService;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.mapper.supplier.SupplierServiceMapper;
import net.jjjshop.common.util.SupplierUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.supplier.param.supplier.SupplierServiceParam;
import net.jjjshop.supplier.service.supplier.SupplierServiceService;
import net.jjjshop.supplier.service.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 供应商客服表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierServiceServiceImpl extends BaseServiceImpl<SupplierServiceMapper, SupplierService> implements SupplierServiceService {

    @Autowired
    private SupplierUtils supplierUtils;
    @Autowired
    private UserService userService;

    /**
     * 保存供应商服务
     * @param shopSupplierId
     * @param serviceParam
     * @return
     */
    public Boolean saveService(Integer shopSupplierId, SupplierServiceParam serviceParam) {
        SupplierService service = this.getOne(new LambdaQueryWrapper<SupplierService>().eq(SupplierService::getShopSupplierId, shopSupplierId));
        if(service != null) {
            serviceParam.setShopSupplierId(service.getShopSupplierId());
        }
        Supplier supplier = supplierUtils.getSupplier(shopSupplierId);
        //更新供应商绑定用户Id
        if(supplier.getUserId() == null && serviceParam.getServiceType() == 20) {
            User user = userService.getById(serviceParam.getUserId());
            if(user == null) {
                throw new BusinessException("绑定用户信息不存在");
            }
            //查询用户Id是否已经绑定
            if(!supplierUtils.checkExistByUserId(user.getUserId())) {
                throw new BusinessException("该用户已经绑定");
            }
            supplier.setUserId(user.getUserId());
            supplierUtils.update(supplier);
        }
        SupplierService supplierService = new SupplierService();
        BeanUtils.copyProperties(serviceParam, supplierService);
        return this.save(supplierService);
    }

}
