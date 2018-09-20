package com.cjcx.pay.framework.service;



import com.cjcx.pay.framework.orm.BaseEntity;
import com.cjcx.pay.framework.web.ResultObject;

import java.io.Serializable;
import java.util.List;

public interface IBaseService<E extends BaseEntity, Id extends Serializable> {



    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    int insert(E entity);

    /**
     * 根据主键更新数据
     *
     * @param entity
     * @return
     */
    int updateById(E entity);


    ResultObject merge(E dto);
    /**
     * 根据主键删除数据
     *
     * @param
     * @return
     */
    int deleteById(Id id);

    /**
     * 根据主键查找数据
     *
     * @param
     * @return
     */
    E findById(Id id);

    /**
     * 根据主键查找数据
     *
     * @param entity
     * @return
     */
    E findByEntityId(E entity);

    /**
     * 查询所有数据
     *
     * @return
     */
    List<E> findAll();


    List<E> findByCondition(E condition);


    /**
     * 统计表数据量
     *
     * @return
     */
    Integer countAll();

    /**
     * 根据条件统计表数据量
     *
     * @param
     * @return
     */
    Integer countByCondition(E condition);


    /**
     * 唯一性检查
     *
     * @param dto
     * @param
     * @return
     */
    Integer checkUnique(E dto, String uniquePropertyNames);

    /**
     * 根据主键集合查找数据
     *
     * @param ids
     * @return
     */
    List<E> findByIds(Id[] ids);

}
