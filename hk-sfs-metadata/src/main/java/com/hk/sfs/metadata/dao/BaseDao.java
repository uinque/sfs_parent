package com.hk.sfs.metadata.dao;

import com.hk.sfs.metadata.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Administrator
 * @date 2017/4/15
 */
public interface BaseDao <T extends BaseEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
}
