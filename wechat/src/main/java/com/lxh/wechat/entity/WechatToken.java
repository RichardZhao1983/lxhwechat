package com.lxh.wechat.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "wechat_token")

public class WechatToken {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String wechatUserId;
	private String wechatToken;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	protected WechatToken() {
	}

	public WechatToken(String wechatUserId, String wechatToken, Date createdAt) {
		super();
		this.wechatUserId = wechatUserId;
		this.wechatToken = wechatToken;
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return String.format("User[id=%d, wechatUserId='%s', wechatToken='%s', createdAt='%tx']", id, wechatUserId,
				wechatToken, createdAt);
	}
}
