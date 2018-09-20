package com.cjcx.member.service.user;

import com.cjcx.member.dto.UserRequestDto;
import com.cjcx.member.framework.service.IBaseService;

import java.util.Map;

public interface IUserRequestService extends IBaseService<UserRequestDto, Integer> {


    Map<String, Object> doRequestTest(UserRequestDto userRequestDto);


}
