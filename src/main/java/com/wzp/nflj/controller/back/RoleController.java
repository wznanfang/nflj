package com.wzp.nflj.controller.back;


import com.wzp.nflj.config.BaseConfig;
import com.wzp.nflj.enums.ResultCodeEnum;
import com.wzp.nflj.model.Admin;
import com.wzp.nflj.model.Authority;
import com.wzp.nflj.model.Role;
import com.wzp.nflj.model.RoleAuthority;
import com.wzp.nflj.repository.*;
import com.wzp.nflj.util.ObjUtil;
import com.wzp.nflj.util.Result;
import com.wzp.nflj.vo.IdVO;
import com.wzp.nflj.vo.RoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: zp.wei
 * @DATE: 2020/7/3 17:26
 */
@Api(tags = "角色相关接口")
@RestController
@RequestMapping("back/role")
public class RoleController extends BaseConfig {

    @Resource
    private AuthorityRepository authorityRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private RoleAuthorityRepository roleAuthorityRepository;
    @Resource
    private AdminRoleRepository adminRoleRepository;
    @Resource
    private AdminRepository adminRepository;


    @ApiOperation("角色新增")
    @PostMapping("save")
    public Result<Role> save(@RequestBody Role role) {
        Role role1 = roleRepository.save(role);
        return Result.ok(role1);
    }


    @ApiOperation("角色删除")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("delete")
    public Result delete(@RequestBody IdVO idVO) {
        if (ObjUtil.isEmpty(idVO.getId())) {
            return Result.error(ResultCodeEnum.LACK_NEEDS_PARAM);
        }
        Long id = idVO.getId();
        Optional<Role> optional = roleRepository.findById(id);
        if (!optional.isPresent()) {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
        roleAuthorityRepository.deleteAllByRole(optional.get());
        adminRoleRepository.deleteAllByRole(optional.get());
        roleRepository.delete(optional.get());
        return Result.ok();
    }


    @ApiOperation("角色更新")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("update")
    public Result<Role> update(@RequestBody RoleVO roleVO) {
        if (ObjUtil.isEmpty(roleVO.getId())) {
            return Result.error(ResultCodeEnum.LACK_NEEDS_PARAM);
        }
        Long id = roleVO.getId();
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (!optionalRole.isPresent()) {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
        Role role = optionalRole.get();
        if (!ObjUtil.isEmpty(roleVO.getName())) {
            role.setName(roleVO.getName());
        }
        if (!ObjUtil.isEmpty(roleVO.getDescription())) {
            role.setDescription(roleVO.getDescription());
        }
        if (ObjUtil.isEmptyList(roleVO.getAuthorityIds())) {
            roleAuthorityRepository.deleteAllByRole(role);
            List<Long> authorityIds = roleVO.getAuthorityIds();
            List<RoleAuthority> roleAuthorities = new ArrayList<>();
            authorityIds.forEach(authorityId -> {
                Optional<Authority> optional = authorityRepository.findById(authorityId);
                if (optional.isPresent()) {
                    RoleAuthority roleAuthority = new RoleAuthority();
                    roleAuthority.setAuthority(optional.get());
                    roleAuthority.setRole(role);
                    roleAuthorities.add(roleAuthority);
                }
            });
            roleAuthorityRepository.saveAll(roleAuthorities);
        }
        roleRepository.save(role);
        return Result.ok(role);
    }


    @ApiOperation("根据用户获取角色")
    @ApiImplicitParam(name = "adminId", paramType = "query", dataType = "Long", example = "1", required = true, value = "后台用户id")
    @GetMapping("findByAdmin")
    public Result<List<Role>> findByAdmin(@RequestParam Long adminId) {
        if (ObjUtil.isEmpty(adminId)) {
            return Result.error(ResultCodeEnum.LACK_NEEDS_PARAM);
        }
        Long id = Long.valueOf(request.getParameter("adminId"));
        Optional<Admin> optional = adminRepository.findById(id);
        if (!optional.isPresent()) {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
        List<Role> list = adminRoleRepository.findRoleByAdmin(optional.get());
        return Result.ok(list);
    }


    @ApiOperation("获取所有角色")
    @GetMapping("findAll")
    public Result<List<Role>> findAll() {
        List<Role> list = roleRepository.findAll();
        return Result.ok(list);
    }


}
