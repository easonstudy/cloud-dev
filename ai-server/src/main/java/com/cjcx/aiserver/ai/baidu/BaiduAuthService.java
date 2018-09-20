package com.cjcx.aiserver.ai.baidu;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 获取访问access_token类
 * 温馨提示：Access Token的有效期为30天（以秒为单位），请您集成时注意在程序中定期请求新的token。
 */
public class BaiduAuthService {
	
	final static String appId = "11638249"; //11324883

	// 官网获取的 API Key 更新为你注册的
	private final static String apiKey = "AlwTUYbPtpbQHKxPzXlNGRKj";    //
	// 官网获取的 Secret Key 更新为你注册的
	private final static String secretKey = "PRvKE3VWPLdrNNtOIehuMWbFLxUAzpTq"; //


    public static void main(String[] args) {
        BaiduAuthService service = new BaiduAuthService();
        //老郭
        //String image = service.getAuth("jSD6dK51ecVsIlSZyGQM06Se", "ALXlRR5XEWH51X9qF9WlIgFVZ27GiQab");
        //System.out.println("图像access_token:" + image);

        //马哥
        //String image = service.getAuth("AlwTUYbPtpbQHKxPzXlNGRKj", "PRvKE3VWPLdrNNtOIehuMWbFLxUAzpTq");
        //System.out.println("图像access_token:" + image);

        //老郭
        //String face  = service.getAuth("gcOVC13FSipvYn4aVw4hkYlS", "hTPAPwTQDs6xF6ytmVzOfx5bCKsXKt9u");
        //System.out.println("人脸access_token:" + face);
    }

    /**
     * 获取权限token
     * @return 返回示例：
     * {
     * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
     * "expires_in": 2592000
     * }
     */
    public static String getAuth() {
        return getAuth(apiKey, secretKey);
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

}

