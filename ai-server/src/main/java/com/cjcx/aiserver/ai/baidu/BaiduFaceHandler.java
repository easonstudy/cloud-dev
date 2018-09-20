package com.cjcx.aiserver.ai.baidu;

import com.cjcx.aiserver.ai.AbstractFaceHandler;
import com.cjcx.aiserver.ai.config.AbstractConfig;
import com.cjcx.aiserver.ai.config.FaceConfig;
import com.cjcx.aiserver.ai.utils.GsonUtils;
import com.cjcx.aiserver.ai.utils.HttptUtils;
import com.cjcx.aiserver.ai.utils.ImageUtils;
import com.cjcx.aiserver.obj.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 人脸处理类
 */
@Slf4j
@Component
public class BaiduFaceHandler extends AbstractFaceHandler {

    /**
     * 请求地址
     */
    private static final String REQUEST_URL = "https://aip.baidubce.com/rest/2.0/face/v3/detect";

    public BaiduFaceHandler(){
        init();
    }

    private void init(){
        config = new FaceConfig();
    }

    /**
     * 执行识别
     * @return
     */
    @Override
    public ResultObject execute(AbstractConfig config) {
        ResultObject r = new ResultObject();
        try {
            //图片预处理
            /*File file = new File("C:\\Users\\guo\\Desktop\\22.jpg");
            InputStream in = new FileInputStream(file);
            String image64 = ImageUtils.getBase64ByInputStream(in);*/

            String image64 = "";
            if(StringUtils.hasLength(config.getImage())){
                log.info("Baidu 请求参数 Base64:{}", config.getImage());
                image64 = config.getImage();
            } else if(StringUtils.hasLength(config.getUrl())) {
                log.info("Baidu 请求参数 图像地址:{}", config.getUrl());
                image64 = ImageUtils.getBase64ByNetworkImageUrl(config.getUrl());
            } else {
                r.setErrCode("-1");
                r.setData("缺少参数");
                return r;
            }
            //参数
            Map<String, Object> map = new HashMap<>();
            map.put("image", image64);
            map.put("image_type", "BASE64");
            map.put("face_field", "age,beauty,expression,face_shape,gender,glasses,landmark,race,quality,face_type");

            String url = REQUEST_URL + "?access_token=" + BaiduTokenManager.getToken(BaiduAccountManager.getFaceDefaultApiKey());
            String params = GsonUtils.getInstance().toJson(map);

            //请求
            String result = HttptUtils.doPostByJson(url, params);

            log.info("Baidu result:" + result);
            //结果
            //JSONObject jsonObject = new JSONObject(result);
            r.setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-10001");
            r.setData("异常:"+e.getMessage());
        } finally {

        }
        return r;
    }







}
