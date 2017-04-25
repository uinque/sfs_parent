package com.hk.sfs.metadata.service.impl;

import com.hk.sfs.metadata.dao.BaseDao;
import com.hk.sfs.metadata.entity.BaseEntity;
import com.hk.sfs.metadata.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Administrator
 * @date 2017/4/16
 */
public abstract class AbsBaseService<T extends BaseEntity,Dao extends BaseDao<T>> implements BaseService<T,Dao>{

    @Autowired
    protected Dao dao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public T save(T entity) {
        return dao.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Iterable<T> save(Iterable<T> entities) {
        return dao.save(entities);
    }

    @Override
    public T findOne(Long id) {
        return dao.findOne(id);
    }

    @Override
    public Iterable<T> findAll() {
        return dao.findAll();
    }

    @Override
    public Iterable<T> findAll(Iterable<Long> ids) {
        return dao.findAll(ids);
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        return dao.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return dao.findAll(pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) {
        dao.delete(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(T entity) {
        dao.delete(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Iterable<? extends T> entities) {
        dao.delete(entities);
    }

    public T findOne(Specification<T> spec) {
        return dao.findOne(spec);
    }

    /**
     * Returns all entities matching the given {@link Specification}.
     *
     * @param spec
     * @return
     */
    public List<T> findAll(Specification<T> spec) {
        return dao.findAll(spec);
    }

    /**
     * Returns a {@link Page} of entities matching the given {@link Specification}.
     *
     * @param spec
     * @param pageable
     * @return
     */
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return dao.findAll(spec, pageable);
    }

    /**
     * Returns all entities matching the given {@link Specification} and {@link Sort}.
     *
     * @param spec
     * @param sort
     * @return
     */
    public List<T> findAll(Specification<T> spec, Sort sort) {
        return dao.findAll(spec, sort);
    }
}
