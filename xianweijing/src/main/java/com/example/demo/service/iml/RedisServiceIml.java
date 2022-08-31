package com.example.demo.service.iml;

import com.example.demo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangduhuang
 * @date 2021/7/18 0:29
 */
@Service
public class RedisServiceIml implements RedisService {
    @Resource
    private  RedisTemplate<String,String> redisTemplate;



    public boolean save(String key,String value){
        try{
            redisTemplate.setKeySerializer(new StringRedisSerializer());//设置key的序列化
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            redisTemplate.afterPropertiesSet();
            ValueOperations<String,String> operations=redisTemplate.opsForValue();
            operations.set(key,value);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public Object get(String key){
        Object o = redisTemplate.opsForValue().get(key);
        return o;
    }
    public Object saveByTime(String key,String value,int time){
        try{
            redisTemplate.setKeySerializer(new StringRedisSerializer());//设置key的序列化
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            redisTemplate.afterPropertiesSet();
            ValueOperations<String,String> operations=redisTemplate.opsForValue();
            operations.set(key,value,60, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public boolean remove(String key){
        boolean flag=redisTemplate.delete(key);
        return flag;
    }

    public boolean isExist(String key){
        return redisTemplate.hasKey(key);
    }
}
