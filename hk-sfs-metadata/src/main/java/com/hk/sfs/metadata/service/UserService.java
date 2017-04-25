package com.hk.sfs.metadata.service;

import com.hk.sfs.metadata.entity.UserEntity;
import com.hk.sfs.metadata.dao.UserDao;

/**
 * @author Administrator
 * @date 2017/4/16
 */
public interface UserService extends BaseService<UserEntity,UserDao> {

    public UserEntity getUserByName(String userName);
}
