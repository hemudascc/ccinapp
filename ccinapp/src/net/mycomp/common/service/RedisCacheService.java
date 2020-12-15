package net.mycomp.common.service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

@Service("redisCacheService")
public class RedisCacheService {

	private static final Logger logger = Logger
			.getLogger(RedisCacheService.class);

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	@Qualifier(value = "redisTemplateInt")
	private RedisTemplate<String, Integer> redisTemplateInt;

	@Value("${redis.eviction.time}")
	private Integer redisCahcheEvictionTime;

	@Value("${redis.object.eviction.time}")
	private Integer redisObjectEvictionTime;

	HashOperations<String, String, String> hashOps;
	SetOperations<String, String> setOps;
	ValueOperations<String, String> valueOps;
	ValueOperations<String, Integer> valueIntOps;
	ValueOperations<String, Object> valueObjectOps;

	
	@PostConstruct
	public void init() {

		hashOps = stringRedisTemplate.opsForHash();
		setOps = stringRedisTemplate.opsForSet();
		valueOps = stringRedisTemplate.opsForValue();
		valueObjectOps = redisTemplate.opsForValue();
		redisTemplateInt.setKeySerializer(new StringRedisSerializer());
		redisTemplateInt.setValueSerializer(new GenericToStringSerializer<Integer>(
				Integer.class));
		valueIntOps = redisTemplateInt.opsForValue();
	}

	public boolean putIntValue(String key,int value) {
		if(key==null){
			return false;
		}
		try{
		 valueIntOps.set(key, value,redisCahcheEvictionTime, TimeUnit.MINUTES);
		return true;
		}catch(Exception ex){
			
		}
		return false;
	}
	
	public boolean putIntValue(String key,int value,int evictionMinute) {
		if(key==null){
			return false;
		}
		try{
		 valueIntOps.set(key, value,evictionMinute, TimeUnit.MINUTES);
		return true;
		}catch(Exception ex){
			
		}
		return false;
	}

	public Integer getIntValue(String key) {
		if(key==null){
			return 0;
		}
		return valueIntOps.get(key);		
	}
	
	public Long getAndIcrementIntValue(String key,int value) {
		return valueIntOps.increment(key, value);	
	}
	
	
	public boolean putAllValue(String key, Map<String, String> map) {

		hashOps.putAll(key, map);
		return true;
	}

	public boolean putValue(String hashMapName, String key, String value) {

		hashOps.put(hashMapName, key, value);
		return true;
	}

	public String getValue(String hashMapName, List<String> key) {
		String value = hashOps.size(hashMapName) + "    "
				+ hashOps.multiGet(hashMapName, key);
		return value;
	}

	public String getValueString(String key) {
		String value = valueOps.get(key);
		return value;
	}

	public void putCacheValue(String key, int i) {
		long time=System.currentTimeMillis();
		if(key==null){
			return;
		}
		try {

			setOps.add(key, String.valueOf(i));
			stringRedisTemplate.expire(key, redisCahcheEvictionTime,
					TimeUnit.MINUTES);
			logger.error("Total time::putCacheValue "+(System.currentTimeMillis()-time));
		} catch (Exception ex) {
			logger.error("putCacheValue::: exception " + ex);
		}
	}

	public Set<String> getCacheValue(String str) {
		long time=System.currentTimeMillis();
		try {
			return setOps.members(str);
		} catch (Exception ex) {
			logger.error("getCacheValue::: exception " + ex);
		}finally{
			logger.error("Total time::getCacheValue "+(System.currentTimeMillis()-time));
		}
		return null;
	}

	public void putTokenValueInCache(String token) {
		long time=System.currentTimeMillis();
		try {
		    valueOps.append(token, token);
			stringRedisTemplate.expire(token, 24,
					TimeUnit.HOURS);
		} catch (Exception ex) {
			logger.error("putTokenValueInCache::: exception " + ex);
		}finally{
			logger.error("Total time::putTokenValueInCache "+(System.currentTimeMillis()-time));
		}
	}

	public String getTokenCacheValue(String str) {
		long time=System.currentTimeMillis();
		try {
			return valueOps.get(str);
		} catch (Exception ex) {
			logger.error("getTokenCacheValue::: exception " + ex);
		}finally{
			logger.error("Total time::getTokenCacheValue "+(System.currentTimeMillis()-time));
		}
		return null;
	}

	public void putObjectCacheValueByEvictionMinute(String str, Object object,int minute) {
		long time=System.currentTimeMillis();
		try {
			valueObjectOps.set(str, object);
			redisTemplate
					.expire(str, minute, TimeUnit.MINUTES);
			// mcc.set(str, memcachedObjectEvictionTime, object);
		} catch (Exception ex) {
			logger.error("putObjectCacheValue::: exception ", ex);
		}finally{
			logger.error("Total time::putObjectCacheValue "+(System.currentTimeMillis()-time));
		}
	}

	public Object getObjectCacheValue(String str) {
		try {
			return valueObjectOps.get(str);
		} catch (Exception ex) {
			logger.error("getObjectCacheValue::: exception ", ex);
		}
		return null;
	}
	
	public void putObjectCacheValueByEvictionDay(String str, Object object,int evictionDay) {
		long time=System.currentTimeMillis();
		try {
			valueObjectOps.set(str, object);
			redisTemplate
					.expire(str, evictionDay, TimeUnit.DAYS);			
		} catch (Exception ex) {
			logger.error("putObjectCacheValueByEvictionDay::: exception ", ex);
		}finally{
			logger.error("Total time::putObjectCacheValueByEvictionDay "+(System.currentTimeMillis()-time));
		}
	}

	public Long getObjectCacheValueByEvictionSecond(String str, Long value,
			int evictionSecond) {
		long time = System.currentTimeMillis();
		Long cahcehValue=0L;
		try {			
			cahcehValue=valueObjectOps.increment(str, value);
			redisTemplate.expire(str, evictionSecond, TimeUnit.SECONDS);
		} catch (Exception ex) {
			logger.error("getObjectCacheValueByEvictionSecond::: exception ", ex);
		} finally {
			logger.info("Total time::getObjectCacheValueByEvictionSecond "
					+ (System.currentTimeMillis() - time));
		}
		return cahcehValue;
	}
	
	public boolean putObjectCacheValueByEvictionSecond(String str, Object value,
			int evictionSecond) {
		long time = System.currentTimeMillis();
		
		try {			
			valueObjectOps.set(str, value);
			redisTemplate.expire(str, evictionSecond, TimeUnit.SECONDS);
			return true;
		} catch (Exception ex) {
			logger.error("getObjectCacheValueByEvictionSecond::: exception ", ex);
		} finally {
			logger.info("Total time::getObjectCacheValueByEvictionSecond "
					+ (System.currentTimeMillis() - time));
		}
		return false;
	}

}
