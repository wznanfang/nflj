package com.wzp.nflj.util;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64工具类
 *
 * @author zp.wei
 */
public class Base64Util {

    /**
     * 编码
     *
     * @param str 要编码的字符串
     * @return
     */
    public static String encode(String str) {
        return Base64.encodeBase64String(str.getBytes());
    }

    /**
     * 解码
     *
     * @param str 要解码的字符串
     * @return
     */
    public static String decode(String str) {
        byte[] decodeBase64 = Base64.decodeBase64(str);
        return new String(decodeBase64);
    }

}
