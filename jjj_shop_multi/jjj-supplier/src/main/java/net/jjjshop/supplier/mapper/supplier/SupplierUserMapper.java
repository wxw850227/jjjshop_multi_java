package net.jjjshop.supplier.mapper.supplier;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.shop.ShopAccess;
import net.jjjshop.common.entity.shop.ShopUser;
import net.jjjshop.common.entity.supplier.SupplierAccess;
import net.jjjshop.common.entity.supplier.SupplierUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商家用户记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-06-23
 */
@Repository
public interface SupplierUserMapper extends BaseMapper<SupplierUser> {
    List<SupplierAccess> getAccessByUserId(@Param("userId") Integer userId);
}
