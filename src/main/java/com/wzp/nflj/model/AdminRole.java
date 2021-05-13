package com.wzp.nflj.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * 管理员角色关系表
 */
@Entity
@Getter
@Setter
public class AdminRole extends Model {

    private static final long serialVersionUID = 5186818311630146999L;

    @ManyToOne
    private Admin admin;

    @ManyToOne
    private Role role;
}
