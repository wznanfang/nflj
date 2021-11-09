package com.wzp.nflj.util.Reflection;

import java.lang.annotation.*;

/**
 * @author zp.wei
 * @date 2021/11/9 9:51
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Reflection {

    /**
     * 名字
     *
     * @return
     */
    String name();


}
