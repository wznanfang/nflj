package com.wzp.nflj.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zp.wei
 * @date 2022/2/28 11:25
 */
@ApiModel("系统日志VO")
@Data
public class SysLogVO implements Serializable {

    private static final long serialVersionUID = 3587814491755685583L;


    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty(value = "开始时间")
    private Long startTime;

    @ApiModelProperty(value = "结束时间")
    private Long endTime;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("请求类型")
    private String method;

    @ApiModelProperty("查询页数，从0开始")
    private Integer page;

    @ApiModelProperty("查询条数")
    private Integer size;

    @ApiModelProperty(value = "排序规则，可传入多个sort参数")
    private String sort;


}
