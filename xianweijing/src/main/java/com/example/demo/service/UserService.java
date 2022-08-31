package com.example.demo.service;

import com.example.demo.model.User;

/**
 * @author zhangduhuang
 * @date 2021/7/17 23:35
 */
public interface UserService {
    public boolean checkLogin(String username,String password);
    public boolean addUser(User user);
    public String activeUser(String username,int activeCode);
    public User getUser(String username);
    public User getUserById(int id);
    public boolean findActive(String username);
    public boolean uniqueUser(String username);
}
