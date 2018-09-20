package com.cjcx.mall.dao;

import com.cjcx.mall.dto.ActionLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ActionLogDao {

    @Select("select * from t_action_log where id=#{id}")
    public ActionLogDto select(Long id);





}
