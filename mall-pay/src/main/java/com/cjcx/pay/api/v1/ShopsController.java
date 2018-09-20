package com.cjcx.pay.api.v1;


import com.cjcx.pay.dto.ShopsDto;
import com.cjcx.pay.dto.StaffDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.annotation.AuthToken;
import com.cjcx.pay.framework.controller.BaseController;
import com.cjcx.pay.framework.service.IBaseService;
import com.cjcx.pay.framework.web.ResultObject;
import com.cjcx.pay.service.shops.IShopsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shops")
public class ShopsController extends BaseController<ShopsDto, Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IShopsService service;

    @Override
    protected IBaseService<ShopsDto, Integer> getDefaultService() {
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
    public ResultObject merge(ShopsDto dto) {
        ResultObject result = new ResultObject();
        try {
            logger.info("Shops merge:" + dto.getIsDelete());
            //dto.setUpdateBy();
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
            logger.info("Shops delete:" + id);
            ShopsDto shops = new ShopsDto();
            shops.setId(id);
            shops.setIsDelete(true);
            service.updateById(shops);
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
            List<ShopsDto> list = service.getShopsList(filterDto);
            if(b) {
                Page<ShopsDto> page = (Page<ShopsDto>) list;
                PageInfo<ShopsDto> pageInfo = new PageInfo<ShopsDto>(page);
                result.setData(pageInfo);
                //result.setData(convertListToPageInfo(list));
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}
