package com.example.demo.config;
import lombok.Getter;
/**
 * @author zhangduhuang
 * @date 2021/7/17 23:36
 */


public enum ProjectExceptionType {
    SYSTEM_ERROR (500,"Server Error"),
    OTHER_ERROR(999,"Unkown Error");
    ProjectExceptionType(int code, String type){
        this.code=code;
        this.type=type;
    }
    @Getter
    private int code;
    @Getter
    private String type;
}
