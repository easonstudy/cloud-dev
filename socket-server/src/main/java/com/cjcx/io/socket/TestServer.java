package com.cjcx.io.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 传统的多线程
 */
public class TestServer {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2000);

            System.out.println("服务器启动,等待客户端连接...");
            while (true) {
                //阻塞
                Socket socket = serverSocket.accept();
                pool.execute(new TestServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

