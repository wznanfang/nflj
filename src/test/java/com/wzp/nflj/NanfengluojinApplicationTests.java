package com.wzp.nflj;

import com.wzp.nflj.model.Admin;
import com.wzp.nflj.util.DateUtil;
import com.wzp.nflj.util.Reflection.ReflectUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class NanfengluojinApplicationTests {

    @Test
    void contextLoads() {
       System.out.println( DateUtil.firstDayZero());
       System.out.println( DateUtil.lastDayZero());
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
        int aa = (int) ((Math.random() * 9 + 1) * 10000);
        System.out.println(aa);
    }

}
