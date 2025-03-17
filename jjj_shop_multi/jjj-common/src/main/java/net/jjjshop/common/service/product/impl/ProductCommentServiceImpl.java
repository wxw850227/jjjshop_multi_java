package net.jjjshop.common.service.product.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductComment;
import net.jjjshop.common.mapper.product.ProductCommentMapper;
import net.jjjshop.common.service.product.ProductCommentService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;



/**
 * 订单评价记录表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductCommentServiceImpl extends BaseServiceImpl<ProductCommentMapper, ProductComment> implements ProductCommentService {
}
