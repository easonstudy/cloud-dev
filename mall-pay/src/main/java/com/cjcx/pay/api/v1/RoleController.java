package com.cjcx.pay.api.v1;

import com.cjcx.pay.dto.PermissionsDto;
import com.cjcx.pay.dto.RoleDto;
import com.cjcx.pay.framework.annotation.AuthToken;
import com.cjcx.pay.framework.controller.BaseController;
import com.cjcx.pay.framework.service.IBaseService;
import com.cjcx.pay.framework.web.PageParams;
import com.cjcx.pay.framework.web.ResultObject;
import com.cjcx.pay.service.staff.IPermissionsService;
import com.cjcx.pay.service.staff.IRolePermissionsService;
import com.cjcx.pay.service.staff.IRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController extends BaseController<RoleDto, Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IRoleService roleService;

    @Autowired
    IPermissionsService permissionsService;

    @Autowired
    IRolePermissionsService rolePermissionsService;

    @Override
    protected IBaseService<RoleDto, Integer> getDefaultService() {
        return roleService;
    }

    @AuthToken
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultObject rolelist() {
        ResultObject result = new ResultObject();
        try {
            PageParams params = super.getPageParams(request);
            if (params.isEnabled()) {
                PageHelper.startPage(params.getPageNum(), params.getPageSize());
            }
            List<RoleDto> list = roleService.findAll();

            Page<RoleDto> page = (Page<RoleDto>) list;
            PageInfo<RoleDto> pageInfo = new PageInfo<RoleDto>(page);
            result.setData(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/permissions/update/{id}", method = RequestMethod.POST)
    public ResultObject updateRolePermissions(@PathVariable("id") Integer id, @RequestParam("keys") String keys) {
        ResultObject result = new ResultObject();
        try {
            logger.info("id:"+id);
            logger.info("keys:"+ keys) ;

            rolePermissionsService.updateRolePermissions(id, keys);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/permissions/{id}", method = RequestMethod.GET)
    public ResultObject getRolePermissions(@PathVariable("id") Integer roleId) {
        ResultObject result = new ResultObject();
        try {
            List<PermissionsDto> list = permissionsService.getPermissionsByRoleId(roleId);
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrCode(ResultObject.ERRCODE_NO);
        }
        return result;
    }

}