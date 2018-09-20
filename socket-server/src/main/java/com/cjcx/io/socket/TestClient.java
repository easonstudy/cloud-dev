package com.cjcx.io.socket;

import java.io.*;
import java.net.Socket;

public class TestClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 2000);

            OutputStream os = socket.getOutputStream();
            //os.write("用户名：Administrator;密码：1234".getBytes("UTF-8"));
            PrintWriter pw = new PrintWriter(os);
            pw.write("用户名：Administrator;密码：1234");
            pw.flush();
            socket.shutdownOutput();

            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info = null;
            while ((info = br.readLine()) != null) {
                System.out.println("我是客户端，服务器说：" + info);
            }
            pw.close();
            os.close();
            is.close();
            br.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


}
