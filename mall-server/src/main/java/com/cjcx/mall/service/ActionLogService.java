package com.cjcx.mall.service;

import com.cjcx.mall.dao.ActionLogDao;
import com.cjcx.mall.dto.ActionLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionLogService implements IActionLogService {
    @Autowired
    ActionLogDao dao;

    @Override
    public ActionLogDto select(Long id) {
        try {
            return dao.select(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
