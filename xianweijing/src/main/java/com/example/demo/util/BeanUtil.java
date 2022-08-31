package com.example.demo.util;
import org.springframework.beans.BeanUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangduhuang
 * @date 2021/7/18 18:59
 */
public class BeanUtil {//将实体转化为java VO视图类
    public static <T> T copy(Object poObj,final Class <T>voClass)
    {
        T voObj =null;
        try {
            voObj = voClass.newInstance();
            BeanUtils.copyProperties(poObj, voObj);
            return voObj;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> List <T> copyList(List <? extends Object> poList ,final Class <T>voClass){

        List<T> voList=new ArrayList<T>();

        T voObj =null;
        for(Object poObj:poList){
            try {
                voObj = voClass.newInstance();
                BeanUtils.copyProperties(poObj, voObj);
                voList.add(voObj);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            System.out.println(voObj);
        }
        return voList;
    }
}
