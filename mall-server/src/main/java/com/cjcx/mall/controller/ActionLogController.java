package com.cjcx.mall.controller;

import com.cjcx.mall.dto.ActionLogDto;
import com.cjcx.mall.service.IActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/log")
public class ActionLogController {

    @Autowired
    IActionLogService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String search(@PathVariable("id") Long id) {
        ActionLogDto dto  = service.select(id);

        if(dto == null)
            return "1";
        return dto.toString();
    }

}
