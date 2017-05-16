package com.hk.sfs.metadata.service.impl;

import com.hk.sfs.metadata.dao.UserDao;
import com.hk.sfs.metadata.entity.UserEntity;
import com.hk.sfs.metadata.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @date 2017/4/16
 */
@Service
public class UserServiceImpl extends AbsBaseService<UserEntity, UserDao> implements UserService {

	@Override
	public UserEntity getUserByName(String userName) {

		return null;
	}

}
