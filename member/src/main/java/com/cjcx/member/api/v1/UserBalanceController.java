package com.cjcx.member.api.v1;

import com.cjcx.member.dto.UserBalanceDto;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.controller.BaseController;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.PageParams;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.user.IUserBalanceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserBalanceController extends BaseController<UserBalanceDto,Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IUserBalanceService userBalanceService;

    @Override
    protected IBaseService<UserBalanceDto, Integer> getDefaultService() {
        return userBalanceService;
    }

    @AuthToken
    @RequestMapping(value="/balance/list", method= RequestMethod.GET)
    public ResultObject list() {
        ResultObject result= new ResultObject();
        try {
            PageParams params = super.getPageParams(request);
            if (params.isEnabled()){
                PageHelper.startPage(params.getPageNum(), params.getPageSize() );
            }

            UserBalanceDto condition = new UserBalanceDto();
            List<UserBalanceDto> list = userBalanceService.findByCondition(condition);

            Page<UserBalanceDto> page = (Page<UserBalanceDto>)list;
            PageInfo<UserBalanceDto> pageInfo = new PageInfo<UserBalanceDto>(page);
            result.setData(pageInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
