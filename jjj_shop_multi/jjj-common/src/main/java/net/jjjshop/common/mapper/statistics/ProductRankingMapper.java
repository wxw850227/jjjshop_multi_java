package net.jjjshop.common.mapper.statistics;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.vo.statistics.ProductRefundRankingVo;
import net.jjjshop.common.vo.statistics.ProductSaleRankingVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRankingMapper extends BaseMapper {

    /**
     * 获取商品销售排行
     * @param
     * @return
     */
    List<ProductSaleRankingVo> getSaleRanking(@Param("shopSupplierId") Integer shopSupplierId);

    /**
     * 获取商品售后排行
     * @param
     * @return
     */
    List<ProductRefundRankingVo> getRefundRanking(@Param("shopSupplierId") Integer shopSupplierId);

}
