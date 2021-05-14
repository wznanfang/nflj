package com.wzp.nflj.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zp.wei
 * @DATE: 2020/8/28 16:57
 */
@ApiModel("登录VO")
@Data
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 10300087461480759L;

    @ApiModelProperty(value = "用户名", example = "admin", required = true)
    private String username;

    @ApiModelProperty(value = "密码", example = "e10adc3949ba59abbe56e057f20f883e", required = true)
    private String password;

}
