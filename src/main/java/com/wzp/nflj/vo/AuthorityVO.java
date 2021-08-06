package com.wzp.nflj.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("权限VO")
public class AuthorityVO implements Serializable {
    private static final long serialVersionUID = 2521001915781187402L;

    @ApiModelProperty(value = "编号", required = true)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "权限码")
    private Long idCode;
}
