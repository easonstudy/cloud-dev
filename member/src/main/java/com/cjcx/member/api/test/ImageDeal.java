package com.cjcx.member.api.test;

import com.cjcx.member.framework.security.AESCrypt;
import com.cjcx.member.framework.zxing.CreateParseCode;

import java.io.File;
import java.net.URLEncoder;

public class ImageDeal {

    public static void main(String[] args) {


        String hrefkey = "oq9suwB_dGGaJRMwOuY629OrOwXM:"+System.currentTimeMillis();
        try {
            hrefkey = AESCrypt.encrypt(hrefkey);
            //URLEncoder
            hrefkey = URLEncoder.encode(hrefkey, "UTF-8");
            System.out.println("完善个人资料 密钥:" + hrefkey);
        } catch (Exception e) {
            e.printStackTrace();
        }




        /*String qrImgPath = "C:" + File.separator + "二维码生成" + File.separator + "qr-img.png";
        //String receiptImgPath = "C:" + File.separator + "二维码生成" + File.separator + "qr-img2.png";
        String receiptImgPath = "C:" + File.separator + "二维码生成" + File.separator + "527.png";

        *//*CreateParseCode cpCode = new CreateParseCode();
        //生成二维码
        cpCode.createCode(qrImgPath);*//*

        String outputPath = "C:" + File.separator + "二维码生成" + File.separator + "new.png";
        String [] files = {receiptImgPath, qrImgPath};

        ImgUtil.mergeImage(files, 2, outputPath);*/
    }

}
