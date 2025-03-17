package net.jjjshop.common.mapper.supplier;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.supplier.SupplierUserRole;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * 商家用户角色记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-10-19
 */
@Repository
public interface SupplierUserRoleMapper extends BaseMapper<SupplierUserRole> {
    /**
     * 根据角色id查询下面的用户数量
     * @param roleId
     * @return
     */
    Integer getUserRoleCount(Integer roleId);

    /**
     * 通过用户查找角色名称
     * @param supplierUserId
     * @return
     */
    List<Map> getRoleListByUser(Integer supplierUserId);

}
