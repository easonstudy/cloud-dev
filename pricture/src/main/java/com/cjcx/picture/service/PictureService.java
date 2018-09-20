package com.cjcx.picture.service;

import com.cjcx.picture.util.ImgUtil;
import com.cjcx.picture.util.IpUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
@PropertySource({"classpath:resource.properties"})
public class PictureService extends AdminService implements IPictureService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ERROR_CODE = "errorCode";
    private static final String ERROR_MSG = "errorMsg";

    // 通过 Spring4 的 Value注解，获取配置文件中的属性值
    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;    // ftp 服务器ip地址
    @Value("${FTP_PORT}")
    private Integer FTP_PORT;        // ftp 服务器port，默认是21
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;    // ftp 服务器用户名
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;    // ftp 服务器密码

    @Value("${FTP_GiFT_PATH}")
    private String FTP_GiFT_PATH;               // ftp 服务器存储礼品图片的绝对路径

    @Value("${FTP_RECEIPT_AUDIT_PATH}")
    private String FTP_RECEIPT_AUDIT_PATH;      // ftp 服务器存储未审核图片的绝对路径

    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;    // ftp 服务器外网访问图片路径

    @Value("${IMAGE_BASE_IP}")
    private String IMAGE_BASE_IP;    // ftp 服务器外网访问图片路径


    @Override
    public Map uploadPicture(MultipartFile uploadFile, Integer local) {
        Map map = new HashMap();
        try {
            //logger.info("上传文件 local:{}", local);
            boolean uploadToLocal = isUploadLocal();
            if (uploadToLocal) {
                map = defaultServer(uploadFile);
            } else {
                map = vsftpServer(uploadFile);
            }
            /*if (local == 0)
                map = defaultServer(uploadFile);
            else
                map = vsftpServer(uploadFile);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private Map defaultServer(MultipartFile file) {
        Map map = new HashMap();
        try {
            String fileName = file.getOriginalFilename();

            List<String> imgList = new ArrayList<>();
            imgList.add("png");
            imgList.add("gif");
            imgList.add("jpg");
            imgList.add("jpeg");
            imgList.add("bmp");
            imgList.add("tiff");

            String type = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!imgList.contains(type)) {
                map.put(ERROR_CODE, "-1");
                map.put(ERROR_MSG, "格式错误");
                return map;
            }

            //位置
            String filePath = FTP_GiFT_PATH + fileName;
            fileName = UUID.randomUUID().toString() + "." + type;

            file.transferTo(new File(filePath));

            map.put(ERROR_CODE, "0");
            map.put(ERROR_MSG, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            map.put(ERROR_CODE, "-99");
            map.put(ERROR_MSG, "异常");
        }
        return map;
    }

    private Map vsftpServer(MultipartFile file) {
        Map map = new HashMap();
        try {
            String fileName = file.getOriginalFilename();

            List<String> imgList = new ArrayList<>();
            imgList.add("png");
            imgList.add("gif");
            imgList.add("jpg");
            imgList.add("jpeg");

            String type = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!imgList.contains(type)) {
                map.put(ERROR_CODE, "-1");
                map.put(ERROR_MSG, "格式错误");
                return map;
            }

            //存贮的位置
            fileName = UUID.randomUUID().toString() + "." + type;
            //图片上传
            boolean result = uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD,
                    file.getInputStream(), FTP_GiFT_PATH, fileName);

            //logger.info("存贮位置:" + FTP_BASE_PATH + ", 文件名:" + fileName);
            //返回结果
            if (!result) {
                map.put(ERROR_CODE, "-2");
                map.put(ERROR_MSG, "上传服务器失败");
                return map;
            }
            map.put(ERROR_CODE, "0");
            map.put(ERROR_MSG, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            map.put(ERROR_CODE, "-99");
            map.put(ERROR_MSG, "异常");
        }
        return map;
    }

    /**
     * @param netUrl 网络图片
     * @param local  是否启用http client 上传至图片服务器(如果改项目在本地, 不启动)
     * @return
     */
    public Map uploadPicture(String netUrl, Integer local) {
        Map map = new HashMap();
        try {
            logger.info("Image net url:{}, local :{}", netUrl, local);
            URL url = new URL(netUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();   //通过输入流获取图片数据


            //存贮的位置
            String fileName = UUID.randomUUID().toString() + ".jpg";
            boolean result = false;
            //图片上传
            boolean uploadToLocal = isUploadLocal();
            if (uploadToLocal) {
                Map uploadMap = saveFile(inStream, FTP_RECEIPT_AUDIT_PATH, fileName);
                fileName = uploadMap.get("fileName").toString();
                result = (boolean)uploadMap.get("result");
            } else {
                result = uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, inStream, FTP_RECEIPT_AUDIT_PATH, fileName);
                logger.info("存贮位置:" + FTP_RECEIPT_AUDIT_PATH + ", 文件名:" + fileName + ", result:" + result);
            }

            //返回结果
            if (!result) {
                map.put(ERROR_CODE, "-2");
                map.put(ERROR_MSG, "上传服务器失败");
                return map;
            }
            map.put(ERROR_CODE, "0");
            map.put(ERROR_MSG, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            map.put(ERROR_CODE, "-99");
            map.put(ERROR_MSG, "异常");
        }
        return map;
    }

    private Map saveFile(InputStream inputStream, String fileFolder, String fileName) {
        Map map = new HashMap();
        try {
            byte[] bytes = ImgUtil.readInputStream(inputStream);

            StringBuffer buffer = new StringBuffer();
            String stmp = "";
            for (int i = 0; i < 2; i++) {
                stmp = Integer.toHexString(bytes[i] & 0xFF);
                if (stmp.length() == 1) stmp = "0" + stmp;
                buffer.append(stmp.toUpperCase());
            }
            String result = buffer.toString();

            String suffix = ".jpg";
            String type = ".jpg";
            if ("FFD8".equals(result)) {
                type = ".jpeg";
            } else if ("FFD9".equals(result)) {
                type = ".jpg";
            } else if ("4749".equals(result)) {
                type = ".gif";
            } else if ("424D".equals(result)) {
                type = ".bmp";
            } else if ("4D4D".equals(result)) {
                type = ".tiff";
            }

            fileName = fileName.replaceAll(suffix, ".jpeg");
            String filePath = fileFolder + fileName;

            logger.info("图片16进制:{}" + result + ", 类型:" + type);
            logger.info("存贮位置:" + filePath);
            ImgUtil.buff2Image(bytes, filePath);

            map.put("fileName", fileName);
            map.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Image stream wrong:{}", e.getMessage());
            map.put("fileName", fileName);
            map.put("result", false);
        }
        return map;
    }


    /**
     * ftp 上传图片方法
     *
     * @param ip          ftp 服务器ip地址
     * @param port        ftp 服务器port，默认是21
     * @param account     ftp 服务器用户名
     * @param passwd      ftp 服务器密码
     * @param inputStream 文件流
     * @param workingDir  ftp 服务器存储图片的绝对路径
     * @param fileName    上传到ftp 服务器文件名
     * @throws Exception
     */
    public boolean uploadFile(String ip, Integer port, String account, String passwd,
                              InputStream inputStream, String workingDir, String fileName) throws Exception {
        boolean result = false;
        // 1. 创建一个FtpClient对象
        FTPClient ftpClient = new FTPClient();
        try {
            // 2. 创建 ftp 连接
            ftpClient.connect(ip, port);
            // 3. 登录 ftp 服务器
            ftpClient.login(account, passwd);
            int reply = ftpClient.getReplyCode(); // 获取连接ftp 状态返回值
            logger.info("reply code : " + reply);
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect(); // 如果返回状态不再 200 ~ 300 则认为连接失败
                return result;
            }
            // 4. 读取本地文件
//			FileInputStream inputStream = new FileInputStream(new File("F:\\hello.png"));
            // 5. 设置上传的路径
            ftpClient.changeWorkingDirectory(workingDir);
            // 6. 修改上传文件的格式为二进制
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 7. 服务器存储文件，第一个参数是存储在服务器的文件名，第二个参数是文件流
            if (!ftpClient.storeFile(fileName, inputStream)) {
                logger.info(" 服务器存储文件异常");
                return result;
            }

            // 8. 关闭连接
            inputStream.close();
            ftpClient.logout();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("异常 : " + e.getMessage());
        } finally {
            // FIXME 听说，项目里面最好少用try catch 捕获异常，这样会导致Spring的事务回滚出问题？？？难道之前写的代码都是假代码！！！
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }


    /**
     * @return
     */
    private boolean isUploadLocal() {
        //获取本机IP
        String ip = IpUtil.getIp(getHttpServletRequeset());
        logger.info("上传文件 本机IP:{}, 图片服务器IP:{}, 相同:{}", ip, IMAGE_BASE_IP, IMAGE_BASE_IP.equals(ip));
        if (IMAGE_BASE_IP.equals(ip)) {
            logger.info("本机上传");
            return true;
        } else {
            logger.info("远程上传");
            return false;
        }
    }
}
