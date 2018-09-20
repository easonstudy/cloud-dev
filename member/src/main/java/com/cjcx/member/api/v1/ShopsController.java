package com.cjcx.member.api.v1;

import com.cjcx.member.dto.GiftDto;
import com.cjcx.member.dto.ScoreRatioDto;
import com.cjcx.member.dto.ShopsDto;
import com.cjcx.member.dto.StaffDto;
import com.cjcx.member.dto.filter.FilterDto;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.controller.BaseController;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.shops.IShopsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            enabledPaging();
            List<ShopsDto> list = service.getShopsList(filterDto);
            result.setData(convertListToPageInfo(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}
