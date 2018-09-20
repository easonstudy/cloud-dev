package com.cjcx.member.api.v1;

import com.cjcx.member.dto.MessageDto;
import com.cjcx.member.dto.StaffDto;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.controller.BaseController;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.PageParams;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.message.impl.MessageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import weixin.popular.util.JsonUtil;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MessageController extends BaseController<MessageDto, Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MessageService messageService;

    @Override
    protected IBaseService<MessageDto, Integer> getDefaultService() {
        return messageService;
    }


    /**
     * 新增或者修改
     * @param messageDto
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/message/merge", method = RequestMethod.POST)
    public ResultObject merge(MessageDto messageDto) {
        ResultObject result = new ResultObject();
        try {
            Map map = messageService.merge(messageDto);
            result.setErrCode(map.get(ResultObject.PARAM_ERRCODE).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/message/list", method = RequestMethod.GET)
    public ResultObject list(MessageDto dto) {
        ResultObject result = new ResultObject();
        try {
            PageParams params = super.getPageParams(request);
            if (params.isEnabled()) {
                PageHelper.startPage(params.getPageNum(), params.getPageSize());
            }
            MessageDto condition = new MessageDto();
            condition.setMsgtype(dto.getMsgtype());
            List<MessageDto> list = messageService.findByCondition(condition);

            Page<MessageDto> page = (Page<MessageDto>) list;
            PageInfo<MessageDto> pageInfo = new PageInfo<MessageDto>(page);
            result.setData(pageInfo);

            logger.info(JsonUtil.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
