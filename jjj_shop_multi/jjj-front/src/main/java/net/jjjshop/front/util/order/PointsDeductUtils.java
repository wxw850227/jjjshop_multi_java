package net.jjjshop.front.util.order;

import net.jjjshop.common.settings.vo.PointsVo;
import net.jjjshop.front.vo.product.ProductBuyVo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 积分抵扣类
 */
public class PointsDeductUtils {
    public static void setProductPoints(List<ProductBuyVo> productList, int maxPointsNumCount, int actualPointsNum, PointsVo pointsVo)
    {
        // 计算实际积分抵扣数量
        setProductListPointsNum(productList, maxPointsNumCount, actualPointsNum);
        // 计算实际积分抵扣金额
        setProductListPointsMoney(productList, pointsVo);
    }

    /**
     * 计算实际积分抵扣数量
     */
    private static void setProductListPointsNum(List<ProductBuyVo> productList, int maxPointsNumCount, int actualPointsNum)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        double percent = Double.parseDouble(df.format((double)actualPointsNum/maxPointsNumCount));
        int totalNum = 0;
        for (int i=0;i<productList.size();i++) {
            if (productList.get(i).getIsPointsDiscount() == 0) {
                continue;
            }
            if(i == productList.size() - 1){
                productList.get(i).setPointsNum(actualPointsNum - totalNum);
            }else{
                productList.get(i).setPointsNum((int)percent * productList.get(i).getMaxPointsNum());
                totalNum += productList.get(i).getPointsNum();
            }
        }
    }

    /**
     * 计算实际积分抵扣金额
     */
    private static void setProductListPointsMoney(List<ProductBuyVo> productList, PointsVo pointsVo) {
        for (ProductBuyVo product:productList) {
            if (product.getIsPointsDiscount() == 0) {
                continue;
            }
            product.setPointsMoney(BigDecimal.valueOf(product.getPointsNum() * pointsVo.getDiscount().getDiscountRatio()).setScale(2, RoundingMode.DOWN));
        }
    }
}
