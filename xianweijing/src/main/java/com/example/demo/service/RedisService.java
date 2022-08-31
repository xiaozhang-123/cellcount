package com.example.demo.service;

/**
 * @author zhangduhuang
 * @date 2021/7/18 17:34
 */
public interface RedisService {
    public Object saveByTime(String key,String value,int time);
    public Object get(String key);
    public boolean save(String key,String value);
    public boolean remove(String key);
}
