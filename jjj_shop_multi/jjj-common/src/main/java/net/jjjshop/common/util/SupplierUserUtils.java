package net.jjjshop.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.config.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SupplierUserUtils {

    @Autowired
    private SupplierUserService supplierUserService;

    /**
     * 验证用户名是否重复
     * @param userName
     * @return
     */
    public Boolean checkExist(String userName) {
        Integer count = supplierUserService.count(new LambdaQueryWrapper<SupplierUser>()
                .eq(SupplierUser::getIsDelete, 0)
                .eq(SupplierUser::getUserName, userName).comment(CommonConstant.NOT_WITH_App_Id));
        if (count > 0) {
            return false;
        }
        return true;
    }

}
