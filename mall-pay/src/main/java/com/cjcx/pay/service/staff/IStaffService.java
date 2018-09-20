package com.cjcx.pay.service.staff;



import com.cjcx.pay.dto.StaffDto;
import com.cjcx.pay.framework.service.IBaseService;

import java.util.Map;

public interface IStaffService extends IBaseService<StaffDto, Integer> {

    Map changePassword(String token, String oldPassword, String newPassword);
    Map changePassword(StaffDto StaffDto);
    Map changeProfile(String token, String nickName, String email);

}
