package com.hk.sfs.metadata.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 * @date 2017/4/15
 */
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -8662772303432923796L;

	private Long id;

	private Date createTime;

	private Date modifyTime;

	private String modifyBy;

	private Boolean isDeleted;

	@Id
	@GeneratedValue(generator = "localIdGenerator")
	@GenericGenerator(name = "localIdGenerator", strategy = "com.hk.sfs.metadata.entity.LocalIdGenerator")
	@Column(name = "ID")
	//@JsonSerialize(using=IdSerializer.class)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", updatable = false)
	@CreatedDate
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_BY")
	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Column(name = "IS_DELETED", columnDefinition = "tinyint default 0 comment '逻辑删除字段(0:正常,1:删除)'")
	public Boolean getDeleted() {
		return isDeleted;
	}

	public void setDeleted(Boolean deleted) {
		isDeleted = deleted;
	}
}
