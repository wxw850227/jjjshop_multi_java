package net.jjjshop.front.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.front.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BaseSupplierController extends BaseController {
    @Autowired
    private SupplierUserService supplierUserService;
    /**
     * 校验参数
     * @param shopSupplierId
     * @return
     */
    public boolean checkSupplier(Integer shopSupplierId){
        User user = this.getUser(true);
        Map<String, Object> map = new HashMap<>();
        SupplierUser supplierUser = supplierUserService.getOne(new LambdaQueryWrapper<SupplierUser>().eq(SupplierUser::getUserId, user.getUserId()).last("LIMIT 1"));
        if(shopSupplierId.intValue() != supplierUser.getShopSupplierId().intValue()) {
            throw new BusinessException("非法请求");
        }
        return true;
    }
}
