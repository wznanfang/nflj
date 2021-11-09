package com.wzp.nflj;

import com.wzp.nflj.model.Admin;
import com.wzp.nflj.util.Reflection.ReflectUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
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
        Map<String, String> tableName = ReflectUtils.getTableName(Admin.class);
        for (Map.Entry<String, String> entry : tableName.entrySet()) {
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            System.out.println(mapKey + "：" + mapValue);
        }
        Map<String, String>map = new HashMap<>();
        map.put("username","admin");
        map.put("username1","admin1");

        //获取@Id注解的主键id
        Field idField = ReflectUtils.getIdField(Admin.class);
        System.out.println(idField.getName());

        //获取Reflection反射注解标记的字段名
        Map<String, String> columnName = ReflectUtils.getColumnName(Admin.class);
        for (Map.Entry<String, String> entry : columnName.entrySet()) {
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            System.out.println(mapKey+":"+mapValue);
            for(Map.Entry<String, String> entry1 : map.entrySet()){
                if(mapKey.equals(entry1.getKey())){
                    String mapValue1 = entry1.getValue();
                    System.out.println(mapKey + "变更为：" + mapValue1);
                }
            }
        }
    }

}
