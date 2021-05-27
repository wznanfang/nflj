package com.wzp.nflj.service;


import com.wzp.nflj.model.Authority;
import com.wzp.nflj.model.BaseUser;
import com.wzp.nflj.model.Role;
import com.wzp.nflj.repository.AdminRepository;
import com.wzp.nflj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseUserServiceImpl implements BaseUserService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public BaseUser findByUsername(String username) {
        BaseUser baseUser = null;
        if (username.startsWith("admin:")) {
            username = username.substring(6);
            baseUser = adminRepository.findByUsername(username);
            String username1 = "admin:" + baseUser.getUsername();
            baseUser.setUsername(username1);
        } else {
            baseUser = userRepository.findByUsername(username);
        }
        return baseUser;
    }
}
