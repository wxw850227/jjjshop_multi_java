package net.jjjshop.supplier.service.settings.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.ReturnAddress;
import net.jjjshop.common.mapper.settings.ReturnAddressMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.supplier.param.setting.ReturnAddressPageParam;
import net.jjjshop.supplier.param.setting.ReturnAddressParam;
import net.jjjshop.supplier.service.settings.ReturnAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 退货地址记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-20
 */
@Slf4j
@Service
public class ReturnAddressServiceImpl extends BaseServiceImpl<ReturnAddressMapper, ReturnAddress> implements ReturnAddressService {

    /**
     * 分页查询退货地址
     * @param returnAddressPageParam
     * @return
     */
    public Paging<ReturnAddress> getList(ReturnAddressPageParam returnAddressPageParam) {
        return new Paging<>(this.page(new PageInfo<>(returnAddressPageParam), new LambdaQueryWrapper<ReturnAddress>()
                .eq(ReturnAddress::getShopSupplierId, returnAddressPageParam.getShopSupplierId())
                .orderByAsc(ReturnAddress::getSort)));
    }

    /**
     * 查询所有退货地址
     * @param
     * @return
     */
    public List<ReturnAddress> getAll(Integer shopSupplierId) {
        return this.list( new LambdaQueryWrapper<ReturnAddress>().orderByAsc(ReturnAddress::getSort).eq(ReturnAddress::getShopSupplierId, shopSupplierId));
    }


    /**
     * 添加退货地址
     * @param returnAddressParam
     * @return
     */
    public boolean add(ReturnAddressParam returnAddressParam) {
        ReturnAddress returnAddress = new ReturnAddress();
        BeanUtils.copyProperties(returnAddressParam, returnAddress);
        return this.save(returnAddress);
    }

    /**
     * 修改退货地址
     * @param returnAddressParam
     * @return
     */
    public boolean edit(ReturnAddressParam returnAddressParam) {
        ReturnAddress returnAddress = new ReturnAddress();
        BeanUtils.copyProperties(returnAddressParam, returnAddress);
        return this.updateById(returnAddress);
    }

    /**
     * 删除退货地址
     * @param id
     * @return
     */
    public boolean setDelete(Integer id) {
        return this.update(new LambdaUpdateWrapper<ReturnAddress>().eq(ReturnAddress::getAddressId, id)
                .set(ReturnAddress::getIsDelete, 1));
    }
}
