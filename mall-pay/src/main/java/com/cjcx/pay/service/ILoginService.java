package com.cjcx.pay.service;


import com.cjcx.pay.dto.StaffDto;

public interface ILoginService {

    public String login(String username, String password);

    public String logout();

    public StaffDto getUserByToken(String token);

}
