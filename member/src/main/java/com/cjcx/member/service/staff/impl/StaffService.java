package com.cjcx.member.service.staff.impl;


import com.cjcx.member.dao.StaffDao;
import com.cjcx.member.dto.StaffDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.staff.IStaffService;
import com.cjcx.member.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class StaffService extends BaseService<StaffDto, Integer> implements IStaffService {

    @Autowired
    StaffDao dao;

    @Override
    protected IBaseDao<StaffDto, Integer> getDao() {
        return dao;
    }

    @Override
    public Map changePassword(String token, String oldPassword, String newPassword) {
        Map outmap = new HashMap();
        try {
            StaffDto staff = dao.findByToken(token);
            if (staff == null) {
                outmap.put(ResultObject.PARAM_ERRCODE, -1);
                return outmap;
            }
            if (!staff.getPassword().equals(Md5Util.getHexMD5Str(oldPassword))) {
                outmap.put(ResultObject.PARAM_ERRCODE, -1);
                return outmap;
            }
            staff.setPassword(Md5Util.getHexMD5Str(newPassword));
            dao.updateById(staff);
            outmap.put(ResultObject.PARAM_ERRCODE, 0);
        } catch (Exception e) {
            e.printStackTrace();
            outmap.put(ResultObject.PARAM_ERRCODE, -1);
        }
        return outmap;
    }

    @Override
    public Map changePassword(StaffDto staffDto) {
        Map outmap = new HashMap();
        try {
            String newpwd = staffDto.getPassword();

            StaffDto staff = dao.findById(staffDto.getId());
            if (staff == null) {
                outmap.put(ResultObject.PARAM_ERRCODE, -1);
                return outmap;
            }

            staff.setPassword(Md5Util.getHexMD5Str(newpwd));
            dao.updateById(staff);
            outmap.put(ResultObject.PARAM_ERRCODE, 0);
        } catch (Exception e) {
            e.printStackTrace();
            outmap.put(ResultObject.PARAM_ERRCODE, -1);
        }
        return outmap;
    }

    @Override
    public Map changeProfile(String token, String nickName, String email) {
        Map outmap = new HashMap();
        try {
            StaffDto staff = dao.findByToken(token);
            if (staff == null) {
                outmap.put(ResultObject.PARAM_ERRCODE, -1);
                return outmap;
            }
            staff.setNickName(nickName);
            staff.setEmail(email);
            dao.updateById(staff);
            outmap.put(ResultObject.PARAM_ERRCODE, 0);
        } catch (Exception e) {
            e.printStackTrace();
            outmap.put(ResultObject.PARAM_ERRCODE, -1);
        }
        return outmap;
    }

    @Override
    public ResultObject merge(StaffDto dto) {
        ResultObject r = new ResultObject();
        try {
            if (dto.getId() != null && dto.getId() > 0) {
                //编辑
                dao.updateById(dto);
            } else {
                //新增
                String newpwd = Md5Util.getHexMD5Str(dto.getPassword());
                dto.setPassword(newpwd);
                dto.setCreateTime(new Date());
                dao.insert(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-10001");
        }
        return r;
    }
}
