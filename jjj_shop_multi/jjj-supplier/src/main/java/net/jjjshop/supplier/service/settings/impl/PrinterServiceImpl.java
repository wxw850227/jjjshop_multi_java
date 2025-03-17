package net.jjjshop.supplier.service.settings.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.Printer;
import net.jjjshop.common.enums.PrinterTypeEnum;
import net.jjjshop.common.mapper.settings.PrinterMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.setting.PrinterPageParam;
import net.jjjshop.supplier.param.setting.PrinterParam;
import net.jjjshop.supplier.service.settings.PrinterService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小票打印机记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-20
 */
@Slf4j
@Service
public class PrinterServiceImpl extends BaseServiceImpl<PrinterMapper, Printer> implements PrinterService {

    /**
     * 分页查询小票打印机
     * @param printerPageParam
     * @return
     */
    public Paging<Printer> getList(PrinterPageParam printerPageParam) {
        Paging<Printer> paging = new Paging<>(this.page(new PageInfo<>(printerPageParam), new LambdaQueryWrapper<Printer>()
                .eq(Printer::getShopSupplierId,SupplierLoginUtil.getShopSupplierId())
                //是否删除0=显示1=隐藏
                .eq(Printer::getIsDelete,0)
                .orderByAsc(Printer::getSort)
        ));
        if(paging.getTotal() > 0){
            paging.getRecords().forEach(o -> {
                if (PrinterTypeEnum.FEI_E_YUN.getPrinterType().equals(o.getPrinterType())) {
                    o.setPrinterType(PrinterTypeEnum.FEI_E_YUN.getText());
                } else if (PrinterTypeEnum.PRINTER_CENTER.getPrinterType().equals(o.getPrinterType())) {
                    o.setPrinterType(PrinterTypeEnum.PRINTER_CENTER.getText());
                }
            });
        }
        return paging;
    }

    /**
     * 获取所有小票打印机
     * @param
     * @return
     */
    public List<Printer> getAll() {
        return this.list(new LambdaQueryWrapper<Printer>()
                .eq(Printer::getShopSupplierId,SupplierLoginUtil.getShopSupplierId())
                //是否删除0=显示1=隐藏
                .eq(Printer::getIsDelete,0)
                .orderByAsc(Printer::getSort)
        );
    }

    /**
     * 添加小票打印机
     * @param printerParam
     * @return
     */
    public boolean add(PrinterParam printerParam) {
        Printer bean = this.getPrinterByType(printerParam);
        if(bean.getShopSupplierId() == null){
            //获取当前登录用户的供应商ID
            bean.setShopSupplierId(SupplierLoginUtil.getShopSupplierId());
        }
        return this.save(bean);
    }

    /**
     * 修改小票打印机
     * @param printerParam
     * @return
     */
    public boolean edit(PrinterParam printerParam) {
        return this.updateById(this.getPrinterByType(printerParam));
    }

    /**
     * 删除小票打印机
     * @param id
     * @return
     */
    public boolean setDelete(Integer id) {
        return this.update(new LambdaUpdateWrapper<Printer>().eq(Printer::getPrinterId, id)
                .set(Printer::getIsDelete, 1));
    }

    /**
     * 查询小票打印机种类
     * @param
     * @return
     */
    public List<String> getPrinterTypeName() {
        return PrinterTypeEnum.getTypeName();
    }

    /**
     * 通过打印机的种类获取打印机对象
     * @param printerParam
     * @return
     */
    public Printer getPrinterByType(PrinterParam printerParam) {
        Printer printer = new Printer();
        BeanUtils.copyProperties(printerParam, printer);
        if (PrinterTypeEnum.FEI_E_YUN.getText().equals(printerParam.getPrinterType())) {
            String feiE = printerParam.getFeieYun().toJSONString();
            printer.setPrinterType(PrinterTypeEnum.FEI_E_YUN.getPrinterType());
            printer.setPrinterConfig(feiE);
        } else if (PrinterTypeEnum.PRINTER_CENTER.getText().equals(printerParam.getPrinterType())) {
            String print = printerParam.getPrintCenter().toJSONString();
            printer.setPrinterType(PrinterTypeEnum.PRINTER_CENTER.getPrinterType());
            printer.setPrinterConfig(print);
        }
        return printer;
    }
}
