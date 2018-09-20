package com.cjcx.member.api.v1;

import com.cjcx.member.dto.TransactionGiftDto;
import com.cjcx.member.dto.filter.FilterDto;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.controller.BaseController;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.PageParams;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.score.ITransactionGiftService;
import com.cjcx.member.utils.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TransactionGiftController extends BaseController<TransactionGiftDto, Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ITransactionGiftService service;

    @Override
    protected IBaseService<TransactionGiftDto, Integer> getDefaultService() {
        return service;
    }

    /**
     * 礼品兑换历史
     *
     * @param dto
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/gift/exchange/list")
    public ResultObject exchangeHistory(@RequestBody(required = false) FilterDto dto) {
        ResultObject result = new ResultObject();
        try {
            PageParams params = getPageParams(request);
            if (params.isEnabled()) {
                PageHelper.startPage(params.getPageNum(), params.getPageSize());
            }

            if (dto.getGiftStartTime() != null) {
                dto.setGiftStartTime(DateUtil.add(dto.getGiftStartTime(), Calendar.DAY_OF_MONTH, 1));
                dto.setGiftEndTime(DateUtil.add(dto.getGiftEndTime(), Calendar.DAY_OF_MONTH, 1));
                logger.info("开始时间:" + DateUtil.convertToStr(LocalDateTime.ofInstant(dto.getGiftStartTime().toInstant(), ZoneId.systemDefault())));
                logger.info("结束时间:" + DateUtil.convertToStr(LocalDateTime.ofInstant(dto.getGiftEndTime().toInstant(), ZoneId.systemDefault())));
                //logger.info("开始时间2:" + dto.getGiftStartTime());
            }

            List<TransactionGiftDto> list = service.getTransactionGiftList(dto);
            Page<TransactionGiftDto> page = (Page<TransactionGiftDto>) list;
            PageInfo<TransactionGiftDto> pageInfo = new PageInfo<TransactionGiftDto>(page);
            result.setData(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
