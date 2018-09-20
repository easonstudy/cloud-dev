package com.cjcx.picture;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

public class PictureFTPTest {
    @Test
    public void test() {
        try {
            //本地文件
            File file =  new File("C:\\Users\\guo\\Desktop\\f0cdb1e23a.jpg");

            // 1. 创建一个FtpClient对象
            FTPClient ftpClient = new FTPClient();
            // 2. 创建 ftp 连接
            ftpClient.connect("120.79.210.194", 21);
            // 3. 登录 ftp 服务器
            ftpClient.login("ftpuser", "bb123456");
            // 4. 读取本地文件
            FileInputStream inputStream = new FileInputStream(file);
            // 5. 设置上传的路径
            ftpClient.changeWorkingDirectory("/usr/local/upload/images");
            // 6. 修改上传文件的格式为二进制
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 7. 服务器存储文件，第一个参数是存储在服务器的文件名，第二个参数是文件流
            ftpClient.storeFile("girl.jpg", inputStream);
            // 8. 关闭连接
            ftpClient.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
