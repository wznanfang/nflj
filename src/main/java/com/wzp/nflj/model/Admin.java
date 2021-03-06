package com.wzp.nflj.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author wzp
 * @Date 2021/4/20 11:34
 **/
@ApiModel("管理员用户")
@Setter
@Getter
@Entity
@Table(name = "admin")
public class Admin extends BaseUser {
    private static final long serialVersionUID = 8255398851069228829L;

}
