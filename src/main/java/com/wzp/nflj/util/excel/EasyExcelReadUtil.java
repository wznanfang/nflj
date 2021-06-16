package com.wzp.nflj.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.wzp.nflj.config.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zp.wei
 * @DATE: 2020/12/23 9:41
 */
@Slf4j
public class EasyExcelReadUtil<T> extends AnalysisEventListener<T> {

    /**
     * 每隔3000条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;

    List<T> list = new ArrayList<>();


    /**
     * 这个每一条数据解析都会来调用
     *
     * @param t
     * @param context
     */
    @Override
    public void invoke(T t, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(t));
        list.add(t);
        // 达到batchCount了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 存储数据到数据库
     */
    private void saveData() {
        log.info("共有{}条数据，开始存储数据库！", list.size());
        //不建议这样循环增加，如果是使用mybatis，则可自己写相关sql进行批量增加
        /*UserMapper userMapper = SpringContextUtil.getBean(UserMapper.class);
        list.forEach(userMapper::insertSelective);*/

        // 如果是使用mybatis-plus，则其service提供saveBatch的批量新增功能,如下：
        /*UserService userService = SpringContextUtil.getBean(UserService.class);
        userService.saveBatch(list);*/

        // 存储完成清理 list，避免数据重复
        list.clear();
        log.info("存储数据库成功！");
    }


}
