package com.cjcx.wechat.open.weixin;


import com.cjcx.wechat.open.utils.WeixinUtil;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;


/**
 * 素材管理
 * 参数	        是否必须	    说明
 * access_token	是	    调用接口凭证
 * type	        是	    媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
 * media	    是	    form-data中媒体文件标识，有filename、filelength、content-type等信息
 */
public class Material extends WeixinUtil {

    /**
     * MediaAPI.mediaUpload("access_token",MediaType.image,File or InputStream or URI);
     */

    private static final String url = "https://api.weixin.qq.com/cgi-bin/media/upload?";

    /**
     * 新增临时素材
     * @param type
     * @param fileUrl 文件路径 或者 URL
     * @return
     */
    public String addMaterial(String type, String fileUrl) {
        StringBuffer buff = new StringBuffer(url);
        buff.append("access_token=").append(super.getAccessToken()).append("&type=").append(type);

        try {
            URI uri = new URI("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //MediaAPI.mediaUpload("access_token", MediaType.image, );


        return null;
    }

    /**
     * 上传素材
     */
    private void postMaterial(String url){

    }

    public static void main(String[] args) {
        Material material = new Material();
        material.b();
    }

    /**
     * 网络下载资源图片
     */
    public void a(){
        try {
            URL url = new URL("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/mantpl/img/news/news_88aeb6fe.png");
            DataInputStream dis = new DataInputStream(url.openStream());

            File file = new File("C:\\Users\\guo\\Desktop\\a.png");
            if(!file.exists())
                file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buff = new byte[1024];
            int length;
            while ((length = dis.read(buff)) > 0) {
                outputStream.write(buff, 0 , length);
            }
            dis.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void b(){
        try {
            URL url = new URL("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/mantpl/img/news/news_88aeb6fe.png");
            InputStream inputStream = url.openStream();


            File file = new File("C:\\Users\\guo\\Desktop\\b.png");
            if(!file.exists())
                file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buff = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0 , length);
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
