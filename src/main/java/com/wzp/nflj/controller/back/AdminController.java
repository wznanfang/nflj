package com.wzp.nflj.controller.back;

import com.alibaba.excel.EasyExcel;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.wzp.nflj.config.BaseConfig;
import com.wzp.nflj.config.CustomConfig;
import com.wzp.nflj.enums.ResultEnum;
import com.wzp.nflj.model.Admin;
import com.wzp.nflj.model.Authority;
import com.wzp.nflj.model.QAdmin;
import com.wzp.nflj.model.Role;
import com.wzp.nflj.repository.AdminRepository;
import com.wzp.nflj.repository.AdminRoleRepository;
import com.wzp.nflj.repository.RoleAuthorityRepository;
import com.wzp.nflj.service.EasyExcelReadService;
import com.wzp.nflj.service.EasyExcelWriteService;
import com.wzp.nflj.util.DateUtil;
import com.wzp.nflj.util.IpUtil;
import com.wzp.nflj.util.ObjUtil;
import com.wzp.nflj.util.Result;
import com.wzp.nflj.util.excel.EasyExcelWriteUtil;
import com.wzp.nflj.vo.AdminVO;
import com.wzp.nflj.vo.IdVO;
import com.wzp.nflj.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wzp
 * @date 2021/5/14 10:25
 */
@Api(tags = "管理员用户管理")
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
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private EasyExcelWriteService easyExcelWriteService;


    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVO loginVO) {
        if (ObjUtil.isEmpty(loginVO.getUsername()) || ObjUtil.isEmpty(loginVO.getPassword())) {
            return Result.error(ResultEnum.LACK_NEEDS_PARAM);
        }
        Map<String, Object> map1 = new HashMap<>(2);
        String username = loginVO.getUsername();
        String password = loginVO.getPassword();
        Admin admin = adminRepository.findByUsername(username);
        if (admin == null) {
            return Result.error(ResultEnum.ERROR_USERNAME_OR_PASSWORD);
        }
        if (!admin.getEnabled()) {
            return Result.error(ResultEnum.USER_NOT_ENABLE);
        }
        boolean flag = passwordEncoder.matches(password, admin.getPassword());
        if (!flag) {
            return Result.error(ResultEnum.ERROR_USERNAME_OR_PASSWORD);
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
            return Result.error(ResultEnum.FORBIDDEN);
        }
        map1.put("admin", admin);
        map1.put("token", responseEntity.getBody());
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


    @ApiOperation("注册")
    @PostMapping("/register")
    @ApiOperationSupport(ignoreParameters = {"id", "roleIds", "enabled", "size", "page", "sort"})
    public Result<Admin> register(@RequestBody AdminVO adminVO) {
        if (ObjUtil.isEmpty(adminVO.getUsername()) || ObjUtil.isEmpty(adminVO.getPhone())) {
            return Result.error(ResultEnum.LACK_NEEDS_PARAM);
        }
        String username = adminVO.getUsername();
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null) {
            return Result.error(ResultEnum.HAS_USER);
        }
        admin = new Admin();
        admin.setUsername(username);
        admin.setPhone(adminVO.getPhone());
        admin.setPassword(new BCryptPasswordEncoder().encode(CustomConfig.originalPassword));
        String ip = IpUtil.getRealIp(request);
        admin.setRegisterIp(ip);
        adminRepository.save(admin);
        return Result.ok(admin);
    }


    @ApiOperation("删除")
    @PostMapping("delete")
    public Result delete(@RequestBody IdVO idVO) {
        if (ObjUtil.isEmpty(idVO.getId())) {
            return Result.error(ResultEnum.LACK_NEEDS_PARAM);
        }
        Optional<Admin> optional = adminRepository.findById(idVO.getId());
        Admin admin = optional.orElse(null);
        if (admin == null) {
            return Result.error(ResultEnum.PARAM_ERROR);
        }
        admin.setEnabled(false);
        adminRepository.save(admin);
        return Result.ok();
    }


    @ApiOperation("根据ID查询")
    @GetMapping("getOne")
    public Result getOne(IdVO idVO) {
        if (ObjUtil.isEmpty(idVO.getId())) {
            return Result.error(ResultEnum.LACK_NEEDS_PARAM);
        }
        Optional<Admin> optional = adminRepository.findById(idVO.getId());
        Admin admin = optional.orElse(null);
        if (admin == null) {
            return Result.error(ResultEnum.PARAM_ERROR);
        }
        return Result.ok(admin);
    }


    @ApiOperation("根据条件查询")
    @ApiOperationSupport(ignoreParameters = {"id", "roleIds", "enabled"})
    @GetMapping("findAll")
    public Result findAll(@PageableDefault Pageable pageable, AdminVO adminVO) {
        QAdmin qAdmin = QAdmin.admin;
        Predicate predicate = new BooleanBuilder();
        if (!ObjUtil.isEmpty(adminVO.getUsername())) {
            predicate = qAdmin.username.like("%" + adminVO.getUsername() + "%").and(predicate);
        }
        if (!ObjUtil.isEmpty(adminVO.getPhone())) {
            predicate = qAdmin.phone.like("%" + adminVO.getPhone() + "%").and(predicate);
        }
        Page<Admin> page = adminRepository.findAll(predicate, pageable);
        return Result.ok(page);
    }


    @ApiOperation("使用easyExcel导出到excel")
    @GetMapping("easyExcelDownload")
    public Result easyExcelDownload() {
        //获取总数据量
        Long totalNum = adminRepository.findCount();
        if (totalNum > 0) {
            String fileName = "系统用户表" + DateUtil.sysTime() + ".xlsx";
            //先保存到本地服务器
            boolean excelExport = easyExcelWriteService.adminExcelExport(totalNum, fileName);
            if (!excelExport) {
                return Result.error(ResultEnum.EXCEL_EXPORT_FAIL);
            }
            //再通过浏览器下载
            boolean excelDownload = new EasyExcelWriteUtil().downloadExcel(response, fileName);
            if (!excelDownload) {
                return Result.error(ResultEnum.EXCEL_DOWNLAND_FAIL);
            }
        }
        //如果这里有返回会导致 Cannot call sendError() after the response has been committed 错误
        // 原因在于response输出流已关闭，导致执行第二个输出时出现response被提交之后不能发送错误请求，故设置为 return null
        return null;
    }


    @ApiOperation("使用easyExcel导入到数据库")
    @ApiImplicitParam(name = "filename", value = "文件名", dataType = "string", paramType = "query", example = "G:/excel/1608702092807.xlsx")
    @GetMapping("/easyExcelImport")
    public Result easyExcelImport() {
        String filename = request.getParameter("filename");
        // 这里 需要指定用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        //读取多个sheet
        EasyExcel.read(filename, Admin.class, new EasyExcelReadService()).doReadAll();
        return Result.ok(ResultEnum.EXCEL_IMPORT_SUCCESS);
    }


}
