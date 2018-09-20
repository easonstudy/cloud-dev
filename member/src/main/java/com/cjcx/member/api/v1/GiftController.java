package com.cjcx.member.api.v1;

import com.cjcx.member.dto.GiftDto;
import com.cjcx.member.dto.StaffDto;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.controller.BaseController;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.score.IGiftService;
import com.cjcx.member.service.score.ITransactionGiftService;
import com.cjcx.member.utils.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gift")
public class GiftController extends BaseController<GiftDto, Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IGiftService service;

    @Autowired
    ITransactionGiftService transactionGiftService;

    @Override
    protected IBaseService<GiftDto, Integer> getDefaultService() {
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
    public ResultObject merge(GiftDto dto) {
        ResultObject result = new ResultObject();
        try {
            result = service.merge(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResultObject deleteStaff(@PathVariable("id") Integer id) {
        ResultObject result = new ResultObject();
        StaffDto m = null;
        try {
            logger.info("USER DELETE:" + id);
            GiftDto gift = new GiftDto();
            gift.setId(id);
            gift.setIsDelete(true);
            service.updateById(gift);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrCode(ResultObject.ERRCODE_NO);
        }
        return result;
    }

    /**
     * 列表
     *
     * @param dto
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/list")
    public ResultObject list(@RequestBody(required = false) GiftDto dto) {
        ResultObject result = new ResultObject();
        try {
            super.enabledPaging();
            List<GiftDto> list = service.findByCondition(dto);
            result.setData(super.convertListToPageInfo(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 列表
     *
     * @param
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/searchList")
    public ResultObject list(@RequestParam("filterStr") String filterStr) {
        ResultObject result = new ResultObject();
        try {
            super.enabledPaging();
            GiftDto dto = new GiftDto();
            dto.setFilterStr(filterStr);
            List<GiftDto> list = service.searchByCondition(dto);
            result.setData(super.convertListToPageInfo(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 礼品兑换
     *
     * @param dto
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/exchange")
    public ResultObject exchange(@RequestBody GiftDto dto) {
        ResultObject r = new ResultObject();
        try {
            logger.info(GsonUtil.toJson(dto));
            r = transactionGiftService.doTransactionGift(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }



}
