package com.example.demo.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zhangduhuang
 * @date 2021/7/17 23:35
 */
@ApiModel(description = "Json返回体返回内容")
@Data
public class AjaxResponse {
    @ApiModelProperty(value = "返回状况描述信息")
    private  boolean isok;
    @ApiModelProperty(value = "返回自定义状态码")
    private int code;
    @ApiModelProperty(value = "返回情况提示信息")
    private String message;
    @ApiModelProperty(value = "返回包含对象内容")
    private Object data;
    @ApiModelProperty(value = "返回时间戳")
    private Date timeStamp;
    private AjaxResponse(){

    }

    static com.example.demo.config.AjaxResponse error(ProjectExceptionType e){
        com.example.demo.config.AjaxResponse resultBean=new com.example.demo.config.AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setCode(e.getCode());
        resultBean.setTimeStamp(new Date());
        resultBean.setMessage("联系管理员");
        return resultBean;
    }

    public static com.example.demo.config.AjaxResponse success(){
        com.example.demo.config.AjaxResponse resultBean =new com.example.demo.config.AjaxResponse();
        resultBean.setIsok(true);
        resultBean.setCode(200);
        resultBean.setMessage("ok");
        resultBean.setTimeStamp(new Date());
        return resultBean;
    }

    public static com.example.demo.config.AjaxResponse success(Object data) {
        com.example.demo.config.AjaxResponse resultBean = new com.example.demo.config.AjaxResponse();
        resultBean.setIsok(true);
        resultBean.setCode(200);
        resultBean.setMessage("OK");
        resultBean.setData(data);
        resultBean.setTimeStamp(new Date());
        return resultBean;
    }

    public static AjaxResponse successByMes(String message){
        com.example.demo.config.AjaxResponse resultBean = new com.example.demo.config.AjaxResponse();
        resultBean.setIsok(true);
        resultBean.setCode(200);
        resultBean.setMessage(message);
        resultBean.setTimeStamp(new Date());
        return resultBean;
    }

    public static AjaxResponse falseResult(String message){
        AjaxResponse resultBean=new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setCode(400);
        resultBean.setMessage( message);
        return  resultBean;
    }

    public static AjaxResponse failure(Object data){
        AjaxResponse resultBean=new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setCode(400);
        resultBean.setData(data);
        return  resultBean;
    }
    public static AjaxResponse falseByCode(int code,Object data,String message){
        AjaxResponse responseBean=new AjaxResponse();
        responseBean.setData(data);
        responseBean.setIsok(false);
        responseBean.setCode(code);
        responseBean.setMessage(message);
        return responseBean;
    }
}
