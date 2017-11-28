package com.netty.server.netty.util;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service 
public class RedisDao {
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Resource(name="stringRedisTemplate")
	ValueOperations<String,String> valOpsStr;
	
	public void saveString(String key,String value){
		valOpsStr.set(key, value);
	}

	public String getString(String key){
		return valOpsStr.get(key);
	}
	
	public void deleteString(String key){
		stringRedisTemplate.delete(key);
	}
}
