package com.example.demo.dao;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangduhuang
 * @date 2021/7/17 23:35
 */
public interface UserRespository extends JpaRepository<User,Integer> {
    List<User> findByUsernameAndPassword(String username, String password);

    List<User> findByUsername(String username);
}
