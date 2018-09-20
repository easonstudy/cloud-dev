package com.cjcx.pay.framework.service;



import com.cjcx.pay.framework.orm.BaseEntity;
import com.cjcx.pay.framework.orm.IBaseDao;
import com.cjcx.pay.framework.web.ResultObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public abstract class BaseService<E extends BaseEntity, Id extends Serializable> extends AdminService implements IBaseService<E, Id> {

    protected abstract IBaseDao<E, Id> getDao();

    @Override
    public int insert(E entity) {
        return getDao().insert(entity);
    }

    @Override
    public int updateById(E entity) {
        return getDao().updateById(entity);
    }

    @Override
    public ResultObject merge(E dto) {
        ResultObject r = new ResultObject();
        try {
            if (dto.getId() != null && dto.getId() > 0) {
                //编辑
                dto.setUpdateTime(new Date());
                getDao().updateById(dto);
            } else {
                //新增
                dto.setCreateTime(new Date());
                getDao().insert(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-10001");
        }
        return r;
    }

    @Override
    public int deleteById(Id id) {
        return getDao().deleteById(id);
    }

    @Override
    public E findById(Id id) {
        return getDao().findById(id);
    }

    @Override
    public List<E> findByIds(Id[] ids) {
        return getDao().findByIds(ids);
    }

    @Override
    public E findByEntityId(E entity) {
        if (entity == null) {
            return null;
        }

        return getDao().findByEntityId(entity);
    }

    @Override
    public List<E> findAll() {
        return getDao().findAll();
    }

    @Override
    public List<E> findByCondition(E condition) {
        if (condition == null) {
            return getDao().findAll();
        }
        return getDao().findByCondition(condition);
    }

    @Override
    public Integer countAll() {
        return getDao().countAll();
    }

    @Override
    public Integer countByCondition(E condition) {
        return getDao().countByCondition(condition);
    }

    @Override
    public Integer checkUnique(E dto, String uniquePropertyNames) {
        return getDao().checkUnique(dto, uniquePropertyNames);
    }
}
