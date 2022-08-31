package com.example.demo.service.iml;

import com.example.demo.dao.UserRespository;
import com.example.demo.model.User;
import com.example.demo.service.RedisService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangduhuang
 * @date 2021/7/17 23:35
 */
@Slf4j
@Service
public class UserServiceIml implements UserService {
    @Autowired
    UserRespository userRespository;
   @Autowired
   RedisService redisService;

    @Override
    public boolean checkLogin(String username, String password) {
        List<User> userList=userRespository.findByUsername(username);
        if(userList==null && userList.size()<1){
            return false;
        }
        User user=userList.get(0);
        log.info("登录情况追踪");
        if(username.equals(user.getUsername()) && password.equals(user.getPassword())) return true;
        return false;
    }

    @Override
    public boolean addUser(User user) {
       if(user==null) return false;
       userRespository.save(user);
       log.info("添加用户");
       return true;
    }

    @Override
    public String activeUser(String username,int activeCode) {

        Object obj= redisService.get(username);
        if(obj==null) return "验证码超时或者未进行验证";
        int num=Integer.parseInt(obj.toString());
        if(num!=activeCode) return "验证失败";

        return "ok";
    }

    @Override
    public User getUser(String username) {
        List<User> userList=userRespository.findByUsername(username);
        if(userList==null && userList.size()<1){
            return null;
        }
        return userList.get(0);
    }

    @Override
    public User getUserById(int id) {
        User result=userRespository.findById(id).orElse(null);
        return result;
    }

    @Override
    public boolean findActive(String username) {
        List<User> userList=userRespository.findByUsername(username);
        if(userList==null && userList.size()<1){
            return false;
        }else if(userList.get(0).getActive()>-1) return true;


        return false;
    }

    @Override
    public boolean uniqueUser(String username) {
        List<User> userList=userRespository.findByUsername(username);
        if(userList==null && userList.size()<1){
            return false;
        }else if(userList.size()==1) return true;
        return false;

    }


}
