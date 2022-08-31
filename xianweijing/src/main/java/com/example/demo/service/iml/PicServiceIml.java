package com.example.demo.service.iml;

import com.example.demo.dao.PicRespository;
import com.example.demo.model.Picture;
import com.example.demo.service.PicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangduhuang
 * @date 2021/8/19 9:46
 */
@Service
public class PicServiceIml implements PicService {
    @Autowired
    PicRespository picRespository;

    @Override
    public List<Picture> getAllPic(String username) {
        return picRespository.findAll();

    }

    @Override
    public List<Picture> getResult(String username, int status,int save) {
        List<Picture> pictureList=picRespository.findByUsernameAndStatusAndSave(username,status,save);
        return pictureList;
    }

    @Override
    public Picture addPic(Picture picture) {
        Picture picture1=null;
        try{
            picture1=picRespository.saveAndFlush(picture);
        }catch (Exception e){
            return null;
        }
        return picture1;
    }

    @Override
    public Picture getPic(int pid) {
        Picture pic=picRespository.findById(pid).orElse(null);
        if(pic ==null ) return null;
        else return pic;
    }
}
