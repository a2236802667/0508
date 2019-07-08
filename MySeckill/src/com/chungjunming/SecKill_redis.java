package com.chungjunming;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

public class SecKill_redis {
	
	public static boolean doSecKill(String uid,String prodid) {
		
		String productKey = "sk:" + prodid + ":product";
		String userKey = "sk:" + prodid + ":user";
		
		JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
		
		Jedis jedis = jedisPool.getResource();
		
		if(jedis.sismember(userKey, uid)) {
			System.err.println(uid + "已经秒杀过了");
			jedis.close();
			return false;
		}
		
		jedis.watch(productKey);
		
		String store = jedis.get(productKey);
		
		if(store == null) {
			System.err.println("商家尚未上架" + prodid + "产品");
			jedis.close();
			return false;
		}
		
		int store_int = Integer.parseInt(store);
		
		if(store_int <= 0) {
			System.err.println(prodid + "已经秒完了");
			jedis.close();
			return false;
		}
		
		Transaction multi = jedis.multi();
		
		multi.sadd(userKey, uid);
		multi.decr(productKey);
		
		List<Object> result = multi.exec();
		
		if(result == null || result.size() < 2) {
			System.err.println(uid + "秒杀失败");
			jedis.close();
			return false;
		}
		
		System.out.println(uid + "成功秒杀到了产品");
		
		jedis.close();
		return true;
	}
	
}
