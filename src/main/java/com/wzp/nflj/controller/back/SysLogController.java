package com.wzp.nflj.controller.back;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.wzp.nflj.config.BaseConfig;
import com.wzp.nflj.model.QSysLog;
import com.wzp.nflj.model.SysLog;
import com.wzp.nflj.repository.SysLogRepository;
import com.wzp.nflj.util.ObjUtil;
import com.wzp.nflj.util.Result;
import com.wzp.nflj.vo.SysLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zp.wei
 * @date 2022/2/28 11:13
 */
@Api(tags = "系统日志管理")
@Slf4j
@RestController
@RequestMapping("/back/sysLog")
public class SysLogController extends BaseConfig {

    @Resource
    private SysLogRepository sysLogRepository;


    @ApiOperation("条件查询")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @GetMapping("/findAll")
    public Result<Page<SysLog>> findAll(@PageableDefault Pageable pageable, SysLogVO sysLogVO) {
        QSysLog qSysLog = QSysLog.sysLog;
        Predicate predicate = new BooleanBuilder();
        if (!ObjUtil.isEmpty(sysLogVO.getUsername())) {
            predicate = qSysLog.username.like("%" + sysLogVO.getUsername() + "%").and(predicate);
        }
        if (!ObjUtil.isEmpty(sysLogVO.getStartTime())) {
            Long startTime = sysLogVO.getStartTime();
            predicate = qSysLog.createdAt.goe(startTime).and(predicate);
        }
        if (!ObjUtil.isEmpty(sysLogVO.getEndTime())) {
            Long endTime = sysLogVO.getEndTime();
            predicate = qSysLog.createdAt.loe(endTime).and(predicate);
        }
        if (!ObjUtil.isEmpty(sysLogVO.getMethod())) {
            predicate = qSysLog.username.eq(sysLogVO.getMethod()).and(predicate);
        }
        Page<SysLog> page = sysLogRepository.findAll(predicate, pageable);
        return Result.ok(page);
    }


}
