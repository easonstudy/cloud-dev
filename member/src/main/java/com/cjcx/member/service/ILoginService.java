package com.cjcx.member.service;

import com.cjcx.member.dto.StaffDto;

public interface ILoginService {

    public String login(String username, String password);

    public String logout();

    public StaffDto getUserByToken(String token);

}
