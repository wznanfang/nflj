package com.wzp.nflj.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author zp.wei
 * @date 2022/11/12 11:40
 */
public class WaterMarkUtil {

    /**
     * 生成水印
     *
     * @param srcImgPath       原图片路径
     * @param tarImgPath       待保存路径
     * @param markContentColor 颜色
     * @param content          内容
     * @param QrCodeContent    二维码内容
     * @param font             字体
     * @throws Exception 异常
     */
    public static void addWaterMark(String srcImgPath, String tarImgPath, String content, String QrCodeContent, Color markContentColor, Font font) throws Exception {
        // 读取原图片信息
        File srcImgFile = new File(srcImgPath);//得到文件
        Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
        int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
        int srcImgHeight = srcImg.getHeight(null);//获取图片的高
        // 加水印
        BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufImg.createGraphics();
        g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
        g.setColor(markContentColor); //设置水印颜色
        g.setFont(font);  //设置字体
        //设置水印的坐标 如果水印长度超过图片宽度则换行
        int fontLen = getWatermarkLength(content, g); // 水印文字总长度
        int line = fontLen / srcImgWidth;//文字长度相对于图片宽度应该有多少行
        int y = (line + 1) * font.getSize();
        System.out.println("水印文字总长度:" + fontLen + ",字符个数:" + content.length());
        //文字叠加,自动换行叠加
        int tempX = 50; // 文字初始x坐标位置
        int tempY = y;  // 文字初始y坐标位置
        int tempCharLen;//单字符长度
        int tempLineLen = 0;//单行字符总长度临时计算
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            char tempChar = content.charAt(i);
            tempCharLen = getCharLen(tempChar, g);
            tempLineLen += tempCharLen;
            if (tempLineLen >= srcImgWidth - 70) {
                //长度已经满一行,进行文字叠加
                g.drawString(sb.toString(), tempX, tempY);
                sb.delete(0, sb.length());//清空内容,重新追加
                tempY += font.getSize();
                tempLineLen = 0;
            }
            sb.append(tempChar);
        }
        g.drawString(sb.toString(), tempX, tempY);//最后叠加余下的文字
        //生成二维码
        if(!ObjUtil.isEmpty(QrCodeContent)){
            Image image = QRCodeUtil.encode(QrCodeContent, null, true);
            //将小图片绘到大图片上,300,300 .表示你的小图片在大图片上的位置。
            g.drawImage(image, 300, 1000, null);
        }
        g.dispose();
        FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
        ImageIO.write(bufImg, "jpg", outImgStream);
        System.out.println("添加水印完成");
        outImgStream.flush();
        outImgStream.close();
    }


    /**
     * 获取水印文字总长度
     *
     * @param waterMarkContent
     * @param g
     * @return
     */
    public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }


    /**
     * 获取水印对应文字的位置
     *
     * @param c
     * @param g
     * @return
     */
    public static int getCharLen(char c, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charWidth(c);
    }


}
