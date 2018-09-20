package com.cjcx.member.framework.controller;

import com.cjcx.member.dto.StaffDto;
import com.cjcx.member.entity.Staff;
import com.cjcx.member.framework.web.PageParams;
import com.cjcx.member.service.ILoginService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


public abstract class AdminSupport {

    public static final String PARAM_PAGE_NUM = "pageNum";
    public static final String PARAM_PAGE_SIZE = "pageSize";

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @Autowired
    ILoginService loginService;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    public String getAuthorizationToken() {
        return request.getHeader("Authorization");
    }


    public Staff getStaff() {
        String token = getAuthorizationToken();
        StaffDto staff = loginService.getUserByToken(token);
        return staff;
    }

    /**
     * 是否开启分页
     */
    protected void enabledPaging() {
        PageParams params = getPageParams(request);
        if (params.isEnabled()) {
            PageHelper.startPage(params.getPageNum(), params.getPageSize());
        }
    }

    protected PageParams getPageParams(HttpServletRequest request) {
        Map<String, Object> filters = WebUtils.getParametersStartingWith(request, "");
        PageParams page = new PageParams();
        if (filters != null) {
            String pageNum = String.valueOf(filters.get(PARAM_PAGE_NUM));
            if (StringUtils.isNumeric(pageNum)) {
                page.setPageNum(Integer.parseInt(pageNum));
                page.setEnabled(true);
            }

            String pageSize = String.valueOf(filters.get(PARAM_PAGE_SIZE));
            if (StringUtils.isNumeric(pageSize)) {
                page.setPageSize(Integer.parseInt(pageSize));
                page.setEnabled(true);
            }
        }
        return page;
    }


}

