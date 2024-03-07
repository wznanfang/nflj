package com.wzp.nflj.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;

/**
 * md5地址工具类
 *
 * @Author: zp.wei
 * @DATE: 2020/10/21 10:45
 */
public class MD5Util {

    /**
     * MD5加密
     *
     * @param str 要加密的值
     * @param salt
     * @return
     */
    public static String encode(String str, String salt) {
        str = str + salt;
        return encode(str);
    }


    public static String encode(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++){
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


    /**
     * 文件类取MD5
     *
     * @param file
     * @return
     */
    public static String calcMD5(File file) {
        try (InputStream stream = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {
            return calcMD5(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 输入流取MD5
     *
     * @param stream
     * @return
     */
    public static String calcMD5(InputStream stream) {
        MessageDigest md5 = null;
        byte[] byteArray = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byteArray = toByteArray(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
