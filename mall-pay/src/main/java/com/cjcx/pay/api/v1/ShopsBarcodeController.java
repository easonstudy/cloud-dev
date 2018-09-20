package com.cjcx.pay.api.v1;

import com.cjcx.pay.dto.ShopsBarcodeDto;
import com.cjcx.pay.dto.StaffDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.annotation.AuthToken;
import com.cjcx.pay.framework.controller.BaseController;
import com.cjcx.pay.framework.service.IBaseService;
import com.cjcx.pay.framework.web.ResultObject;
import com.cjcx.pay.service.shops.IShopsBarcodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/shops_barcode")
public class ShopsBarcodeController extends BaseController<ShopsBarcodeDto, Integer> {

    @Autowired
    IShopsBarcodeService service;

    @Override
    protected IBaseService<ShopsBarcodeDto, Integer> getDefaultService() {
        return service;
    }





    /**
     * 新增或者修改
     *
     * @param dto
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/merge", method = RequestMethod.POST)
    public ResultObject merge(ShopsBarcodeDto dto) {
        ResultObject result = new ResultObject();
        try {
            //log.info("ShopsBarcodeDto merge:" + dto.getIsDelete());

            result = getDefaultService().merge(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResultObject delete(@PathVariable("id") Integer id) {
        ResultObject result = new ResultObject();
        StaffDto m = null;
        try {
            log.info("ShopsBarcode delete:" + id);
            ShopsBarcodeDto bar = new ShopsBarcodeDto();
            bar.setId(id);
            bar.setIsDelete(true);
            service.updateById(bar);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrCode(ResultObject.ERRCODE_NO);
        }
        return result;
    }

    /**
     * 列表
     *
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/list")
    public ResultObject list(FilterDto filterDto) {
        ResultObject result = new ResultObject();
        try {
            Boolean b = enabledPaging();
            List<ShopsBarcodeDto> list = service.getShopsBarcodeList(filterDto);
            if(b) {
                Page<ShopsBarcodeDto> page = (Page<ShopsBarcodeDto>) list;
                PageInfo<ShopsBarcodeDto> pageInfo = new PageInfo<ShopsBarcodeDto>(page);
                result.setData(pageInfo);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
