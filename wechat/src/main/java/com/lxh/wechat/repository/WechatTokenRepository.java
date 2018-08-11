package com.lxh.wechat.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.lxh.wechat.entity.WechatToken;

public interface WechatTokenRepository extends CrudRepository<WechatToken, Long> {
	List<WechatToken> findByWechatUserId(String wechatUserId);
}
