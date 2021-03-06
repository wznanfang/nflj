package com.wzp.nflj;

import com.wzp.nflj.model.Admin;
import com.wzp.nflj.util.Base64Util;
import com.wzp.nflj.util.reflection.ReflectUtil;
import com.wzp.nflj.util.rsaSign.RSAEncrypt;
import com.wzp.nflj.util.rsaSign.RSASignature;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class NanfengluojinApplicationTests {

    @Test
    void contextLoads() throws NoSuchMethodException {
    }


    @Test
    public void test() {
        //获取@Table注解标记的表名
        Map<String, String> tableName = ReflectUtil.getTableName(Admin.class);
        for (Map.Entry<String, String> entry : tableName.entrySet()) {
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            System.out.println(mapKey + "：" + mapValue);
        }
        Map<String, String> map = new HashMap<>();
        map.put("username", "admin");
        map.put("username1", "admin1");

        //获取@Id注解的主键id
        Field idField = ReflectUtil.getIdField(Admin.class);
        System.out.println(idField.getName());

        //获取Reflection反射注解标记的字段名
        Map<String, String> columnName = ReflectUtil.getColumnName(Admin.class);
        for (Map.Entry<String, String> entry : columnName.entrySet()) {
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            System.out.println(mapKey + ":" + mapValue);
            for (Map.Entry<String, String> entry1 : map.entrySet()) {
                if (mapKey.equals(entry1.getKey())) {
                    String mapValue1 = entry1.getValue();
                    System.out.println(mapValue + "变更为：" + mapValue1);
                }
            }
        }
    }

    /**
     * BigDecimal 保留两位小数
     */
    @Test
    void test1() {
        BigDecimal bigDecimal = new BigDecimal(999.998);
        BigDecimal bigDecimal1 = new BigDecimal(999.991);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        System.out.println(new BigDecimal(decimalFormat.format(bigDecimal)));
        System.out.println(new BigDecimal(decimalFormat.format(bigDecimal1)));
        //四舍六入五考虑，五后非零就进一，五后皆零看奇偶，五前为偶应舍去，五前为奇要进一。
        BigDecimal b1 = new BigDecimal(1.2050);
        BigDecimal b2 = new BigDecimal(1.2150);
        BigDecimal b3 = new BigDecimal(1.2250);
        BigDecimal b4 = new BigDecimal(1.2550);
        BigDecimal b44 = new BigDecimal(1.2551);
        BigDecimal b5 = new BigDecimal(1.2650);
        BigDecimal i1 = b1.setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal i2 = b2.setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal i3 = b3.setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal i4 = b4.setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal i44 = b44.setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal i5 = b5.setScale(2, RoundingMode.HALF_EVEN);
        System.out.println(i1);
        System.out.println(i2);
        System.out.println(i3);
        System.out.println(i4);
        System.out.println(i44);
        System.out.println(i5);
    }


    /**
     * 获取随机数
     */
    @Test
    void test2() {
//        int aa = (int) ((Math.random() * 9 + 1) * 10000);
//        System.out.println(aa);
        System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        System.out.println(LocalDate.now().atStartOfDay());
        System.out.println(LocalDateTime.now().plusDays(-1).toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
    }


    /**
     * 测试公钥私钥加解密
     *
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        //生成公钥和私钥
        String filepath = "F:/";
//        RSAEncrypt.genKeyPair(filepath);

        System.out.println("--------------公钥加密私钥解密过程-------------------");
        String plainText = "ihep_666666666666";
        //公钥加密过程
        byte[] cipherData = RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(filepath)), plainText.getBytes());
        String cipher = Base64Util.encode(cipherData);
        //私钥解密过程
        byte[] res = RSAEncrypt.decrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(filepath)), Base64Util.decodeStr(cipher));
        String restr = new String(res);
        System.out.println("原文：" + plainText);
        System.out.println("加密：" + cipher);
        System.out.println("解密：" + restr);
        System.out.println();

        System.out.println("--------------私钥加密公钥解密过程-------------------");
        plainText = "ihep_私钥加密公钥解密";
        //私钥加密过程
        cipherData = RSAEncrypt.encrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(filepath)), plainText.getBytes());
        cipher = Base64Util.encode(cipherData);
        //公钥解密过程
        res = RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(filepath)), Base64Util.decodeStr(cipher));
        restr = new String(res);
        System.out.println("原文：" + plainText);
        System.out.println("加密：" + cipher);
        System.out.println("解密：" + restr);
        System.out.println();

        System.out.println("---------------私钥签名过程------------------");
        String content = "ihep_这是用于签名的原始数据";
        System.out.println("签名原串：" + content);
        String signstr = RSASignature.sign(content, RSAEncrypt.loadPrivateKeyByFile(filepath));
        System.out.println("签名串：" + signstr);
        System.out.println("---------------公钥校验签名------------------");
        System.out.println("验签结果：" + RSASignature.doCheck(content, signstr, RSAEncrypt.loadPublicKeyByFile(filepath)));
        System.out.println();

    }

}
