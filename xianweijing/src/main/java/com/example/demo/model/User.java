package com.example.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhangduhuang
 * @date 2021/7/17 23:35
 */
@ApiModel(description = "记录用户账号信息")
@Entity
@Table(name = "user")
@Data
public class User {
    @ApiModelProperty(value = "用户id")
    @Id
    private int id;
    @ApiModelProperty(value = "用户名")
    @Column(name="username")
    private String username;
    @ApiModelProperty(value = "用户密码")
    @Column(name="password")
    private String password;
    @ApiModelProperty(value = "用户邮箱")
    @Column(name="email")
    private String email;
    @ApiModelProperty(value = "用户电话")
    @Column(name="phone")
    private String phone;
    @ApiModelProperty(value = "用户激活状态")
    @Column(name="active")
    private int active;

    @ApiModelProperty(value = "用户昵称")
    @Column(name="nickname")
    private String nickname;

    @ApiModelProperty(value = "用户单位")
    @Column(name="unit")
    private String unit;

    @ApiModelProperty(value = "用户支付宝账号")
    @Column(name="alipay_account")
    private String alipay_account;

    @ApiModelProperty(value = "用户奖励金额")
    @Column(name="money")
    private float money;
    public User(String username,String password,String email,String phone,String nickname,String unit,String alipay_account){
        this.username=username;
        this.password=password;
        this.email=email;
        this.phone=phone;
        this.nickname=nickname;
        this.unit=unit;
        this.alipay_account=alipay_account;
    }
    public User(){

    }
}
