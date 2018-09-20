package com.cjcx.io.socket;

import java.io.*;
import java.net.Socket;

public class TestServerHandler implements Runnable {
    Socket socket;

    public TestServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //客户端输入数据
        InputStream is = null;
        //客户端输入数据 读取
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;

        //服务端输出数据
        OutputStream os = null;
        //更好的操作字符流
        PrintWriter pw = null;
        try {
            is = socket.getInputStream();

            //byte[] bytes = readInputStream(inputStream);
            //System.out.println(bytes.length);
            //String message = new String(bytes, "UTF-8");
            // System.out.println("我是服务器，客户端说：" + message);
            inputStreamReader = new InputStreamReader(is);
            br = new BufferedReader(inputStreamReader);

            String message = null;
            while ((message = br.readLine()) != null) {
                System.out.println("我是服务器，客户端说：" + message);
            }
            socket.shutdownInput();

            Thread.sleep(3000);
            String response_content = "你好,客户端";

            os = socket.getOutputStream();
            pw = new PrintWriter(os);
            pw.write(response_content);
            pw.flush();
            socket.shutdownOutput();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pw != null)
                    pw.close();

                if (br != null)
                    br.close();

                if (inputStreamReader != null)
                    inputStreamReader.close();

                if (is != null)
                    is.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] readInputStream(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = is.read()) != -1) {
            bos.write(bytes, 0, len);
        }
        return bos.toByteArray();
    }
}
