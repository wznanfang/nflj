package com.wzp.nflj.repository;

import com.wzp.nflj.model.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author zp.wei
 * @date 2022/2/25 16:36
 */
@Repository
public interface SysLogRepository extends JpaRepository<SysLog,Long>, QuerydslPredicateExecutor {
}
