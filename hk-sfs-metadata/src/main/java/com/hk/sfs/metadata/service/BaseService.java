package com.hk.sfs.metadata.service;

import com.hk.sfs.metadata.dao.BaseDao;
import com.hk.sfs.metadata.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author Administrator
 * @date 2017/4/16
 */
public interface BaseService<T extends BaseEntity,Dao extends BaseDao<T>> {

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity
     * @return the saved entity
     */
    public T save(T entity);

    /**
     * Saves all given entities.
     *
     * @param entities
     * @return the saved entities
     * @throws IllegalArgumentException in case the given entity is (@literal null}.
     */
    public Iterable<T> save(Iterable<T> entities);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    public T findOne(Long id);

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    public Iterable<T> findAll();

    /**
     * Returns all instances of the type with the given IDs.
     *
     * @param ids
     * @return
     */
    public Iterable<T> findAll(Iterable<Long> ids);

    /**
     * Returns all entities sorted by the given options.
     *
     * @param sort
     * @return all entities sorted by the given options
     */
    public Iterable<T> findAll(Sort sort);

    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable
     * @return a page of entities
     */
    public Page<T> findAll(Pageable pageable);

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    public void delete(Long id);

    /**
     * Deletes a given entity.
     *
     * @param entity
     * @throws IllegalArgumentException in case the given entity is (@literal null}.
     */
    public void delete(T entity);

    /**
     * Deletes the given entities.
     *
     * @param entities
     * @throws IllegalArgumentException in case the given {@link Iterable} is (@literal null}.
     */
    public void delete(Iterable<? extends T> entities);

    /**
     * Returns a single entity matching the given {@link org.springframework.data.jpa.domain.Specification}.
     *
     * @param spec
     * @return
     */
    public T findOne(Specification<T> spec);

    /**
     * Returns all entities matching the given {@link Specification}.
     *
     * @param spec
     * @return
     */
    public List<T> findAll(Specification<T> spec);

    /**
     * Returns a {@link Page} of entities matching the given {@link Specification}.
     *
     * @param spec
     * @param pageable
     * @return
     */
    public Page<T> findAll(Specification<T> spec, Pageable pageable);

    /**
     * Returns all entities matching the given {@link Specification} and {@link Sort}.
     *
     * @param spec
     * @param sort
     * @return
     */
    public List<T> findAll(Specification<T> spec, Sort sort);
}
