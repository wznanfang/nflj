package com.wzp.nflj.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author wzp
 * @Date 2021/4/20 11:34
 **/
@ApiModel("后台用户VO")
@Data
public class AdminVO implements Serializable {
    private static final long serialVersionUID = 7529254143635526488L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "角色ID集合")
    private List<Long> roleIds;

    @ApiModelProperty(value = "激活")
    private Boolean enabled;

    @ApiModelProperty(value = "每页显示条数")
    private Integer size;

    @ApiModelProperty(value = "页数，从0开始")
    private Integer page;

    @ApiModelProperty(value = "排序规则，可传入多个sort参数")
    private String sort;



}
