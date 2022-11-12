package com.wzp.nflj;

import com.wzp.nflj.util.WaterMarkUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author zp.wei
 * @date 2022/11/12 11:43
 */
@SpringBootTest
public class AddWaterMarkTests {


    @Test
    void contextLoads() throws NoSuchMethodException {
    }


    @Test
    void test() throws Exception {
        Font font1 = new Font("微软雅黑", Font.PLAIN, 35); //水印字体 方法1
        // 方法2引用外部字体有可能会失败不起作用，建议使用方法3
//        File file = new File("E://包图小白体.ttf");
//        Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, file);
//        Font font2 = new Font(dynamicFont.getName(), Font.PLAIN, 35); //水印字体 方法2
//        Font font3 = dynamicFont.deriveFont(Font.PLAIN, 35); //水印字体 方法3
        String srcImgPath = "C:/Users/南方/Pictures/Camera Roll/白色.jpg"; //源图片地址
        String tarImgPath = "C:/Users/南方/Pictures/Camera Roll/" + System.currentTimeMillis() + ".jpg"; //待存储的地址
        String content = "不逆于物，进退沉浮";  //水印内容
        Color color = new Color(37, 139, 194); //水印图片色彩以及透明度
        WaterMarkUtil.addWaterMark(srcImgPath, tarImgPath, content,content,color, font1);
    }


}
