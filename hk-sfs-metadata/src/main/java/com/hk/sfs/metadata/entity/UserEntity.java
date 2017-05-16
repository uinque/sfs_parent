package com.hk.sfs.metadata.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Administrator
 * @date 2017/4/15
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "MEMBER")
public class UserEntity extends BaseEntity {

	private String userName;

	private Integer age;

	private String sex;

	private String qq;

	public UserEntity() {
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "AGE")
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(name = "SEX")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "QQ")
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Override
	public String toString() {
		return "UserEntity{" + "userName='" + userName + '\'' + ", age=" + age + ", sex='" + sex + '\'' + ", qq='" + qq
				+ '\'' + '}';
	}
}
