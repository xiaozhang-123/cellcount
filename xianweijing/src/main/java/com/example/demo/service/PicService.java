package com.example.demo.service;

import com.example.demo.model.Picture;

import java.util.List;

/**
 * @author zhangduhuang
 * @date 2021/8/19 9:36
 */
public interface PicService {
    public List<Picture> getAllPic(String username);
    public List<Picture> getResult(String username,int status,int save);
    public Picture addPic(Picture picture);
    public Picture getPic(int pid);
}
