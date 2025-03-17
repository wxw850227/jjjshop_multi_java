package net.jjjshop.common.service.settings.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.settings.Delivery;
import net.jjjshop.common.entity.settings.DeliveryRule;
import net.jjjshop.common.mapper.settings.DeliveryMapper;
import net.jjjshop.common.service.settings.DeliveryService;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送模板主表 服务实现类
 * @author jjjshop
 * @since 2022-06-29
 */
@Slf4j
@Service
public class DeliveryServiceImpl extends BaseServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {
}
