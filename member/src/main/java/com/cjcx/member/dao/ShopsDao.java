package com.cjcx.member.dao;

import com.cjcx.member.dto.ShopsDto;
import com.cjcx.member.dto.filter.FilterDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopsDao extends IBaseDao<ShopsDto, Integer> {

    List<ShopsDto> getShopsList(FilterDto filterDto);


}
