package com.wzp.nflj.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @author zp.wei
 * @date 2022/2/25 15:27
 */
@Entity
@Getter
@Setter
@ApiModel("日志")
public class SysLog extends Model{

    private static final long serialVersionUID = 6191956001380147414L;


    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("消耗时间")
    private Long spendTime;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("urlPath")
    private String urlPath;

    @ApiModelProperty("请求类型")
    private String method;

    @ApiModelProperty("IP地址")
    private String ip;

    @ApiModelProperty("请求参数")
    private String parameter;


}
