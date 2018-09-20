package com.cjcx.member.framework.orm;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;


/**
 * Dao基础接口，所有Dao应该继承于此接口
 *
 * @param
 * @param
 * @author
 */
public interface IBaseDao<E extends Serializable, Id extends Serializable> {

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
     * 根据主键集合查找数据
     *
     * @param ids
     * @return
     */
    List<E> findByIds(Id[] ids);


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

    /**
     * 根据组合条件查询数据
     *
     * @param
     * @return
     */
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
    Integer checkUnique(@Param("dto") E dto, @Param("uniquePropertyNames") String uniquePropertyNames);

}
