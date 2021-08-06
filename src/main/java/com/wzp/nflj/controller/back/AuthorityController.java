package com.wzp.nflj.controller.back;

import com.wzp.nflj.config.BaseConfig;
import com.wzp.nflj.enums.ResultCodeEnum;
import com.wzp.nflj.model.Authority;
import com.wzp.nflj.model.Role;
import com.wzp.nflj.repository.AuthorityRepository;
import com.wzp.nflj.repository.RoleAuthorityRepository;
import com.wzp.nflj.repository.RoleRepository;
import com.wzp.nflj.util.ObjUtil;
import com.wzp.nflj.util.Result;
import com.wzp.nflj.vo.AuthorityVO;
import com.wzp.nflj.vo.IdVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @Author: zp.wei
 * @DATE: 2020/7/3 16:16
 */
@Api(tags = "权限相关接口")
@RestController
@RequestMapping("back/authority")
public class AuthorityController extends BaseConfig {

    @Resource
    private AuthorityRepository authorityRepository;
    @Resource
    private RoleAuthorityRepository roleAuthorityRepository;
    @Resource
    private RoleRepository roleRepository;


    @ApiOperation("权限新增")
    @PostMapping("save")
    public Result<Authority> save(@RequestBody Authority authority) {
        if (ObjUtil.isEmpty(authority.getIdCode())) {
            return Result.error(ResultCodeEnum.LACK_NEEDS_PARAM);
        }
        Long idCode = authority.getIdCode();
        if (authorityRepository.findByIdCode(idCode) != null) {
            return Result.error(ResultCodeEnum.ID_CODE_EXISTENT);
        }
        Authority authority1 = authorityRepository.save(authority);
        return Result.ok(authority1);
    }


    @ApiOperation("根据ID删除权限")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("delete")
    public Result delete(@RequestBody IdVO idVO) {
        if (ObjUtil.isEmpty(idVO.getId())) {
            return Result.error(ResultCodeEnum.LACK_NEEDS_PARAM);
        }
        Long id = idVO.getId();
        Optional<Authority> optional = authorityRepository.findById(id);
        if (!optional.isPresent()) {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
        roleAuthorityRepository.deleteAllByAuthority(optional.get());
        authorityRepository.delete(optional.get());
        return Result.ok();
    }


    @ApiOperation("权限更新")
    @PostMapping("update")
    public Result<Authority> update(@RequestBody AuthorityVO authorityVO) {
        if (ObjUtil.isEmpty(authorityVO.getId())) {
            return Result.error(ResultCodeEnum.LACK_NEEDS_PARAM);
        }
        Long id = authorityVO.getId();
        Optional<Authority> optional = authorityRepository.findById(id);
        if (!optional.isPresent()) {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
        Authority authorityObj = optional.get();
        if (!ObjUtil.isEmpty(authorityVO.getName())) {
            String name = authorityVO.getName();
            authorityObj.setName(name);
        }
        if (!ObjUtil.isEmpty(authorityVO.getDescription())) {
            String description = authorityVO.getDescription();
            authorityObj.setDescription(description);
        }
        if (!ObjUtil.isEmpty(authorityVO.getValue())) {
            String value = authorityVO.getValue();
            authorityObj.setVal(value);
        }
        if (!ObjUtil.isEmpty(authorityVO.getIdCode())) {
            Long idCode = authorityVO.getIdCode();
            if (authorityRepository.findByIdCode(idCode) != null) {
                return Result.error(ResultCodeEnum.ID_CODE_EXISTENT);
            }
            authorityObj.setIdCode(idCode);
        }
        authorityRepository.save(authorityObj);
        return Result.ok(authorityObj);
    }


    @ApiOperation("根据角色获取权限")
    @ApiImplicitParam(name = "roleId", paramType = "query", dataType = "Long", example = "1", required = true, value = "角色id")
    @GetMapping("findByRole")
    public Result<List<Authority>> findByRole(@RequestParam Long roleId) {
        if (ObjUtil.isEmpty(roleId)) {
            return Result.error(ResultCodeEnum.LACK_NEEDS_PARAM);
        }
        Long id = Long.valueOf(request.getParameter("roleId"));
        Optional<Role> optional = roleRepository.findById(id);
        if (!optional.isPresent()) {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
        Role role = optional.get();
        List<Authority> list = roleAuthorityRepository.findAuthorityByRole(role);
        return Result.ok(list);
    }


    @ApiOperation("获取所有权限")
    @GetMapping("findAll")
    public Result<List<Authority>> findAll() {
        List<Authority> list = authorityRepository.findAll();
        return Result.ok(list);
    }


}
