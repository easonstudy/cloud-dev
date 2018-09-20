package com.cjcx.member.api.v1;

import com.cjcx.member.dto.PermissionsDto;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.controller.BaseController;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.PageParams;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.staff.IPermissionsService;
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
public class PermissionsController extends BaseController<PermissionsDto, Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IPermissionsService permissionsService;

    @Override
    protected IBaseService<PermissionsDto, Integer> getDefaultService() {
        return permissionsService;
    }

    @AuthToken
    @RequestMapping(value = "/permissions/list", method = RequestMethod.GET)
    public ResultObject list() {
        ResultObject result = new ResultObject();
        try {
            PageParams params = super.getPageParams(request);
            if (params.isEnabled()) {
                PageHelper.startPage(params.getPageNum(), params.getPageSize());
            }
            List<PermissionsDto> list = permissionsService.findAll();

            Page<PermissionsDto> page = (Page<PermissionsDto>) list;
            PageInfo<PermissionsDto> pageInfo = new PageInfo<PermissionsDto>(page);
            result.setData(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}