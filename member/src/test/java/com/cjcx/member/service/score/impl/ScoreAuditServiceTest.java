package com.cjcx.member.service.score.impl;

import com.cjcx.member.object.ScanReceiptData;
import com.cjcx.member.utils.GsonUtil;
import com.cjcx.member.utils.JacksonUtil;
import com.cjcx.member.utils.RequestUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ScoreAuditServiceTest {

    private static Logger logger = LoggerFactory.getLogger(ScoreAuditServiceTest.class);

    @Test
    public void addScoreAudit() throws Exception {

        //小票图片
        String imgUrl = "http://120.79.210.194/images/receipt_audit/b1f7d5ea-d914-4041-865f-e7a757b4e462.jpg";
        //本地文件 二进制流
        /*String imgFile = "C:\\Users\\guo\\Desktop\\b1f7d5ea-d914-4041-865f-e7a757b4e462.jpg";
        InputStream inputStream = new FileInputStream(imgFile);
        //InputStream inputStream = getImageInputStream(imgUrl);
        String base64 = getImageBase64(inputStream);*/


        Long s = System.currentTimeMillis();
        //String reqUrl = "http://www.yunpiaobox.cn:18080/mall/interface/uploadReceiptImage.service";
        String reqUrl = "http://localhost:8080/mall/interface/uploadReceiptImage.service";
        Map map = new HashMap();
        map.put("serialNo", "N000007");
        map.put("url", imgUrl);
        //map.put("image", URLEncoder.encode(base64,"UTF-8"));
        logger.info("用时1:"+(System.currentTimeMillis() - s));
        s = System.currentTimeMillis();
        logger.info("自动审核地址:" + reqUrl +", 参数:");
        String result = "{\"data\":{\"payPrice\":0,\"state\":0,\"orderNum\":\"311BE213391E06F9\",\"wechatPrice\":154.5,\"shopsName\":\"上海银联测试\",\"receiptNum\":\"201806051842131230\",\"totalNum\":0,\"serialNo\":\"N63349\",\"cardPrice\":0,\"thirdPartyPrice\":0,\"cashPrice\":0,\"otherPrice\":0,\"payFalg\":\"微信,\",\"alipayPrice\":0,\"shopsId\":75,\"totalPrice\":154.5},\"returnCode\":0}";
                //RequestUtil.doPostByJson(reqUrl, GsonUtil.toJson(map));
        logger.info("自动审核结果:" + result);


        JSONObject json = new JSONObject(result);
        Integer returnCode = json.getInt("returnCode");
        if(returnCode == 0) {
            ScanReceiptData receipt = JacksonUtil.json2Bean(json.getString("data"), ScanReceiptData.class);
            logger.info("小票信息:" + receipt.toString());
        }else {
            logger.info("失败:" + returnCode);
        }

        logger.info("用时2:"+(System.currentTimeMillis() - s));



    }

    private static InputStream getImageInputStream(String netUrl) {
        try {
            URL url = new URL(netUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            return conn.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: 根据图片inputStream转换为base64编码字符串
     * @Author:
     * @CreateTime:
     * @return
     */
    private static String getImageBase64(InputStream inputStream) throws Exception {
        byte[] data = readInputStream(inputStream);
        System.out.println("Base64 length:" + data.length);
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    private static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

}