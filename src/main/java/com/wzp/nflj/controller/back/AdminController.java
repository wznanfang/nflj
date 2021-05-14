package com.wzp.nflj.controller.back;

import com.wzp.nflj.config.BaseConfig;
import com.wzp.nflj.enums.ResultCodeEnum;
import com.wzp.nflj.model.Admin;
import com.wzp.nflj.model.Authority;
import com.wzp.nflj.model.Role;
import com.wzp.nflj.repository.*;
import com.wzp.nflj.util.DateUtil;
import com.wzp.nflj.util.IpUtil;
import com.wzp.nflj.util.ObjUtil;
import com.wzp.nflj.util.Result;
import com.wzp.nflj.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wzp
 * @date 2021/5/14 10:25
 */
@Api(tags = "管理员户管理")
@Slf4j
@RestController
@RequestMapping("/back/admin")
public class AdminController extends BaseConfig {

    @Resource
    private AdminRepository adminRepository;
    @Resource
    private AdminRoleRepository adminRoleRepository;
    @Resource
    private RoleAuthorityRepository roleAuthorityRepository;


    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVO loginVO) {
        if (ObjUtil.isEmpty(loginVO.getUsername()) || StringUtils.isEmpty(loginVO.getPassword())) {
            return Result.error(ResultCodeEnum.LACK_NEEDS_PARAM);
        }
        Map<String, Object> map1 = new HashMap<>(2);
        String username = loginVO.getUsername();
        String password = loginVO.getPassword();
        Admin admin = adminRepository.findByUsername(username);
        if (admin == null) {
            return Result.error(ResultCodeEnum.ERROR_USERNAME_OR_PASSWORD);
        }
        if (!admin.getEnabled()) {
            return Result.error(ResultCodeEnum.USER_NOT_ENABLE);
        }
        boolean flag = new BCryptPasswordEncoder().matches(password, admin.getPassword());
        if (!flag) {
            return Result.error(ResultCodeEnum.ERROR_USERNAME_OR_PASSWORD);
        }
        String ip = IpUtil.getRealIp(request);
        admin.setLastLoginIp(ip);
        admin.setLastLoginTime(DateUtil.sysTime());
        adminRepository.save(admin);
        //获取权限
        List<Authority> list = findAuthority(admin);
        admin.setAuthorityList(list);
        //获取token
        ResponseEntity<OAuth2AccessToken> responseEntity = getToken(username, password);
        if (responseEntity == null) {
            return Result.error(ResultCodeEnum.FORBIDDEN);
        }
        map1.put("admin", admin);
        map1.put("token", responseEntity);
        return Result.ok(map1);
    }

    private List<Authority> findAuthority(Admin admin) {
        List<Authority> authorities = new ArrayList<>();
        List<Role> roles = adminRoleRepository.findRoleByAdmin(admin);
        if (roles != null && !roles.isEmpty()) {
            authorities.addAll(roleAuthorityRepository.findAuthorityByRoleIn(roles));
        }
        return authorities;
    }
}
