package com.wzp.nflj;

import com.wzp.nflj.model.Admin;
import com.wzp.nflj.util.Base64Util;
import com.wzp.nflj.util.reflection.ReflectUtil;
import com.wzp.nflj.util.rsaSign.RSAEncrypt;
import com.wzp.nflj.util.rsaSign.RSASignature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
        String filepath = "E:/";
        RSAEncrypt.genKeyPair(filepath);

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


    //继承Thread
    public class Threead extends Thread {

        public void run() {
            System.out.println(Thread.currentThread().getName() + "我是南方");
        }
    }

    //实现runnable接口
    public class Runnablee implements Runnable {

        public Runnablee(String s) {
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "我是南方");
        }
    }

    @Test
    void test4() {
//        Threead threead = new Threead();
//        threead.setName("南方");
//        threead.start();

//        Runnablee runnablee = new Runnablee();
//        new Thread(runnablee).start();

        //使用阿里巴巴推荐的创建线程池的方式
        //通过ThreadPoolExecutor构造函数自定义参数创建
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 6, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 10; i++) {
            //创建WorkerThread对象（WorkerThread类实现了Runnable 接口）
            Runnable runnablee = new Runnablee("" + i);
            //执行Runnable
            executor.execute(runnablee);
        }
        //终止线程池
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }


    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void test5() {
//        redisTemplate.opsForSet().add("keytest",1,2,3,4,1,2,3);
//        System.out.println(redisTemplate.opsForSet().members("keytest"));
//
//        redisTemplate.opsForZSet().add("成绩","张三",10);
//        redisTemplate.opsForZSet().add("成绩","李四",30);
//        redisTemplate.opsForZSet().add("成绩","王麻子",20);
//        redisTemplate.opsForZSet().add("成绩","南方",40);
//        redisTemplate.opsForZSet().add("成绩","枯叶",80);
//        redisTemplate.opsForZSet().add("成绩","落尽",70);
//        System.out.println(redisTemplate.opsForZSet().range("成绩",0,-1));
//        System.out.println(redisTemplate.opsForZSet().rangeByScore("成绩",40,80));
//        System.out.println(redisTemplate.opsForZSet().score("成绩","落尽"));


//        String a = "1";
//        String b = "2";
//        change(a,b);
//        System.out.println("a"+a);
//        System.out.println("b"+b);
//        int[] arr = { 1, 2, 3, 4, 5 };
//        System.out.println(arr[0]);
//        change(arr);
//        System.out.println(arr[0]);

        redisTemplate.opsForHash().put("1", 1, 1);
        System.out.println(redisTemplate.opsForHash().get("1", 1));
        redisTemplate.opsForHash().put("1", 2, 2);
        System.out.println(redisTemplate.opsForHash().get("1", 2));
        redisTemplate.opsForHash().put("1", 3, 3);
        System.out.println(redisTemplate.opsForHash().get("1", 3));
        redisTemplate.expire("1", 10, TimeUnit.SECONDS);
        Map<Integer, Integer> map = redisTemplate.opsForHash().entries("1");
        System.out.println(map);

    }

    public void change(String i, String j) {
        String temp = i;
        i = j;
        j = temp;
        System.out.println("i" + i);
        System.out.println("j" + j);

    }


    public void change(int[] array) {
        array[0] = 0;
    }


    @Test
    void test6() {
//        ArrayList<Integer>arrayList = new ArrayList<>();
//        System.out.println(arrayList.size());
//        arrayList.add(1);

//        HashMap<Integer, Integer> hashMap = new HashMap<>();
//        hashMap.put(1,1);
        Integer a = new Integer(1);
        Integer b = new Integer(1);
        System.out.println(a == b);
        Integer c = 1;
        Integer d = 1;
        System.out.println(c == d);

    }

}
