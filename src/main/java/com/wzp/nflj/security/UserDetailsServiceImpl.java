package com.wzp.nflj.security;

import com.wzp.nflj.model.BaseUser;
import com.wzp.nflj.service.BaseUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;

/**
 * 验证用户
 *
 * @Author: zp.wei
 * @DATE: 2020/8/31 14:51
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private BaseUserService baseUserServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseUser realUser = baseUserServiceImpl.findByUsername(username);
        if (realUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return realUser;
    }
}
