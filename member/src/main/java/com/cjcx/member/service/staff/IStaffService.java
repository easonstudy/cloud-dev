package com.cjcx.member.service.staff;


import com.cjcx.member.dto.StaffDto;
import com.cjcx.member.framework.service.IBaseService;

import java.util.Map;

public interface IStaffService extends IBaseService<StaffDto, Integer> {

    Map changePassword(String token, String oldPassword, String newPassword);
    Map changePassword(StaffDto StaffDto);
    Map changeProfile(String token, String nickName, String email);

}
