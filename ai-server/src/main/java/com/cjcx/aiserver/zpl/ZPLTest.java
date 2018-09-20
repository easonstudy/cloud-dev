package com.cjcx.aiserver.zpl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.Inflater;

/**
 * ZPL 解压缩
 */
public class ZPLTest {
    public static void main(String[] args) {
        demoZpl();
    }

    public static void demoZpl() {
        try {
            //原始数据
            //~DGR:SSGFX000.GRF,35420,77,:Z64:
            //~DGR(表示图像):名称,总字节,宽字节数,:Z64:数据:
            String basic_str = "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";
            System.out.println("encode_data:" + basic_str.length());

            //MiMe Base64
            byte[] decode_bytes = Base64.getMimeDecoder().decode(basic_str);
            System.out.println("decode_bytes:" + decode_bytes.length);

            //LZ77解压
            byte[] result = new byte[35420];
            Inflater decompresser = new Inflater();
            decompresser.setInput(decode_bytes, 0, decode_bytes.length);
            int resultLength = decompresser.inflate(result); // 返回的是解压后的的数据包大小，
            decompresser.end();

            //保存图片
            byte[] dest_bytes = new byte[resultLength];
            System.arraycopy(result, 0, dest_bytes, 0, resultLength);
            saveImage(dest_bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 操作图片 必须 磁盘加载到内存中
     * BufferedImage 是Image的子类,  Image,BufferImage主要作用把图加载入内存
     * 这样方便操作图片的大小，变灰，透明度
     * BufferedImage bufferedImage = ImageIO.read(new FileInputStream(filePath));
     *
     * @param bytes
     */
    private static void saveImage(byte[] bytes) {
        int totalBytes = 35420;
        int row = 77;
        int width = row * 8;
        int height = totalBytes / row;

        //图像的颜色
        Color white = new Color(255, 255, 255);
        Color black = new Color(0, 0, 0);
        /* 保存图片
         * BufferedImage.TYPE_INT_RGB 不透明色的BufferedImage对象
         * BufferedImage.TYPE_INT_ARGB  透明色的BufferedImage对象
         */
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        /*
         * Graphics2D 主要用于图形的绘制，比如：线、圆、文本等
         * 直观的理解：Graphics2D 就相当于画笔，而BufferedImage 就是画笔绘制的结果
         */
        Graphics2D g = image.createGraphics();

        //需要生成RGB格式，需要做如下配置
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        //释放资源
        g.dispose();

        g = image.createGraphics();

        //抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //默认图形白色, 并以初始位置坐标(0,0) 填充width宽, height高的图像
        g.setColor(white);
        g.fillRect(0, 0, width, height);

        /*
         * 图形填充颜色 1个字节占图形的8个点
         */
        for (int y = 0; y < height; y++) {          //高
            for (int x = 0; x < row; x++) {         //宽
                byte xb = bytes[row * y + x];
                for (int z = 0; z < 8; z++) {
                    int bit = 7 - z % 8;            //字节高位在前
                    if ((xb >> bit & 0x01) == 1) {
                        g.setColor(black);
                        //drawRect 画圈 在x, y坐标 画width, height的图像
                        g.drawRect(x * 8 + z, y, 1, 1);
                    }
                }
            }
        }

        //图形旋转
        //int degree = 90;
        //image = rotateImage(image, offset);

        try {
            ImageIO.write(image, "png", new File("C:\\Users\\guo\\Desktop\\3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 旋转图片为指定角度
     *
     * @param bufferedimage 目标图像
     * @param degree        旋转角度
     * @return
     */
    public static BufferedImage rotateImage(final BufferedImage bufferedimage,
                                            final int degree) {
        int w = bufferedimage.getWidth();// 得到图片宽度。
        int h = bufferedimage.getHeight();// 得到图片高度。
        int type = bufferedimage.getColorModel().getTransparency();// 得到图片透明度。
        BufferedImage img;// 空的图片。
        Graphics2D graphics2d;// 空的画笔。
        (graphics2d = (img = new BufferedImage(h, w, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);// 旋转，degree是整型，度数，比如垂直90度。
        graphics2d.drawImage(bufferedimage, 0, 0, null);// 从bufferedimagecopy图片至img，0,0是img的坐标。
        graphics2d.dispose();
        return img;// 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。

    }
}