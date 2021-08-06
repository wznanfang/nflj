package com.wzp.nflj.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@ApiModel("角色VO")
@Setter
@Getter
public class RoleVO implements Serializable {
    private static final long serialVersionUID = 4823875530802467688L;

    @ApiModelProperty(value = "编号", required = true)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "权限ID集合")
    private List<Long> authorityIds;
}
