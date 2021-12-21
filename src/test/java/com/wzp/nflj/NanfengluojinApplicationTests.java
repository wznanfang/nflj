package com.wzp.nflj;

import com.wzp.nflj.model.Admin;
import com.wzp.nflj.util.Reflection.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class NanfengluojinApplicationTests {

    @Test
    void contextLoads() {
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
    void test1(){
        BigDecimal bigDecimal = new BigDecimal(999.998);
        BigDecimal bigDecimal1 = new BigDecimal(999.991);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        System.out.println(new BigDecimal(decimalFormat.format(bigDecimal)));
        System.out.println(new BigDecimal(decimalFormat.format(bigDecimal1)));
    }


    /**
     * 获取随机数
     */
    @Test
    void test2(){
        int aa = (int) ((Math.random()*9+1)*10000);
        System.out.println(aa);
    }

}
