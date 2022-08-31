package com.example.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author zhangduhuang
 * @date 2021/8/19 9:20
 */
@ApiModel(description = "记录显微镜分析信息")
@Entity
@Table(name = "picture")
@Data
public class Picture {
    @ApiModelProperty(value = "图片id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid;
    @ApiModelProperty(value = "图片主题")
    @Column(name="theme")
    private String theme;
    @ApiModelProperty(value = "图片备注")
    @Column(name="picdesc")
    private String picdesc;
    @ApiModelProperty(value = "文件路径")
    @Column(name="path")
    private String path;
    @ApiModelProperty(value = "用户名")
    @Column(name="username")
    private String username;
    @ApiModelProperty(value = "图片状态")
    @Column(name="status")
    private int status; //status为处理结果已经出
    @ApiModelProperty(value = "死亡细胞数")
    @Column(name="deadnum")
    private int deadnum;
    @ApiModelProperty(value = "存活细胞数")
    @Column(name="alivenum")
    private int alivenum;
    @ApiModelProperty(value = "图片存储状态")
    @Column(name="save")
    private int save;
}
