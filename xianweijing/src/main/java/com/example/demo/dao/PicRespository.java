package com.example.demo.dao;

import com.example.demo.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangduhuang
 * @date 2021/8/19 9:46
 */
public interface PicRespository extends JpaRepository<Picture,Integer> {
    List<Picture> findByUsernameAndStatusAndSave(String username,int status,int save);

}
